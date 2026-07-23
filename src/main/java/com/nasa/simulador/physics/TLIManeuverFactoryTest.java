package com.nasa.simulador.physics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nasa.simulador.OrekitConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.orekit.forces.maneuvers.ImpulseManeuver;
import org.orekit.orbits.Orbit;
import org.orekit.time.AbsoluteDate;

/**
 * Verifica OAM-4: magnitud, dirección y época de la maniobra TLI.
 */
class TLIManeuverFactoryTest {

    @BeforeAll
    static void initializeOrekit() {
        OrekitConfig.init();
    }

    @ParameterizedTest
    @ValueSource(doubles = {3.05, 3.15, 3.25})
    void oam4ConfiguraElDeltaVProporcionalmente(double deltaVKmS) {

        Orbit orbit = ParkingOrbitFactory.createDefaultOrbit();

        TLIParameters parameters = new TLIParameters(
                deltaVKmS,
                0.75,
                1.0,
                0.0,
                0.0,
                450.0
        );

        ImpulseManeuver maneuver =
                TLIManeuverFactory.create(orbit, parameters);

        assertEquals(
                deltaVKmS * MissionParameters.KM,
                maneuver.getDeltaVSat().getNorm(),
                1.0e-9
        );

        assertEquals(
                1.0,
                maneuver.getDeltaVSat().normalize().getX(),
                1.0e-12
        );

        AbsoluteDate ignitionDate =
                TLIManeuverFactory.getIgnitionDate(
                        orbit,
                        parameters
                );

        assertEquals(
                0.75 * MissionParameters.HOUR,
                ignitionDate.durationFrom(orbit.getDate()),
                1.0e-9
        );
    }
}
