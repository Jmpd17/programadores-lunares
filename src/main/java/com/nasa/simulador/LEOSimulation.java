package com.nasa.simulador;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.util.FastMath;

import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;

import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.PositionAngleType;

import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.KeplerianPropagator;

import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;

import org.orekit.utils.Constants;

public final class LEOSimulation {

    private static final double KM = 1000.0;

    private LEOSimulation() {
        // Evita crear objetos de esta clase.
    }

    public static void run() {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("       SIMULACIÓN ORBITAL LEO");
        System.out.println("==========================================");

        Frame frame = FramesFactory.getEME2000();

        /*
         * Usamos TAI para que esta simulación básica pueda ejecutarse
         * aunque todavía no exista la carpeta orekit-data.
         */
        AbsoluteDate initialDate = new AbsoluteDate(
                2026,
                1,
                1,
                12,
                0,
                0.0,
                TimeScalesFactory.getTAI()
        );

        double semiMajorAxis = 7_000_000.0;
        double eccentricity = 0.001;
        double inclination = FastMath.toRadians(98.0);

        Orbit initialOrbit = new KeplerianOrbit(
                semiMajorAxis,
                eccentricity,
                inclination,
                0.0,
                0.0,
                0.0,
                PositionAngleType.TRUE,
                frame,
                initialDate,
                Constants.EGM96_EARTH_MU
        );

        KeplerianPropagator propagator =
                new KeplerianPropagator(initialOrbit);

        System.out.println();
        System.out.println("Tiempo | X (km) | Y (km) | Z (km)");
        System.out.println("---------------------------------------------");

        for (int seconds = 0; seconds <= 600; seconds += 60) {

            AbsoluteDate currentDate =
                    initialDate.shiftedBy(seconds);

            SpacecraftState state =
                    propagator.propagate(currentDate);

            Vector3D position = state.getPosition();

            System.out.printf(
                    "%5d s | %9.2f | %9.2f | %9.2f%n",
                    seconds,
                    position.getX() / KM,
                    position.getY() / KM,
                    position.getZ() / KM
            );
        }

        System.out.println();
        System.out.println("[SUCCESS] Simulación LEO terminada.");
    }
}
