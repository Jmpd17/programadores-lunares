package com.nasa.simulador.physics;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.ode.events.Action;
import org.orekit.bodies.CelestialBody;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.forces.maneuvers.ImpulseManeuver;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.events.AltitudeDetector;
import org.orekit.propagation.events.EventDetector;
import org.orekit.propagation.events.handlers.EventHandler;
import org.orekit.propagation.numerical.NumericalPropagator;
import org.orekit.propagation.sampling.OrekitFixedStepHandler;
import org.orekit.time.AbsoluteDate;

/**
 * Busca automaticamente parametros TLI.
 *
 * La seleccion prioriza:
 * 1. Sobrevuelo lunar valido.
 * 2. Reentrada terrestre descendente a 120 km.
 * 3. Menor altitud lunar entre los candidatos completos.
 */
public final class TLIParameterSearch {

    private TLIParameterSearch() {
        // Evita crear instancias.
    }

    public static TLIParameters findBest() {

        System.out.println();
        System.out.println(
                "=================================================="
        );
        System.out.println(
                "       BUSQUEDA AUTOMATICA DE TLI"
        );
        System.out.println(
                "=================================================="
        );

        Orbit initialOrbit =
                ParkingOrbitFactory.createDefaultOrbit();

        CelestialBody moon =
                CelestialBodyFactory.getMoon();

        OneAxisEllipsoid earth =
                EarthModelFactory.createWgs84Earth();

        double orbitalPeriodHours =
                initialOrbit.getKeplerianPeriod()
                        / MissionParameters.HOUR;

        TLIParameters bestLunarParameters = null;
        double bestLunarAltitudeM =
                Double.POSITIVE_INFINITY;
        double bestLunarApproachHours =
                Double.NaN;

        TLIParameters bestCompleteParameters = null;
        double bestCompleteAltitudeM =
                Double.POSITIVE_INFINITY;
        double bestCompleteApproachHours =
                Double.NaN;

        int candidateNumber = 0;
        int validLunarCandidates = 0;
        int reentryCandidates = 0;

        for (
                int ignitionIndex = 0;
                ignitionIndex
                        < MissionParameters
                                .TLI_SEARCH_IGNITION_SAMPLES;
                ignitionIndex++
        ) {

            double ignitionHours =
                    orbitalPeriodHours
                            * ignitionIndex
                            / MissionParameters
                                    .TLI_SEARCH_IGNITION_SAMPLES;

            for (
                    double deltaV =
                            MissionParameters
                                    .TLI_SEARCH_MIN_DELTA_V_KM_S;
                    deltaV <=
                            MissionParameters
                                    .TLI_SEARCH_MAX_DELTA_V_KM_S
                                    + 1.0e-9;
                    deltaV +=
                            MissionParameters
                                    .TLI_SEARCH_DELTA_V_STEP_KM_S
            ) {

                candidateNumber++;

                TLIParameters candidate =
                        new TLIParameters(
                                deltaV,
                                ignitionHours,
                                1.0,
                                0.0,
                                0.0,
                                MissionParameters
                                        .DEFAULT_TLI_ISP_S
                        );

                NumericalPropagator lunarPropagator =
                        NumericalPropagatorFactory
                                .createQuiet(initialOrbit);

                ImpulseManeuver maneuver =
                        TLIManeuverFactory.create(
                                initialOrbit,
                                candidate
                        );

                lunarPropagator.addEventDetector(
                        maneuver
                );

                AbsoluteDate ignitionDate =
                        TLIManeuverFactory.getIgnitionDate(
                                initialOrbit,
                                candidate
                        );

                ClosestApproachTracker tracker =
                        new ClosestApproachTracker(
                                ignitionDate,
                                moon
                        );

                lunarPropagator.getMultiplexer().add(
                        MissionParameters
                                .TLI_SEARCH_SAMPLE_STEP_S,
                        tracker
                );

                lunarPropagator.propagate(
                        ignitionDate.shiftedBy(
                                MissionParameters
                                        .TLI_SEARCH_DURATION_S
                        )
                );

                if (!tracker.hasResult()) {
                    continue;
                }

                double altitudeM =
                        tracker.getMinimumDistanceM()
                                - MissionParameters
                                        .MOON_RADIUS_M;

                double approachHours =
                        tracker.getClosestState()
                                .getDate()
                                .durationFrom(ignitionDate)
                                / MissionParameters.HOUR;

                if (altitudeM < bestLunarAltitudeM) {

                    bestLunarAltitudeM = altitudeM;
                    bestLunarParameters = candidate;
                    bestLunarApproachHours =
                            approachHours;

                    System.out.printf(
                            "[MEJOR LUNAR] dv %.3f km/s | "
                                    + "encendido %.3f h | "
                                    + "altitud %.3f km | "
                                    + "tiempo %.3f h%n",
                            deltaV,
                            ignitionHours,
                            altitudeM / MissionParameters.KM,
                            approachHours
                    );
                }

                boolean validLunarFlyby =
                        altitudeM <=
                                MissionParameters
                                        .MAX_VALID_LUNAR_FLYBY_ALTITUDE_M;

                if (!validLunarFlyby) {
                    continue;
                }

                validLunarCandidates++;

                boolean hasReentry =
                        detectsReentry(
                                initialOrbit,
                                earth,
                                candidate
                        );

                if (!hasReentry) {
                    continue;
                }

                reentryCandidates++;

                if (altitudeM < bestCompleteAltitudeM) {

                    bestCompleteAltitudeM = altitudeM;
                    bestCompleteParameters = candidate;
                    bestCompleteApproachHours =
                            approachHours;

                    System.out.printf(
                            "[RETORNO] dv %.3f km/s | "
                                    + "encendido %.3f h | "
                                    + "altitud lunar %.3f km%n",
                            deltaV,
                            ignitionHours,
                            altitudeM / MissionParameters.KM
                    );
                }
            }
        }

        TLIParameters selectedParameters;
        double selectedAltitudeM;
        double selectedApproachHours;
        boolean completeMissionFound;

        if (bestCompleteParameters != null) {

            selectedParameters =
                    bestCompleteParameters;
            selectedAltitudeM =
                    bestCompleteAltitudeM;
            selectedApproachHours =
                    bestCompleteApproachHours;
            completeMissionFound = true;

        } else {

            selectedParameters =
                    bestLunarParameters;
            selectedAltitudeM =
                    bestLunarAltitudeM;
            selectedApproachHours =
                    bestLunarApproachHours;
            completeMissionFound = false;
        }

        if (selectedParameters == null) {
            throw new IllegalStateException(
                    "La busqueda no produjo ningun candidato."
            );
        }

        System.out.println();
        System.out.println(
                "--------------- MEJOR RESULTADO ---------------"
        );

        System.out.printf(
                "Candidatos evaluados: %d%n",
                candidateNumber
        );

        System.out.printf(
                "Sobrevuelos lunares validos: %d%n",
                validLunarCandidates
        );

        System.out.printf(
                "Candidatos con reentrada: %d%n",
                reentryCandidates
        );

        System.out.printf(
                "Delta-v seleccionado: %.3f km/s%n",
                selectedParameters
                        .getDeltaVMagnitudeMps()
                        / MissionParameters.KM
        );

        System.out.printf(
                "Momento de encendido: %.6f horas%n",
                selectedParameters
                        .getIgnitionOffsetSeconds()
                        / MissionParameters.HOUR
        );

        System.out.printf(
                "Altitud lunar estimada: %.3f km%n",
                selectedAltitudeM
                        / MissionParameters.KM
        );

        System.out.printf(
                "Tiempo aproximado de llegada: %.3f horas%n",
                selectedApproachHours
        );

        System.out.printf(
                "Mision completa encontrada: %s%n",
                completeMissionFound
        );

        if (!completeMissionFound) {
            System.out.println(
                    "[WARNING] No se encontro reentrada "
                            + "en la cuadricula actual."
            );
        }

        System.out.println(
                "[SUCCESS] Busqueda automatica completada."
        );

        return selectedParameters;
    }

