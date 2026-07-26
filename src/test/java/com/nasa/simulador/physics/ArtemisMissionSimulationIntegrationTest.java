package com.nasa.simulador.physics;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.nasa.simulador.OrekitConfig;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Prueba de integración de la canalización física completa.
 */
@Tag("integration")
class ArtemisMissionSimulationIntegrationTest {

    @BeforeAll
    static void initializeOrekit() {
        OrekitConfig.init();
    }

    @Test
    @Timeout(300)
    void completeMissionPipelineProducesLunarFlybyAndReentry() {

        double ignitionHours =
                ParkingOrbitFactory.createDefaultOrbit()
                        .getKeplerianPeriod()
                        / MissionParameters.HOUR
                        * 15.0
                        / MissionParameters
                                .TLI_SEARCH_IGNITION_SAMPLES;

        TLIParameters validatedParameters =
                new TLIParameters(
                        3.175,
                        ignitionHours,
                        1.0,
                        0.0,
                        0.0,
                        MissionParameters.DEFAULT_TLI_ISP_S
                );

        MissionSimulationResult result =
                ArtemisMissionSimulation.run(
                        validatedParameters
                );

        assertNotNull(
                result,
                "El resultado de la simulación no debe ser nulo."
        );

        List<TrajectoryPoint> trajectory =
                result.getTrajectory();

        MissionEvents events = result.getEvents();

        assertNotNull(
                trajectory,
                "La trayectoria no debe ser nula."
        );

        assertNotNull(
                events,
                "El registro de eventos no debe ser nulo."
        );

        assertAll(
                () -> assertNotNull(
                        result.getFinalState(),
                        "Debe existir un estado final."
                ),

                () -> assertTrue(
                        trajectory.size()
                                >= MissionParameters.MIN_TRAJECTORY_POINTS,
                        "La trayectoria debe contener al menos 500 puntos."
                ),

                () -> assertTrue(
                        trajectory.stream().allMatch(
                                point ->
                                        Double.isFinite(
                                                point.getEarthAltitudeM()
                                        )
                                                && Double.isFinite(
                                                        point.getMoonDistanceM()
                                                )
                                                && Double.isFinite(
                                                        point.getSpeedMps()
                                                )
                        ),
                        "Los datos físicos deben ser valores finitos."
                ),

                () -> assertTrue(
                        events.hasLunarPeriapsis(),
                        "La misión completa debe detectar "
                                + "un periapsis lunar válido."
                ),

                () -> assertTrue(
                        events.hasReentry(),
                        "La misión completa debe detectar "
                                + "la interfaz de reentrada."
                ),

                () -> assertTrue(
                        result.isReentryDetected(),
                        "El resultado debe informar "
                                + "que la reentrada fue detectada."
                )
        );

        for (int index = 1;
             index < trajectory.size();
             index++) {

            assertTrue(
                    trajectory.get(index)
                            .getDate()
                            .isAfter(
                                    trajectory.get(index - 1)
                                            .getDate()
                            ),
                    "Las marcas temporales deben ser crecientes."
            );
        }
    }
}