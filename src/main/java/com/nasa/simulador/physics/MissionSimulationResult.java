package com.nasa.simulador.physics;

import java.util.List;

import org.orekit.propagation.SpacecraftState;

/**
 * Contiene el resultado completo de una simulación.
 */
public final class MissionSimulationResult {

    private final List<TrajectoryPoint> trajectory;
    private final SpacecraftState finalState;
    private final MissionEvents events;

    /**
     * Construye el resultado de la misión.
     *
     * @param trajectory puntos de trayectoria
     * @param finalState último estado propagado
     * @param events eventos detectados
     */
    public MissionSimulationResult(
            List<TrajectoryPoint> trajectory,
            SpacecraftState finalState,
            MissionEvents events
    ) {

        this.trajectory = List.copyOf(trajectory);
        this.finalState = finalState;
        this.events = events;
    }

    public List<TrajectoryPoint> getTrajectory() {
        return trajectory;
    }

    public SpacecraftState getFinalState() {
        return finalState;
    }

    public MissionEvents getEvents() {
        return events;
    }

    public boolean isReentryDetected() {
        return events.hasReentry();
    }
}
