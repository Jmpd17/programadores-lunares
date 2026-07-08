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

public class TLIPrototype {

    private static final double KM = 1000.0;

    private static final double EARTH_RADIUS_KM = 6378.137;
    private static final double MOON_MEAN_DISTANCE_KM = 384400.0;
    private static final double MOON_ORBITAL_PERIOD_DAYS = 27.321661;

    private static final int PROPAGATION_DAYS = 10;
    private static final int STEP_HOURS = 12;

    public static void run() {

        Locale.setDefault(Locale.US);

        System.out.println();
        System.out.println("====================================================");
        System.out.println("        ARTEMIS II - TLI SPIKE PROTOTYPE");
        System.out.println("====================================================");
        System.out.println("Prototype: Trans-Lunar Injection trajectory");
        System.out.println("Mode: Java console / No GUI / Spike validation");
        System.out.println("====================================================");
        System.out.println();

        try {

            Frame inertialFrame = FramesFactory.getEME2000();

            AbsoluteDate initialDate = new AbsoluteDate(
                    2026, 1, 1,
                    0, 0, 0.0,
                    TimeScalesFactory.getUTC()
            );

            Orbit tliOrbit = createTLIOrbit(initialDate, inertialFrame);

            KeplerianPropagator propagator =
                    new KeplerianPropagator(tliOrbit);

            double apogeeTimeSeconds =
                    tliOrbit.getKeplerianPeriod() / 2.0;

            double moonAngularVelocity =
                    getMoonAngularVelocity();

            double moonInitialPhase =
                    FastMath.PI - moonAngularVelocity * apogeeTimeSeconds;

            inspectTrajectory(
                    propagator,
                    initialDate,
                    moonInitialPhase,
                    moonAngularVelocity
            );

        } catch (Exception e) {
            System.out.println("[ERROR] The TLI Spike failed.");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    private static Orbit createTLIOrbit(
            AbsoluteDate initialDate,
            Frame inertialFrame
    ) {

        /*
         * This trajectory is a conceptual TLI Spike.
         *
         * The spacecraft starts close to Earth and follows a highly
         * elliptical Earth-centered trajectory. The apogee is placed near
         * the lunar region to allow a lunar flyby inspection.
         *
         * This is not a production-grade mission design.
         * It is a proof of concept for the course deliverable.
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

        double raan =
                FastMath.toRadians(0.0);

        double argumentOfPerigee =
                FastMath.toRadians(0.0);

        double trueAnomaly =
                FastMath.toRadians(0.0);

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

        boolean spacecraftMovedAwayFromEarth = false;
        boolean spacecraftApproachedMoon = false;
        boolean spacecraftReturnedTowardEarth = false;

        System.out.println("Initial date: " + initialDate);
        System.out.println("Propagation time: " + PROPAGATION_DAYS + " days");
        System.out.println();

        System.out.println("+----------+----------------------+----------------------+");
        System.out.println("| Time (h) | Earth distance (km)  | Moon distance (km)   |");
        System.out.println("+----------+----------------------+----------------------+");

        double previousEarthDistanceKm = 0.0;

        int totalHours = PROPAGATION_DAYS * 24;

        for (int hour = 0; hour <= totalHours; hour += STEP_HOURS) {

            double seconds = hour * 3600.0;

            AbsoluteDate currentDate =
                    initialDate.shiftedBy(seconds);

            SpacecraftState state =
                    propagator.propagate(currentDate);

            Vector3D spacecraftPosition =
                    state.getPVCoordinates().getPosition();

            Vector3D moonPosition =
                    calculateApproximateMoonPosition(
                            seconds,
                            moonInitialPhase,
                            moonAngularVelocity
                    );

            double earthDistanceKm =
                    spacecraftPosition.getNorm() / KM;

            double moonDistanceKm =
                    spacecraftPosition.subtract(moonPosition).getNorm() / KM;

            if (earthDistanceKm > maximumEarthDistanceKm) {
                maximumEarthDistanceKm = earthDistanceKm;
            }

            if (moonDistanceKm < minimumMoonDistanceKm) {
                minimumMoonDistanceKm = moonDistanceKm;
                closestApproachHour = hour;
            }

            if (earthDistanceKm > 100000.0) {
                spacecraftMovedAwayFromEarth = true;
            }

            if (moonDistanceKm < 50000.0) {
                spacecraftApproachedMoon = true;
            }

            if (hour > 0 && previousEarthDistanceKm > earthDistanceKm
                    && spacecraftMovedAwayFromEarth) {
                spacecraftReturnedTowardEarth = true;
            }

            previousEarthDistanceKm = earthDistanceKm;
            finalEarthDistanceKm = earthDistanceKm;

            System.out.printf(
                    "| %8d | %20.2f | %20.2f |%n",
                    hour,
                    earthDistanceKm,
                    moonDistanceKm
            );
        }

        System.out.println("+----------+----------------------+----------------------+");

        printInspectionReport(
                minimumMoonDistanceKm,
                closestApproachHour,
                maximumEarthDistanceKm,
                finalEarthDistanceKm,
                spacecraftMovedAwayFromEarth,
                spacecraftApproachedMoon,
                spacecraftReturnedTowardEarth
        );
    }

    private static Vector3D calculateApproximateMoonPosition(
            double seconds,
            double initialPhase,
            double angularVelocity
    ) {

        /*
         * Simplified Moon model:
         * The Moon is represented as a circular motion around Earth.
         * This is enough for visual and numeric inspection in this Spike.
         */

        double angle =
                initialPhase + angularVelocity * seconds;

        double x =
                MOON_MEAN_DISTANCE_KM * KM * FastMath.cos(angle);

        double y =
                MOON_MEAN_DISTANCE_KM * KM * FastMath.sin(angle);

        double z =
                0.0;

        return new Vector3D(x, y, z);
    }

    private static double getMoonAngularVelocity() {

        double moonPeriodSeconds =
                MOON_ORBITAL_PERIOD_DAYS * 24.0 * 3600.0;

        return 2.0 * FastMath.PI / moonPeriodSeconds;
    }

    private static void printInspectionReport(
            double minimumMoonDistanceKm,
            double closestApproachHour,
            double maximumEarthDistanceKm,
            double finalEarthDistanceKm,
            boolean spacecraftMovedAwayFromEarth,
            boolean spacecraftApproachedMoon,
            boolean spacecraftReturnedTowardEarth
    ) {

        System.out.println();
        System.out.println("====================================================");
        System.out.println("              INSPECTION REPORT");
        System.out.println("====================================================");

        System.out.printf(
                "Closest lunar approach: %.2f km%n",
                minimumMoonDistanceKm
        );

        System.out.printf(
                "Time of closest approach: %.2f hours%n",
                closestApproachHour
        );

        System.out.printf(
                "Maximum Earth distance: %.2f km%n",
                maximumEarthDistanceKm
        );

        System.out.printf(
                "Final Earth distance: %.2f km%n",
                finalEarthDistanceKm
        );

        System.out.println();
        System.out.println("Free-return geometry inspection:");

        if (spacecraftMovedAwayFromEarth) {
            System.out.println("[OK] The spacecraft leaves the Earth region.");
        } else {
            System.out.println("[WARN] Earth departure is not clearly visible.");
        }

        if (spacecraftApproachedMoon) {
            System.out.println("[OK] The trajectory reaches the lunar flyby region.");
        } else {
            System.out.println("[WARN] Lunar flyby is not close enough.");
        }

        if (spacecraftReturnedTowardEarth) {
            System.out.println("[OK] The spacecraft shows a return trend toward Earth.");
        } else {
            System.out.println("[WARN] Return trend is not clearly visible.");
        }

        System.out.println();

        if (spacecraftMovedAwayFromEarth
                && spacecraftApproachedMoon
                && spacecraftReturnedTowardEarth) {

            System.out.println("[RESULT] Free-return geometry is visible by inspection.");

        } else {

            System.out.println("[RESULT] Free-return geometry requires adjustment.");
        }

        System.out.println("====================================================");
        System.out.println("[SUCCESS] TLI Spike prototype completed.");
    }
}