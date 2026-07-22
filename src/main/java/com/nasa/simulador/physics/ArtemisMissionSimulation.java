package com.nasa.simulador.physics;

import java.util.List;

import org.orekit.bodies.CelestialBody;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.forces.maneuvers.ImpulseManeuver;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.numerical.NumericalPropagator;
import org.orekit.time.AbsoluteDate;

/**
 * Ejecuta la canalización completa del motor orbital Artemis II.
 */
public final class ArtemisMissionSimulation {

    private ArtemisMissionSimulation() {
        // Evita crear instancias.
    }

    /**
     * Ejecuta la misión desde la órbita de estacionamiento
     * hasta la reentrada o hasta el límite temporal.
     *
     * @param tliParameters parámetros configurables de la TLI
     * @return resultado completo de la misión
     */
    public static MissionSimulationResult run(
            TLIParameters tliParameters
    ) {

        System.out.println();
        System.out.println(
                "=================================================="
        );
        System.out.println(
                "       SIMULACION COMPLETA ARTEMIS II"
        );
        System.out.println(
                "=================================================="
        );

        Orbit initialOrbit =
                ParkingOrbitFactory.createDefaultOrbit();

        NumericalPropagator propagator =
                NumericalPropagatorFactory.create(
                        initialOrbit
                );

        AbsoluteDate ignitionDate =
                TLIManeuverFactory.getIgnitionDate(
                        initialOrbit,
                        tliParameters
                );

        ImpulseManeuver tliManeuver =
                TLIManeuverFactory.create(
                        initialOrbit,
                        tliParameters
                );

        propagator.addEventDetector(
                tliManeuver
        );

        OneAxisEllipsoid earth =
                EarthModelFactory.createWgs84Earth();

        CelestialBody moon =
                CelestialBodyFactory.getMoon();

        TrajectoryCollector collector =
                new TrajectoryCollector(
                        ignitionDate,
                        earth,
                        moon
                );

        /*
         * El integrador sigue siendo adaptativo, pero Orekit
         * entrega un estado interpolado cada cinco minutos.
         */
        propagator.getMultiplexer().add(
                MissionParameters.TRAJECTORY_SAMPLE_STEP_S,
                collector
        );

        MissionEvents events =
        new MissionEvents(ignitionDate);

        events.attachTo(
                propagator,
                earth
        );

        AbsoluteDate maximumMissionDate =
                ignitionDate.shiftedBy(
                        MissionParameters
                                .MAX_MISSION_DURATION_S
                );

        System.out.println();
        System.out.printf(
                "[INFO] Delta-v TLI: %.3f km/s%n",
                tliParameters.getDeltaVMagnitudeMps()
                        / MissionParameters.KM
        );

        System.out.printf(
                "[INFO] Encendido TLI: %.3f horas "
                        + "después del inicio%n",
                tliParameters.getIgnitionOffsetSeconds()
                        / MissionParameters.HOUR
        );

        System.out.printf(
                "[INFO] Intervalo de muestreo: %.0f segundos%n",
                MissionParameters.TRAJECTORY_SAMPLE_STEP_S
        );

        System.out.println(
                "[INFO] Ejecutando propagación completa..."
        );

        SpacecraftState finalState =
                propagator.propagate(
                        maximumMissionDate
                );

        List<TrajectoryPoint> trajectory =
                collector.getPoints();

        validateTrajectory(trajectory);

        MissionSimulationResult result =
                new MissionSimulationResult(
                        trajectory,
                        finalState,
                        events
                );

        printReport(
                result,
                ignitionDate
        );

        return result;
    }

    private static void validateTrajectory(
            List<TrajectoryPoint> trajectory
    ) {

        if (trajectory.size()
                < MissionParameters.MIN_TRAJECTORY_POINTS) {

            throw new IllegalStateException(
                    "La trayectoria contiene solamente "
                            + trajectory.size()
                            + " puntos. Se requieren al menos "
                            + MissionParameters
                                    .MIN_TRAJECTORY_POINTS
                            + "."
            );
        }

        System.out.println();
        System.out.println(
                "[OK] Trayectoria capturada correctamente."
        );

        System.out.println(
                "[OK] Cantidad mínima de 500 puntos superada."
        );
    }

