package com.nasa.simulador.physics;

import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.frames.FramesFactory;
import org.orekit.utils.IERSConventions;

/**
 * Construye el modelo geométrico terrestre utilizado
 * por la telemetría y los detectores de eventos.
 */
public final class EarthModelFactory {

    private EarthModelFactory() {
        // Evita crear instancias.
    }

    /**
     * Crea un elipsoide terrestre WGS84.
     *
     * @return modelo terrestre WGS84
     */
    public static OneAxisEllipsoid createWgs84Earth() {

        return new OneAxisEllipsoid(
                MissionParameters.EARTH_RADIUS_M,
                MissionParameters.EARTH_FLATTENING,
                FramesFactory.getITRF(
                        IERSConventions.IERS_2010,
                        false
                )
        );
    }
}
