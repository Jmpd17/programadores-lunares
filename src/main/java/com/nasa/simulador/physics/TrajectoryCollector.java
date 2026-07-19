package com.nasa.simulador.physics;

import java.util.ArrayList;
import java.util.List;

import org.orekit.bodies.CelestialBody;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.sampling.OrekitFixedStepHandler;
import org.orekit.time.AbsoluteDate;

/**
 * Captura la trayectoria de la nave en intervalos regulares.
 */
public final class TrajectoryCollector
        implements OrekitFixedStepHandler {

    private static final double DATE_TOLERANCE_S = 1.0e-6;

    private final AbsoluteDate trajectoryStartDate;
    private final OneAxisEllipsoid earth;
    private final CelestialBody moon;
    private final List<TrajectoryPoint> points;

    /**
     * Construye el recolector de trayectoria.
     *
     * @param trajectoryStartDate fecha de inicio de la TLI
     * @param earth modelo terrestre
     * @param moon cuerpo celeste lunar
     */
    public TrajectoryCollector(
            AbsoluteDate trajectoryStartDate,
            OneAxisEllipsoid earth,
            CelestialBody moon
    ) {

        this.trajectoryStartDate = trajectoryStartDate;
        this.earth = earth;
        this.moon = moon;
        this.points = new ArrayList<>();
    }

    /**
     * Limpia los datos antes de iniciar una propagación.
     */
    @Override
    public void init(
            SpacecraftState initialState,
            AbsoluteDate targetDate,
            double step
    ) {

        points.clear();
    }

    /**
     * Recibe los estados interpolados a paso fijo.
     */
    @Override
    public void handleStep(
            SpacecraftState currentState
    ) {

        addStateIfApplicable(currentState);
    }

    /**
     * Agrega también el estado final de la propagación.
     */
    @Override
    public void finish(
            SpacecraftState finalState
    ) {

        addStateIfApplicable(finalState);
    }

    private void addStateIfApplicable(
            SpacecraftState state
    ) {

        double elapsed =
                state.getDate()
                        .durationFrom(trajectoryStartDate);

        if (elapsed < -DATE_TOLERANCE_S) {
            return;
        }

        if (!points.isEmpty()) {

            TrajectoryPoint lastPoint =
                    points.get(points.size() - 1);

            double difference =
                    Math.abs(
                            state.getDate().durationFrom(
                                    lastPoint.getDate()
                            )
                    );

            if (difference < DATE_TOLERANCE_S) {
                return;
            }
        }

        points.add(
                TrajectoryPoint.fromState(
                        state,
                        trajectoryStartDate,
                        earth,
                        moon
                )
        );
    }

    /**
     * Obtiene una copia inmutable de la trayectoria.
     *
     * @return puntos capturados
     */
    public List<TrajectoryPoint> getPoints() {
        return List.copyOf(points);
    }

    /**
     * Obtiene la cantidad de puntos capturados.
     *
     * @return número de puntos
     */
    public int size() {
        return points.size();
    }
}