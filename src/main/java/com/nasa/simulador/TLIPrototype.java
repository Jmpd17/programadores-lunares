package com.nasa.simulador;

import java.util.Locale;

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

public final class TLIPrototype {

    private static final double KM = 1000.0;

    private static final double EARTH_RADIUS_KM = 6378.137;
    private static final double MOON_MEAN_DISTANCE_KM = 384400.0;
    private static final double MOON_ORBITAL_PERIOD_DAYS = 27.321661;

    private static final int PROPAGATION_DAYS = 10;
    private static final int STEP_HOURS = 12;

    private TLIPrototype() {
        // Evita crear objetos de esta clase.
    }

    public static void run() {

        Locale.setDefault(Locale.US);

        System.out.println();
        System.out.println("====================================================");
        System.out.println("       ARTEMIS II - SPIKE DE TRAYECTORIA TLI");
        System.out.println("====================================================");
        System.out.println("Tipo: Trans-Lunar Injection");
        System.out.println("Ejecución: consola Java, sin interfaz gráfica");
        System.out.println("Propósito: prueba de concepto académica");
        System.out.println("====================================================");

        Frame inertialFrame = FramesFactory.getEME2000();

        /*
         * Se utiliza TAI para evitar depender de los archivos
         * de saltos de segundos de UTC mientras no exista orekit-data.
         */
        AbsoluteDate initialDate = new AbsoluteDate(
                2026,
                1,
                1,
                0,
                0,
                0.0,
                TimeScalesFactory.getTAI()
        );

        Orbit tliOrbit =
                createTLIOrbit(initialDate, inertialFrame);

        KeplerianPropagator propagator =
                new KeplerianPropagator(tliOrbit);

        double apogeeTimeSeconds =
                tliOrbit.getKeplerianPeriod() / 2.0;

        double moonAngularVelocity =
                calculateMoonAngularVelocity();

        /*
         * Ajustamos la fase lunar para que la Luna se encuentre
         * cerca del apogeo cuando llegue la nave.
         */
        double moonInitialPhase =
                FastMath.PI
                        - moonAngularVelocity * apogeeTimeSeconds;

        inspectTrajectory(
                propagator,
                initialDate,
                moonInitialPhase,
                moonAngularVelocity
        );
    }

    private static Orbit createTLIOrbit(
            AbsoluteDate initialDate,
            Frame inertialFrame
    ) {

        /*
         * La nave inicia a 300 km de altitud y alcanza un apogeo
         * cercano a la región lunar.
         */

        double perigeeAltitudeKm = 300.0;
        double apogeeDistanceKm = 405000.0;

        double perigeeRadiusMeters =
                (EARTH_RADIUS_KM + perigeeAltitudeKm) * KM;

        double apogeeRadiusMeters =
                apogeeDistanceKm * KM;

        double semiMajorAxis =
                (perigeeRadiusMeters + apogeeRadiusMeters) / 2.0;

        double eccentricity =
                (apogeeRadiusMeters - perigeeRadiusMeters)
                        / (apogeeRadiusMeters + perigeeRadiusMeters);

        double inclination =
                FastMath.toRadians(5.0);

        double argumentOfPerigee = 0.0;
        double raan = 0.0;
        double trueAnomaly = 0.0;

        return new KeplerianOrbit(
                semiMajorAxis,
                eccentricity,
                inclination,
                argumentOfPerigee,
                raan,
                trueAnomaly,
                PositionAngleType.TRUE,
                inertialFrame,
                initialDate,
                Constants.EGM96_EARTH_MU
        );
    }