    /**
     * Ejecuta la propagacion larga y comprueba si el
     * candidato cruza descendentemente los 120 km.
     */
    private static boolean detectsReentry(
            Orbit initialOrbit,
            OneAxisEllipsoid earth,
            TLIParameters candidate
    ) {

        NumericalPropagator propagator =
                NumericalPropagatorFactory
                        .createQuiet(initialOrbit);

        ImpulseManeuver maneuver =
                TLIManeuverFactory.create(
                        initialOrbit,
                        candidate
                );

        propagator.addEventDetector(
                maneuver
        );

        AbsoluteDate ignitionDate =
                TLIManeuverFactory.getIgnitionDate(
                        initialOrbit,
                        candidate
                );

        ReentryTracker reentryTracker =
                new ReentryTracker();

        AltitudeDetector reentryDetector =
                new AltitudeDetector(
                        MissionParameters.EVENT_MAX_CHECK_S,
                        MissionParameters.EVENT_THRESHOLD_S,
                        MissionParameters
                                .REENTRY_INTERFACE_ALTITUDE_M,
                        earth
                ).withHandler(
                        reentryTracker
                );

        propagator.addEventDetector(
                reentryDetector
        );

        propagator.propagate(
                ignitionDate.shiftedBy(
                        MissionParameters
                                .MAX_MISSION_DURATION_S
                )
        );

        return reentryTracker.hasReentry();
    }

    /**
     * Captura la menor distancia entre la nave y la Luna.
     */
    private static final class ClosestApproachTracker
            implements OrekitFixedStepHandler {

        private final AbsoluteDate ignitionDate;
        private final CelestialBody moon;

        private double minimumDistanceM =
                Double.POSITIVE_INFINITY;

        private SpacecraftState closestState;

        private ClosestApproachTracker(
                AbsoluteDate ignitionDate,
                CelestialBody moon
        ) {

            this.ignitionDate = ignitionDate;
            this.moon = moon;
        }

        @Override
        public void handleStep(
                SpacecraftState state
        ) {

            double elapsedSeconds =
                    state.getDate()
                            .durationFrom(ignitionDate);

            if (elapsedSeconds
                    < MissionParameters
                            .MIN_LUNAR_EVENT_DELAY_S) {

                return;
            }

            Vector3D moonPosition =
                    moon.getPVCoordinates(
                            state.getDate(),
                            state.getFrame()
                    ).getPosition();

            double distance =
                    Vector3D.distance(
                            state.getPosition(),
                            moonPosition
                    );

            if (distance < minimumDistanceM) {

                minimumDistanceM = distance;
                closestState = state;
            }
        }

        private boolean hasResult() {
            return closestState != null;
        }

        private double getMinimumDistanceM() {
            return minimumDistanceM;
        }

        private SpacecraftState getClosestState() {
            return closestState;
        }
    }

    /**
     * Registra solamente el cruce descendente
     * de la interfaz de reentrada.
     */
    private static final class ReentryTracker
            implements EventHandler {

        private boolean reentry;

        @Override
        public Action eventOccurred(
                SpacecraftState state,
                EventDetector detector,
                boolean increasing
        ) {

            if (increasing) {
                return Action.CONTINUE;
            }

            reentry = true;
            return Action.STOP;
        }

        private boolean hasReentry() {
            return reentry;
        }
    }
}