    private static void printReport(
            MissionSimulationResult result,
            AbsoluteDate ignitionDate
    ) {

        List<TrajectoryPoint> trajectory =
                result.getTrajectory();

        MissionEvents events =
                result.getEvents();

        SpacecraftState finalState =
                result.getFinalState();

        double propagatedHours =
                finalState.getDate()
                        .durationFrom(ignitionDate)
                        / MissionParameters.HOUR;

        System.out.println();
        System.out.println(
                "================ INFORME FINAL ================"
        );

        System.out.printf(
                "Puntos de trayectoria: %d%n",
                trajectory.size()
        );

        System.out.printf(
                "Tiempo propagado: %.3f horas%n",
                propagatedHours
        );

        System.out.printf(
                "Fecha final: %s%n",
                finalState.getDate()
        );

        System.out.printf(
                "Masa final: %.3f kg%n",
                finalState.getMass()
        );

        if (events.hasLunarPeriapsis()) {

            SpacecraftState lunarState =
                    events.getLunarPeriapsisState();

            double lunarEventHours =
                    lunarState.getDate()
                            .durationFrom(ignitionDate)
                            / MissionParameters.HOUR;

            double lunarAltitudeKm =
                    events.getLunarPeriapsisAltitudeM()
                            / MissionParameters.KM;

            System.out.println();
            System.out.println(
                    "[OK] Periapsis lunar registrado."
            );

            System.out.printf(
                    "Fecha del periapsis lunar: %s%n",
                    lunarState.getDate()
            );

            System.out.printf(
                    "Tiempo desde TLI: %.3f horas%n",
                    lunarEventHours
            );

            System.out.printf(
                    "Altitud aproximada sobre la Luna: "
                            + "%.3f km%n",
                    lunarAltitudeKm
            );

        } else if (events.hasLunarApproachCandidate()) {

    SpacecraftState candidateState =
            events.getLunarPeriapsisState();

    double candidateHours =
            candidateState.getDate()
                    .durationFrom(ignitionDate)
                    / MissionParameters.HOUR;

    double candidateAltitudeKm =
            events.getLunarPeriapsisAltitudeM()
                    / MissionParameters.KM;

    System.out.println();
    System.out.println(
            "[WARNING] Se encontró un mínimo post-TLI, "
                    + "pero está demasiado lejos para "
                    + "considerarse sobrevuelo lunar."
    );

    System.out.printf(
            "Tiempo desde TLI: %.3f horas%n",
            candidateHours
    );

    System.out.printf(
            "Altitud aproximada sobre la Luna: %.3f km%n",
            candidateAltitudeKm
    );

    System.out.printf(
            "Límite aceptado por el prototipo: %.3f km%n",
            MissionParameters
                    .MAX_VALID_LUNAR_FLYBY_ALTITUDE_M
                    / MissionParameters.KM
    );

} else {

    System.out.println();
    System.out.println(
            "[WARNING] No se encontró un acercamiento "
                    + "lunar posterior a la TLI."
    );
}

        if (events.hasReentry()) {

            SpacecraftState reentryState =
                    events.getReentryState();

            double reentryHours =
                    reentryState.getDate()
                            .durationFrom(ignitionDate)
                            / MissionParameters.HOUR;

            System.out.println();
            System.out.println(
                    "[OK] Interfaz de reentrada registrada."
            );

            System.out.printf(
                    "Fecha de reentrada: %s%n",
                    reentryState.getDate()
            );

            System.out.printf(
                    "Tiempo desde TLI: %.3f horas%n",
                    reentryHours
            );

            System.out.printf(
                    "Altitud de interfaz: %.3f km%n",
                    MissionParameters
                            .REENTRY_INTERFACE_ALTITUDE_M
                            / MissionParameters.KM
            );

        } else {

            System.out.println();
            System.out.println(
                    "[WARNING] La trayectoria no cruzó "
                            + "los 120 km durante el tiempo máximo."
            );

            System.out.println(
                    "[INFO] El detector funciona, pero la "
                            + "geometría TLI debe ajustarse para "
                            + "producir una reentrada real."
            );
        }

        System.out.println();
        System.out.println(
                "[SUCCESS] Canalización orbital ejecutada."
        );

        System.out.println(
                "================================================="
        );
    }
}
