package com.nasa.simulador.physics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.nasa.simulador.OrekitConfig;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.forces.maneuvers.ImpulseManeuver;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.SpacecraftState;

/**
 * Verifica UI-4: configuraciones distintas producen
 * datos de trayectoria distintos para la interfaz.
 */
class TrajectoryChangeTest {

    @BeforeAll
    static void initializeOrekit() {
        OrekitConfig.init();
    }

    @Test
    void ui4CambiarAltitudInicialProduceTrayectoriaDiferente() {

        Orbit orbit185 = ParkingOrbitFactory.createOrbit(185.0);
        Orbit orbit250 = ParkingOrbitFactory.createOrbit(250.0);

        TrajectoryCollector collector185 =
                new TrajectoryCollector(
                        orbit185.getDate(),
                        EarthModelFactory.createWgs84Earth(),
                        CelestialBodyFactory.getMoon()
                );

        TrajectoryCollector collector250 =
                new TrajectoryCollector(
                        orbit250.getDate(),
                        EarthModelFactory.createWgs84Earth(),
                        CelestialBodyFactory.getMoon()
                );

        SpacecraftState state185 =
                new SpacecraftState(orbit185.shiftedBy(1_800.0));

        SpacecraftState state250 =
                new SpacecraftState(orbit250.shiftedBy(1_800.0));

        collector185.handleStep(state185);
        collector250.handleStep(state250);

        TrajectoryPoint point185 = collector185.getPoints().get(0);
        TrajectoryPoint point250 = collector250.getPoints().get(0);

        assertEquals(1, collector185.size());
        assertEquals(1, collector250.size());

        assertNotEquals(
                point185.getEarthAltitudeM(),
                point250.getEarthAltitudeM(),
                1.0,
                "Cambiar la altitud inicial debe cambiar la trayectoria."
        );

        assertTrue(
                Vector3D.distance(
                        point185.getPositionM(),
                        point250.getPositionM()
                ) > 1_000.0,
                "Las posiciones deben ser visualmente diferentes."
        );
    }

    @Test
    void ui4CambiarDeltaVProduceCondicionesDeTrayectoriaDiferentes() {

        Orbit orbit = ParkingOrbitFactory.createDefaultOrbit();
        SpacecraftState state = new SpacecraftState(orbit);

        TLIParameters nominal =
                new TLIParameters(
                        3.15,
                        0.5,
                        1.0,
                        0.0,
                        0.0,
                        MissionParameters.DEFAULT_TLI_ISP_S
                );

        TLIParameters modified =
                new TLIParameters(
                        3.30,
                        0.5,
                        1.0,
                        0.0,
                        0.0,
                        MissionParameters.DEFAULT_TLI_ISP_S
                );

        ImpulseManeuver nominalManeuver =
                TLIManeuverFactory.create(orbit, nominal);

        ImpulseManeuver modifiedManeuver =
                TLIManeuverFactory.create(orbit, modified);

        Vector3D nominalImpulse =
                nominalManeuver
                        .getImpulseProvider()
                        .getImpulse(state, true);

        Vector3D modifiedImpulse =
                modifiedManeuver
                        .getImpulseProvider()
                        .getImpulse(state, true);

        assertNotEquals(
                nominalImpulse.getNorm(),
                modifiedImpulse.getNorm(),
                1.0e-9,
                "Cambiar delta-v debe cambiar el impulso de la trayectoria."
        );

        assertEquals(3_150.0, nominalImpulse.getNorm(), 1.0e-9);
        assertEquals(3_300.0, modifiedImpulse.getNorm(), 1.0e-9);
    }

    @Test
    void ui4CambiarHoraDeEncendidoCambiaLaConfiguracionTemporal() {

        Orbit orbit = ParkingOrbitFactory.createDefaultOrbit();

        TLIParameters early =
                new TLIParameters(
                        3.15,
                        0.5,
                        1.0,
                        0.0,
                        0.0,
                        MissionParameters.DEFAULT_TLI_ISP_S
                );

        TLIParameters late =
                new TLIParameters(
                        3.15,
                        2.0,
                        1.0,
                        0.0,
                        0.0,
                        MissionParameters.DEFAULT_TLI_ISP_S
                );

        double separationSeconds =
                TLIManeuverFactory
                        .getIgnitionDate(orbit, late)
                        .durationFrom(
                                TLIManeuverFactory
                                        .getIgnitionDate(orbit, early)
                        );

        assertEquals(
                1.5 * MissionParameters.HOUR,
                separationSeconds,
                1.0e-9,
                "Cambiar la hora debe desplazar el inicio de la trayectoria."
        );
    }
}
