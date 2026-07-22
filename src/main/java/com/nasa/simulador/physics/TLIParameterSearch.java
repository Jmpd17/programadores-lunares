package com.nasa.simulador.physics;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.bodies.CelestialBody;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.forces.maneuvers.ImpulseManeuver;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.numerical.NumericalPropagator;
import org.orekit.propagation.sampling.OrekitFixedStepHandler;
import org.orekit.time.AbsoluteDate;

/**
 * Busca automáticamente parámetros TLI que produzcan
 * el menor acercamiento posible a la Luna.
 */
public final class TLIParameterSearch {

    private TLIParameterSearch() {
        // Evita crear instancias.
    }

    /**
     * Prueba diferentes magnitudes de delta-v y épocas
     * de encendido.
     *
     * @return mejores parámetros encontrados
     */
    public static TLIParameters findBest() {

        System.out.println();
        System.out.println(
                "=================================================="
        );
        System.out.println(
                "       BÚSQUEDA AUTOMÁTICA DE TLI"
        );
        System.out.println(
                "=================================================="
        );

        Orbit initialOrbit =
                ParkingOrbitFactory.createDefaultOrbit();

        CelestialBody moon =
                CelestialBodyFactory.getMoon();

        double orbitalPeriodHours =
                initialOrbit.getKeplerianPeriod()
                        / MissionParameters.HOUR;

        TLIParameters bestParameters = null;

        double bestAltitudeM =
                Double.POSITIVE_INFINITY;

        double bestApproachHours =
                Double.NaN;

        int candidateNumber = 0;

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

                ClosestApproachTracker tracker =
                        new ClosestApproachTracker(
                                ignitionDate,
                                moon
                        );

                propagator.getMultiplexer().add(
                        MissionParameters
                                .TLI_SEARCH_SAMPLE_STEP_S,
                        tracker
                );

                propagator.propagate(
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

                if (altitudeM < bestAltitudeM) {

                    bestAltitudeM = altitudeM;

                    bestParameters = candidate;

                    bestApproachHours =
                            tracker.getClosestState()
                                    .getDate()
                                    .durationFrom(ignitionDate)
                                    / MissionParameters.HOUR;

                    System.out.printf(
                            "[MEJOR] Δv %.3f km/s | "
                                    + "encendido %.3f h | "
                                    + "altitud lunar %.3f km | "
                                    + "tiempo %.3f h%n",
                            deltaV,
                            ignitionHours,
                            bestAltitudeM
                                    / MissionParameters.KM,
                            bestApproachHours
                    );
                }
            }
        }

        if (bestParameters == null) {
            throw new IllegalStateException(
                    "La búsqueda no produjo ningún candidato."
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
                "Delta-v seleccionado: %.3f km/s%n",
                bestParameters.getDeltaVMagnitudeMps()
                        / MissionParameters.KM
        );

        System.out.printf(
                "Momento de encendido: %.3f horas%n",
                bestParameters.getIgnitionOffsetSeconds()
                        / MissionParameters.HOUR
        );

        System.out.printf(
                "Altitud lunar estimada: %.3f km%n",
                bestAltitudeM / MissionParameters.KM
        );

        System.out.printf(
                "Tiempo aproximado de llegada: %.3f horas%n",
                bestApproachHours
        );

        System.out.println(
                "[SUCCESS] Búsqueda automática completada."
        );

        return bestParameters;
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
}
