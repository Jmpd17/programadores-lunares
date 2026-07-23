package com.nasa.simulador.physics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.nasa.simulador.OrekitConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.SpacecraftState;

/**
 * Verifica el modelo de datos físicos consumido por la interfaz.
 */
class TrajectoryPointTest {

    @BeforeAll
    static void initializeOrekit() {
        OrekitConfig.init();
    }

    @Test
    void transformaUnEstadoEnTelemetriaFisica() {

        Orbit orbit = ParkingOrbitFactory.createDefaultOrbit();

        SpacecraftState state =
                new SpacecraftState(orbit)
                        .withMass(26000.0);

        TrajectoryPoint point =
                TrajectoryPoint.fromState(
                        state,
                        orbit.getDate(),
                        EarthModelFactory.createWgs84Earth(),
                        CelestialBodyFactory.getMoon()
                );

        assertEquals(0.0, point.getElapsedSeconds(), 1.0e-9);

        assertEquals(
                state.getVelocity().getNorm(),
                point.getSpeedMps(),
                1.0e-9
        );

        assertEquals(26000.0, point.getMassKg(), 1.0e-9);
        assertTrue(point.getMoonDistanceM() > 0.0);
        assertTrue(Double.isFinite(point.getEarthAltitudeM()));
    }
}
