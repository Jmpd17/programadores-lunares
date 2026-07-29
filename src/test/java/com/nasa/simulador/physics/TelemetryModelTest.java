package com.nasa.simulador.physics;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.nasa.simulador.OrekitConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.SpacecraftState;

/**
 * Verifica UI-2: datos de telemetría consumidos por la interfaz.
 */
class TelemetryModelTest {

    @BeforeAll
    static void initializeOrekit() {
        OrekitConfig.init();
    }

    @Test
    void ui2ConvierteEstadoOrbitalEnTelemetriaValida() {

        Orbit orbit = ParkingOrbitFactory.createDefaultOrbit();

        SpacecraftState state =
                new SpacecraftState(orbit)
                        .withMass(
                                MissionParameters
                                        .INITIAL_SPACECRAFT_MASS_KG
                        );

        TrajectoryPoint telemetry =
                TrajectoryPoint.fromState(
                        state,
                        orbit.getDate(),
                        EarthModelFactory.createWgs84Earth(),
                        CelestialBodyFactory.getMoon()
                );

        assertNotNull(telemetry);

        assertAll(
                () -> assertEquals(
                        0.0,
                        telemetry.getElapsedSeconds(),
                        1.0e-9,
                        "El primer punto debe iniciar en cero segundos."
                ),
                () -> assertEquals(
                        MissionParameters.PARKING_ALTITUDE_M,
                        telemetry.getEarthAltitudeM(),
                        2_000.0,
                        "La altitud debe ser cercana a 185 km."
                ),
                () -> assertEquals(
                        state.getVelocity().getNorm(),
                        telemetry.getSpeedMps(),
                        1.0e-9,
                        "La velocidad mostrada debe coincidir con el estado."
                ),
                () -> assertEquals(
                        MissionParameters.INITIAL_SPACECRAFT_MASS_KG,
                        telemetry.getMassKg(),
                        1.0e-9
                ),
                () -> assertTrue(
                        telemetry.getMoonDistanceM() > 0.0,
                        "La distancia a la Luna debe ser positiva."
                ),
                () -> assertTrue(
                        Double.isFinite(telemetry.getEarthAltitudeM())
                                && Double.isFinite(telemetry.getSpeedMps())
                                && Double.isFinite(telemetry.getMoonDistanceM()),
                        "La telemetría debe contener valores finitos."
                )
        );
    }

    @Test
    void ui2LaTelemetriaDebeActualizarElTiempo() {

        Orbit orbit = ParkingOrbitFactory.createDefaultOrbit();

        SpacecraftState laterState =
                new SpacecraftState(
                        orbit.shiftedBy(600.0)
                );

        TrajectoryPoint telemetry =
                TrajectoryPoint.fromState(
                        laterState,
                        orbit.getDate(),
                        EarthModelFactory.createWgs84Earth(),
                        CelestialBodyFactory.getMoon()
                );

        assertEquals(
                600.0,
                telemetry.getElapsedSeconds(),
                1.0e-6,
                "El tiempo de telemetría debe avanzar con la misión."
        );
    }
}