    private static void inspectTrajectory(
            KeplerianPropagator propagator,
            AbsoluteDate initialDate,
            double moonInitialPhase,
            double moonAngularVelocity
    ) {

        double minimumMoonDistanceKm = Double.MAX_VALUE;
        double closestApproachHour = 0.0;

        double maximumEarthDistanceKm = 0.0;
        double finalEarthDistanceKm = 0.0;
        double previousEarthDistanceKm = 0.0;

        boolean departedEarth = false;
        boolean reachedLunarRegion = false;
        boolean returningToEarth = false;

        int totalHours = PROPAGATION_DAYS * 24;

        System.out.println();
        System.out.println("Fecha inicial: " + initialDate);
        System.out.println(
                "Duración de propagación: "
                        + PROPAGATION_DAYS
                        + " días"
        );

        System.out.println();
        System.out.println(
                "+----------+----------------------+----------------------+"
        );

        System.out.println(
                "| Tiempo   | Distancia Tierra km  | Distancia Luna km    |"
        );

        System.out.println(
                "+----------+----------------------+----------------------+"
        );

        for (int hour = 0;
             hour <= totalHours;
             hour += STEP_HOURS) {

            double seconds = hour * 3600.0;

            AbsoluteDate currentDate =
                    initialDate.shiftedBy(seconds);

            SpacecraftState state =
                    propagator.propagate(currentDate);

            Vector3D spacecraftPosition =
                    state.getPosition();

            Vector3D moonPosition =
                    calculateApproximateMoonPosition(
                            seconds,
                            moonInitialPhase,
                            moonAngularVelocity
                    );

            double earthDistanceKm =
                    spacecraftPosition.getNorm() / KM;

            double moonDistanceKm =
                    spacecraftPosition
                            .subtract(moonPosition)
                            .getNorm() / KM;

            if (earthDistanceKm > maximumEarthDistanceKm) {
                maximumEarthDistanceKm = earthDistanceKm;
            }

            if (moonDistanceKm < minimumMoonDistanceKm) {
                minimumMoonDistanceKm = moonDistanceKm;
                closestApproachHour = hour;
            }

            if (earthDistanceKm > 100000.0) {
                departedEarth = true;
            }

            if (moonDistanceKm < 50000.0) {
                reachedLunarRegion = true;
            }

            if (hour > 0
                    && departedEarth
                    && earthDistanceKm < previousEarthDistanceKm) {

                returningToEarth = true;
            }

            previousEarthDistanceKm = earthDistanceKm;
            finalEarthDistanceKm = earthDistanceKm;

            System.out.printf(
                    "| %6d h | %20.2f | %20.2f |%n",
                    hour,
                    earthDistanceKm,
                    moonDistanceKm
            );
        }

        System.out.println(
                "+----------+----------------------+----------------------+"
        );

        printInspectionReport(
                minimumMoonDistanceKm,
                closestApproachHour,
                maximumEarthDistanceKm,
                finalEarthDistanceKm,
                departedEarth,
                reachedLunarRegion,
                returningToEarth
        );
    }

    private static Vector3D calculateApproximateMoonPosition(
            double seconds,
            double initialPhase,
            double angularVelocity
    ) {

        /*
         * Modelo lunar simplificado:
         * la Luna se representa mediante una órbita circular
         * alrededor de la Tierra.
         */

        double angle =
                initialPhase + angularVelocity * seconds;

        double x =
                MOON_MEAN_DISTANCE_KM
                        * KM
                        * FastMath.cos(angle);

        double y =
                MOON_MEAN_DISTANCE_KM
                        * KM
                        * FastMath.sin(angle);

        return new Vector3D(x, y, 0.0);
    }

    private static double calculateMoonAngularVelocity() {

        double moonPeriodSeconds =
                MOON_ORBITAL_PERIOD_DAYS
                        * 24.0
                        * 3600.0;

        return 2.0 * FastMath.PI / moonPeriodSeconds;
    }

    private static void printInspectionReport(
            double minimumMoonDistanceKm,
            double closestApproachHour,
            double maximumEarthDistanceKm,
            double finalEarthDistanceKm,
            boolean departedEarth,
            boolean reachedLunarRegion,
            boolean returningToEarth
    ) {

        System.out.println();
        System.out.println("====================================================");
        System.out.println("          INFORME DE INSPECCIÓN GEOMÉTRICA");
        System.out.println("====================================================");

        System.out.printf(
                "Mayor acercamiento lunar: %.2f km%n",
                minimumMoonDistanceKm
        );

        System.out.printf(
                "Tiempo del acercamiento: %.2f horas%n",
                closestApproachHour
        );

        System.out.printf(
                "Distancia máxima a la Tierra: %.2f km%n",
                maximumEarthDistanceKm
        );

        System.out.printf(
                "Distancia final a la Tierra: %.2f km%n",
                finalEarthDistanceKm
        );

        System.out.println();
        System.out.println("Verificación mediante inspección:");

        printStatus(
                departedEarth,
                "La nave abandona la región terrestre.",
                "No se detectó claramente la salida terrestre."
        );

        printStatus(
                reachedLunarRegion,
                "La trayectoria alcanza la región de sobrevuelo lunar.",
                "La trayectoria no se acercó lo suficiente a la Luna."
        );

        printStatus(
                returningToEarth,
                "La nave presenta una tendencia de regreso a la Tierra.",
                "No se detectó claramente el regreso hacia la Tierra."
        );

        System.out.println();

        if (departedEarth
                && reachedLunarRegion
                && returningToEarth) {

            System.out.println(
                    "[RESULTADO] Geometría de retorno visible por inspección."
            );

        } else {

            System.out.println(
                    "[RESULTADO] La geometría requiere más ajustes."
            );
        }

        System.out.println("====================================================");
        System.out.println("[SUCCESS] Spike TLI completado.");
    }

    private static void printStatus(
            boolean condition,
            String successMessage,
            String warningMessage
    ) {

        if (condition) {
            System.out.println("[OK] " + successMessage);
        } else {
            System.out.println("[WARNING] " + warningMessage);
        }
    }
}