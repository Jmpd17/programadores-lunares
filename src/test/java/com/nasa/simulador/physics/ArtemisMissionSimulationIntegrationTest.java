package com.nasa.simulador.physics;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
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
    void canalizacionCompletaProduceTrayectoriaValida() {

        MissionSimulationResult result =
                ArtemisMissionSimulation.run(
                        TLIParameters.createDefault()
                );

        List<TrajectoryPoint> trajectory =
                result.getTrajectory();

        assertAll(
                () -> assertNotNull(result.getFinalState()),
                () -> assertNotNull(result.getEvents()),
                () -> assertTrue(
                        trajectory.size()
                                >= MissionParameters
                                .MIN_TRAJECTORY_POINTS
                ),
                () -> assertTrue(
                        trajectory.stream()
                                .allMatch(
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
                                )
                )
        );

        for (int index = 1; index < trajectory.size(); index++) {

            assertTrue(
                    trajectory.get(index).getDate().isAfter(
                            trajectory.get(index - 1).getDate()
                    ),
                    "Las marcas temporales deben ser crecientes."
            );
        }

        double maximumEarthDistanceM =
                trajectory.stream()
                        .map(TrajectoryPoint::getPositionM)
                        .mapToDouble(position -> position.getNorm())
                        .max()
                        .orElseThrow();

        assertTrue(
                maximumEarthDistanceM
                        > 300000.0 * MissionParameters.KM,
                "La nave debe abandonar la vecindad terrestre."
        );
    }
}
