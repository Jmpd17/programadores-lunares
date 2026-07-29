package com.nasa.simulador.physics;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.bodies.CelestialBody;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.propagation.SpacecraftState;
import org.orekit.time.AbsoluteDate;

/**
 * Representa una muestra completa de la trayectoria.
 *
 * <p>Contiene los datos que consume la interfaz JavaFX para
 * dibujar la nave, mover la Luna y actualizar la telemetría.</p>
 */
public final class TrajectoryPoint {

    private final AbsoluteDate date;
    private final double elapsedSeconds;
    private final Vector3D positionM;
    private final Vector3D velocityMps;
    private final Vector3D moonPositionM;
    private final double earthAltitudeM;
    private final double moonDistanceM;
    private final double speedMps;
    private final double massKg;

    private TrajectoryPoint(
            AbsoluteDate date,
            double elapsedSeconds,
            Vector3D positionM,
            Vector3D velocityMps,
            Vector3D moonPositionM,
            double earthAltitudeM,
            double moonDistanceM,
            double speedMps,
            double massKg
    ) {

        this.date = date;
        this.elapsedSeconds = elapsedSeconds;
        this.positionM = positionM;
        this.velocityMps = velocityMps;
        this.moonPositionM = moonPositionM;
        this.earthAltitudeM = earthAltitudeM;
        this.moonDistanceM = moonDistanceM;
        this.speedMps = speedMps;
        this.massKg = massKg;
    }

    /**
     * Convierte un estado de Orekit en un punto de trayectoria.
     *
     * @param state estado actual de la nave
     * @param startDate fecha inicial de la trayectoria
     * @param earth modelo geométrico terrestre
     * @param moon cuerpo celeste lunar
     * @return punto de trayectoria para la interfaz
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
                moonPosition,
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

    public Vector3D getMoonPositionM() {
        return moonPositionM;
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
