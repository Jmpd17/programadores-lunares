package com.nasa.simulador.physics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.nasa.simulador.OrekitConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.orekit.orbits.Orbit;

/**
 * Verifica OAM-2: órbita circular de estacionamiento a 185 km.
 */
class ParkingOrbitFactoryTest {

    @BeforeAll
    static void initializeOrekit() {
        OrekitConfig.init();
    }

    @Test
    void oam2CreaOrbitaCircularDe185Km() {

        Orbit orbit = ParkingOrbitFactory.createDefaultOrbit();

        double altitudeM =
                orbit.getA() - MissionParameters.EARTH_RADIUS_M;

        assertEquals(
                MissionParameters.PARKING_ALTITUDE_M,
                altitudeM,
                1.0,
                "La altitud debe ser aproximadamente 185 km."
        );

        assertEquals(
                0.0,
                orbit.getE(),
                1.0e-12,
                "La órbita de estacionamiento debe ser circular."
        );

        assertEquals(
                MissionParameters.PARKING_INCLINATION_RAD,
                orbit.getI(),
                1.0e-12,
                "La inclinación debe coincidir con la configuración."
        );
    }

    @Test
    void creaOrbitaConAltitudConfigurable() {

        Orbit orbit = ParkingOrbitFactory.createOrbit(200.0);

        double altitudeKm =
                (orbit.getA() - MissionParameters.EARTH_RADIUS_M)
                        / MissionParameters.KM;

        assertEquals(200.0, altitudeKm, 0.001);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-10.0, 0.0, 100.0, 120.0})
    void rechazaAltitudesNoValidas(double altitudeKm) {

        assertThrows(
                IllegalArgumentException.class,
                () -> ParkingOrbitFactory.createOrbit(altitudeKm)
        );
    }
}
