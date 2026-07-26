package com.nasa.simulador.physics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.nasa.simulador.OrekitConfig;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.orekit.forces.maneuvers.ImpulseManeuver;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.SpacecraftState;

/**
 * Pruebas del requisito OAM-4:
 * configuración de la maniobra impulsiva TLI.
 */
class TLIManeuverFactoryTest {

    /**
     * Inicializa Orekit antes de ejecutar las pruebas.
     */
    @BeforeAll
    static void initializeOrekit() {
        OrekitConfig.init();
    }

    /**
     * Comprueba que la magnitud del impulso configurado
     * coincide con el delta-v solicitado.
     *
     * @param deltaVKmS delta-v de prueba en km/s
     */
    @ParameterizedTest
    @ValueSource(doubles = {
            3.050,
            3.150,
            3.250
    })
    void shouldCreateManeuverWithConfiguredDeltaV(
            double deltaVKmS
    ) {

        Orbit orbit =
                ParkingOrbitFactory.createDefaultOrbit();

        TLIParameters parameters =
                new TLIParameters(
                        deltaVKmS,
                        0.5,
                        1.0,
                        0.0,
                        0.0,
                        MissionParameters.DEFAULT_TLI_ISP_S
                );

        ImpulseManeuver maneuver =
                TLIManeuverFactory.create(
                        orbit,
                        parameters
                );

        assertNotNull(
                maneuver,
                "La maniobra TLI no debe ser nula."
        );

        SpacecraftState state =
                new SpacecraftState(orbit);

        Vector3D impulse =
                maneuver
                        .getImpulseProvider()
                        .getImpulse(
                                state,
                                true
                        );

        assertEquals(
                deltaVKmS * MissionParameters.KM,
                impulse.getNorm(),
                1.0e-9,
                "La magnitud del impulso debe coincidir "
                        + "con el delta-v configurado."
        );

        Vector3D direction =
                impulse.normalize();

        assertEquals(
                1.0,
                direction.getX(),
                1.0e-12,
                "El impulso debe apuntar en la dirección VNC-X."
        );

        assertEquals(
                0.0,
                direction.getY(),
                1.0e-12
        );

        assertEquals(
                0.0,
                direction.getZ(),
                1.0e-12
        );

        assertEquals(
                MissionParameters.DEFAULT_TLI_ISP_S,
                maneuver.getIsp(),
                1.0e-12,
                "El impulso específico debe conservarse."
        );
    }

    /**
     * Comprueba que la fecha del encendido respeta
     * el desplazamiento temporal configurado.
     */
    @ParameterizedTest
    @ValueSource(doubles = {
            0.0,
            0.5,
            1.0,
            2.0
    })
    void shouldCalculateConfiguredIgnitionDate(
            double ignitionHours
    ) {

        Orbit orbit =
                ParkingOrbitFactory.createDefaultOrbit();

        TLIParameters parameters =
                new TLIParameters(
                        MissionParameters
                                .DEFAULT_TLI_DELTA_V_KM_S,
                        ignitionHours,
                        1.0,
                        0.0,
                        0.0,
                        MissionParameters.DEFAULT_TLI_ISP_S
                );

        double actualOffsetSeconds =
                TLIManeuverFactory
                        .getIgnitionDate(
                                orbit,
                                parameters
                        )
                        .durationFrom(
                                orbit.getDate()
                        );

        assertEquals(
                ignitionHours * MissionParameters.HOUR,
                actualOffsetSeconds,
                1.0e-9,
                "La fecha de encendido debe respetar "
                        + "el desplazamiento configurado."
        );
    }
}