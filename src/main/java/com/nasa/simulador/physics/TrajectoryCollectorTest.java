package com.nasa.simulador.physics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.nasa.simulador.OrekitConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.SpacecraftState;
import org.orekit.time.AbsoluteDate;

/**
 * Verifica OAM-5: cantidad mínima y continuidad de la trayectoria.
 */
class TrajectoryCollectorTest {

    @BeforeAll
    static void initializeOrekit() {
        OrekitConfig.init();
    }

    @Test
    void oam5RecolectaMasDe500PuntosConTiempoCreciente() {

        Orbit initialOrbit =
                ParkingOrbitFactory.createDefaultOrbit();

        AbsoluteDate startDate =
                initialOrbit.getDate();

        OneAxisEllipsoid earth =
                EarthModelFactory.createWgs84Earth();

        TrajectoryCollector collector =
                new TrajectoryCollector(
                        startDate,
                        earth,
                        CelestialBodyFactory.getMoon()
                );

        SpacecraftState initialState =
                new SpacecraftState(initialOrbit);

        collector.init(
                initialState,
                startDate.shiftedBy(
                        500.0
                                * MissionParameters
                                .TRAJECTORY_SAMPLE_STEP_S
                ),
                MissionParameters.TRAJECTORY_SAMPLE_STEP_S
        );

        for (int index = 0; index <= 500; index++) {

            double elapsed =
                    index
                            * MissionParameters
                            .TRAJECTORY_SAMPLE_STEP_S;

            collector.handleStep(
                    new SpacecraftState(
                            initialOrbit.shiftedBy(elapsed)
                    )
            );
        }

        List<TrajectoryPoint> points =
                collector.getPoints();

        assertTrue(
                points.size()
                        >= MissionParameters.MIN_TRAJECTORY_POINTS
        );

        assertEquals(501, points.size());

        for (int index = 1; index < points.size(); index++) {

            assertTrue(
                    points.get(index).getDate().isAfter(
                            points.get(index - 1).getDate()
                    ),
                    "Las fechas deben ser estrictamente crecientes."
            );

            assertTrue(
                    Double.isFinite(
                            points.get(index).getEarthAltitudeM()
                    )
            );

            assertTrue(
                    points.get(index).getMoonDistanceM() > 0.0
            );
        }
    }
}
