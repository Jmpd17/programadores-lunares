package com.nasa.simulador.physics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.orekit.propagation.SpacecraftState;

/**
 * Verifica el contrato del resultado consumido por otros módulos.
 */
class MissionSimulationResultTest {

    @Test
    void conservaElResultadoYProtegeLaListaDeTrayectoria() {

        TrajectoryPoint point = mock(TrajectoryPoint.class);
        SpacecraftState finalState = mock(SpacecraftState.class);
        MissionEvents events = mock(MissionEvents.class);

        when(events.hasReentry()).thenReturn(true);

        List<TrajectoryPoint> mutableTrajectory =
                new ArrayList<>(List.of(point));

        MissionSimulationResult result =
                new MissionSimulationResult(
                        mutableTrajectory,
                        finalState,
                        events
                );

        mutableTrajectory.clear();

        assertEquals(1, result.getTrajectory().size());
        assertSame(finalState, result.getFinalState());
        assertSame(events, result.getEvents());
        assertTrue(result.isReentryDetected());

        assertThrows(
                UnsupportedOperationException.class,
                () -> result.getTrajectory().clear()
        );
    }
}
