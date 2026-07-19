package com.nasa.simulador.physics;

import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.orbits.CircularOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.PositionAngleType;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;

/**
 * Construye el estado orbital inicial de la misión.
 */
public final class ParkingOrbitFactory {

    private ParkingOrbitFactory() {
        // Evita la creación de instancias.
    }

    /**
     * Crea una órbita circular de estacionamiento a 185 km.
     *
     * @return órbita inicial de la misión
     */
    public static Orbit createDefaultOrbit() {

        Frame inertialFrame = FramesFactory.getGCRF();

        AbsoluteDate initialDate = new AbsoluteDate(
                2026,
                1,
                1,
                0,
                0,
                0.0,
                TimeScalesFactory.getUTC()
        );

        /*
         * En una órbita circular, el semieje mayor equivale
         * al radio terrestre más la altitud orbital.
         */
        double semiMajorAxis =
                MissionParameters.EARTH_RADIUS_M
                        + MissionParameters.PARKING_ALTITUDE_M;

        double rightAscensionAscendingNode = 0.0;
        double latitudeArgument = 0.0;

        return new CircularOrbit(
                semiMajorAxis,
                MissionParameters.PARKING_EXCENTRICITY_X,
                MissionParameters.PARKING_EXCENTRICITY_Y,
                MissionParameters.PARKING_INCLINATION_RAD,
                rightAscensionAscendingNode,
                latitudeArgument,
                PositionAngleType.TRUE,
                inertialFrame,
                initialDate,
                MissionParameters.EARTH_MU
        );
    }
}
