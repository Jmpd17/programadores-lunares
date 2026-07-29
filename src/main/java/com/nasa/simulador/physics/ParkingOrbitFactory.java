package com.nasa.simulador.physics;

import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.orbits.CircularOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.PositionAngleType;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;

/**
 * Construye la órbita circular de estacionamiento de la misión.
 */
public final class ParkingOrbitFactory {

    private ParkingOrbitFactory() {
        // Evita crear instancias.
    }

    /**
     * Crea la órbita predeterminada de 185 km.
     *
     * @return órbita inicial predeterminada
     */
    public static Orbit createDefaultOrbit() {
        return createOrbit(
                MissionParameters.PARKING_ALTITUDE_M
                        / MissionParameters.KM
        );
    }

    /**
     * Crea una órbita circular con altitud configurable.
     *
     * @param altitudeKm altitud sobre la Tierra en kilómetros
     * @return órbita inicial de estacionamiento
     */
    public static Orbit createOrbit(double altitudeKm) {

        if (!Double.isFinite(altitudeKm)
                || altitudeKm <= 120.0) {

            throw new IllegalArgumentException(
                    "La altitud inicial debe ser mayor de 120 km."
            );
        }

        Frame inertialFrame =
                FramesFactory.getGCRF();

        AbsoluteDate initialDate =
                new AbsoluteDate(
                        2026,
                        1,
                        1,
                        0,
                        0,
                        0.0,
                        TimeScalesFactory.getUTC()
                );

        double semiMajorAxis =
                MissionParameters.EARTH_RADIUS_M
                        + altitudeKm * MissionParameters.KM;

        return new CircularOrbit(
                semiMajorAxis,
                MissionParameters.PARKING_EXCENTRICITY_X,
                MissionParameters.PARKING_EXCENTRICITY_Y,
                MissionParameters.PARKING_INCLINATION_RAD,
                0.0,
                0.0,
                PositionAngleType.TRUE,
                inertialFrame,
                initialDate,
                MissionParameters.EARTH_MU
        );
    }
}
