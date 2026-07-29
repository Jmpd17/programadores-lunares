package com.nasa.simulador.physics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import com.nasa.simulador.OrekitConfig;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.util.FastMath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.orekit.bodies.CelestialBody;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.PositionAngleType;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.numerical.NumericalPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.TimeStampedPVCoordinates;

/**
 * Verifica OAM-6 y OAM-7 mediante escenarios controlados.
 */
class MissionEventsTest {

    @BeforeAll
    static void initializeOrekit() {
        OrekitConfig.init();
    }

    @Test
    void configuraLosDosDetectoresDeMision() {

        Orbit orbit = ParkingOrbitFactory.createDefaultOrbit();

        NumericalPropagator propagator =
                NumericalPropagatorFactory.createQuiet(orbit);

        MissionEvents events =
                new MissionEvents(orbit.getDate());

        events.attachTo(
                propagator,
                EarthModelFactory.createWgs84Earth()
        );

        assertEquals(2, propagator.getEventDetectors().size());
    }

    @Test
    void oam6RegistraYClasificaUnPeriapsisLunarValido()
            throws Exception {

        AbsoluteDate ignitionDate = new AbsoluteDate(
                2026,
                1,
                1,
                0,
                0,
                0.0,
                TimeScalesFactory.getUTC()
        );

        AbsoluteDate approachDate =
                ignitionDate.shiftedBy(72.0 * MissionParameters.HOUR);

        Frame frame = FramesFactory.getGCRF();

        SpacecraftState spacecraftState =
                mock(SpacecraftState.class);

        CelestialBody moon =
                mock(CelestialBody.class);

        Vector3D moonPosition = new Vector3D(
                MissionParameters.MOON_RADIUS_M
                        + 5000.0 * MissionParameters.KM,
                0.0,
                0.0
        );

        when(spacecraftState.getDate())
                .thenReturn(approachDate);

        when(spacecraftState.getFrame())
                .thenReturn(frame);

        when(spacecraftState.getPosition())
                .thenReturn(Vector3D.ZERO);

        when(moon.getPVCoordinates(approachDate, frame))
                .thenReturn(
                        new TimeStampedPVCoordinates(
                                approachDate,
                                moonPosition,
                                Vector3D.ZERO,
                                Vector3D.ZERO
                        )
                );

        MissionEvents events =
                new MissionEvents(ignitionDate);

        Method recordMethod =
                MissionEvents.class.getDeclaredMethod(
                        "recordLunarApproach",
                        SpacecraftState.class,
                        CelestialBody.class
                );

        recordMethod.setAccessible(true);
        recordMethod.invoke(events, spacecraftState, moon);

        assertTrue(events.hasLunarApproachCandidate());
        assertTrue(events.hasLunarPeriapsis());

        assertEquals(
                5000.0 * MissionParameters.KM,
                events.getLunarPeriapsisAltitudeM(),
                1.0e-6
        );
    }

    @Test
    void ignoraFalsoPeriapsisAntesDe24Horas()
            throws Exception {

        AbsoluteDate ignitionDate = new AbsoluteDate(
                2026,
                1,
                1,
                0,
                0,
                0.0,
                TimeScalesFactory.getUTC()
        );

        AbsoluteDate earlyDate =
                ignitionDate.shiftedBy(2.0 * MissionParameters.HOUR);

        Frame frame = FramesFactory.getGCRF();

        SpacecraftState state = mock(SpacecraftState.class);
        CelestialBody moon = mock(CelestialBody.class);

        when(state.getDate()).thenReturn(earlyDate);
        when(state.getFrame()).thenReturn(frame);
        when(state.getPosition()).thenReturn(Vector3D.ZERO);

        when(moon.getPVCoordinates(earlyDate, frame))
                .thenReturn(
                        new TimeStampedPVCoordinates(
                                earlyDate,
                                new Vector3D(
                                        MissionParameters.MOON_RADIUS_M
                                                + 1000.0
                                                * MissionParameters.KM,
                                        0.0,
                                        0.0
                                ),
                                Vector3D.ZERO,
                                Vector3D.ZERO
                        )
                );

        MissionEvents events = new MissionEvents(ignitionDate);

        Method recordMethod =
                MissionEvents.class.getDeclaredMethod(
                        "recordLunarApproach",
                        SpacecraftState.class,
                        CelestialBody.class
                );

        recordMethod.setAccessible(true);
        recordMethod.invoke(events, state, moon);

        assertFalse(events.hasLunarApproachCandidate());
    }

    @Test
    void oam7DetectaCruceDescendenteDe120Km() {

        Frame frame = FramesFactory.getGCRF();

        AbsoluteDate date = new AbsoluteDate(
                2026,
                1,
                1,
                0,
                0,
                0.0,
                TimeScalesFactory.getUTC()
        );

        double perigeeRadius =
                MissionParameters.EARTH_RADIUS_M
                        + 100.0 * MissionParameters.KM;

        double apogeeRadius =
                MissionParameters.EARTH_RADIUS_M
                        + 185.0 * MissionParameters.KM;

        double semiMajorAxis =
                0.5 * (perigeeRadius + apogeeRadius);

        double eccentricity =
                (apogeeRadius - perigeeRadius)
                        / (apogeeRadius + perigeeRadius);

        Orbit reentryOrbit = new KeplerianOrbit(
                semiMajorAxis,
                eccentricity,
                MissionParameters.PARKING_INCLINATION_RAD,
                0.0,
                0.0,
                FastMath.PI,
                PositionAngleType.TRUE,
                frame,
                date,
                MissionParameters.EARTH_MU
        );

        NumericalPropagator propagator =
                NumericalPropagatorFactory.createQuiet(reentryOrbit);

        OneAxisEllipsoid earth =
                EarthModelFactory.createWgs84Earth();

        MissionEvents events = new MissionEvents(date);
        events.attachTo(propagator, earth);

        propagator.propagate(
                date.shiftedBy(reentryOrbit.getKeplerianPeriod())
        );

        assertTrue(events.hasReentry());
        assertNotNull(events.getReentryState());

        GeodeticPoint reentryPoint =
                earth.transform(
                        events.getReentryState().getPosition(),
                        events.getReentryState().getFrame(),
                        events.getReentryState().getDate()
                );

        assertEquals(
                MissionParameters.REENTRY_INTERFACE_ALTITUDE_M,
                reentryPoint.getAltitude(),
                2000.0
        );
    }
}
