package com.nasa.simulador.physics;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.bodies.CelestialBody;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.propagation.SpacecraftState;
import org.orekit.time.AbsoluteDate;

/**
 * Representa una muestra de la trayectoria de la nave.
 *
 * <p>Contiene los datos necesarios para representar
 * la posición de la nave y actualizar la telemetría
 * de la interfaz JavaFX.</p>
 */
public final class TrajectoryPoint {

    private final AbsoluteDate date;
    private final double elapsedSeconds;
    private final Vector3D positionM;
    private final Vector3D velocityMps;
    private final double earthAltitudeM;
    private final double moonDistanceM;
    private final double speedMps;
    private final double massKg;

    private TrajectoryPoint(
            AbsoluteDate date,
            double elapsedSeconds,
            Vector3D positionM,
            Vector3D velocityMps,
            double earthAltitudeM,
            double moonDistanceM,
            double speedMps,
            double massKg
    ) {

        this.date = date;
        this.elapsedSeconds = elapsedSeconds;
        this.positionM = positionM;
        this.velocityMps = velocityMps;
        this.earthAltitudeM = earthAltitudeM;
        this.moonDistanceM = moonDistanceM;
        this.speedMps = speedMps;
        this.massKg = massKg;
    }

    /**
     * Convierte un estado de Orekit en un punto de trayectoria.
     *
     * @param state estado actual de la nave
     * @param startDate fecha de referencia de la trayectoria
     * @param earth modelo geométrico terrestre
     * @param moon cuerpo celeste lunar
     * @return punto de trayectoria
     */
    public static TrajectoryPoint fromState(
            SpacecraftState state,
            AbsoluteDate startDate,
            OneAxisEllipsoid earth,
            CelestialBody moon
    ) {

        Vector3D position = state.getPosition();
        Vector3D velocity = state.getVelocity();

        GeodeticPoint geodeticPoint =
                earth.transform(
                        position,
                        state.getFrame(),
                        state.getDate()
                );

        Vector3D moonPosition =
                moon.getPVCoordinates(
                        state.getDate(),
                        state.getFrame()
                ).getPosition();

        double moonDistance =
                Vector3D.distance(
                        position,
                        moonPosition
                );

        return new TrajectoryPoint(
                state.getDate(),
                state.getDate().durationFrom(startDate),
                position,
                velocity,
                geodeticPoint.getAltitude(),
                moonDistance,
                velocity.getNorm(),
                state.getMass()
        );
    }

    public AbsoluteDate getDate() {
        return date;
    }

    public double getElapsedSeconds() {
        return elapsedSeconds;
    }

    public Vector3D getPositionM() {
        return positionM;
    }

    public Vector3D getVelocityMps() {
        return velocityMps;
    }

    public double getEarthAltitudeM() {
        return earthAltitudeM;
    }

    public double getMoonDistanceM() {
        return moonDistanceM;
    }

    public double getSpeedMps() {
        return speedMps;
    }

    public double getMassKg() {
        return massKg;
    }
}
