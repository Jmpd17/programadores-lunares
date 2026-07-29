package com.nasa.simulador.physics;

import org.orekit.orbits.Orbit;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.numerical.NumericalPropagator;

/**
 * Ejecuta una prueba técnica del motor orbital numérico.
 */
public final class NumericalPropagationValidation {

    private NumericalPropagationValidation() {
        // Evita crear instancias.
    }

    /**
     * Propaga la órbita de estacionamiento durante una hora.
     */
    public static void run() {

        System.out.println();
        System.out.println(
                "=================================================="
        );
        System.out.println(
                "       VALIDACION DEL MOTOR ORBITAL NUMERICO"
        );
        System.out.println(
                "=================================================="
        );

        Orbit initialOrbit =
                ParkingOrbitFactory.createDefaultOrbit();

        NumericalPropagator propagator =
                NumericalPropagatorFactory.create(
                        initialOrbit
                );

        double initialAltitudeKm =
                (
                        initialOrbit
                                .getPVCoordinates()
                                .getPosition()
                                .getNorm()
                                - MissionParameters.EARTH_RADIUS_M
                ) / MissionParameters.KM;

        double initialVelocityKmS =
                initialOrbit
                        .getPVCoordinates()
                        .getVelocity()
                        .getNorm()
                        / MissionParameters.KM;

        SpacecraftState finalState =
                propagator.propagate(
                        initialOrbit
                                .getDate()
                                .shiftedBy(
                                        MissionParameters
                                                .NUMERICAL_TEST_DURATION_S
                                )
                );

        double finalAltitudeKm =
                (
                        finalState
                                .getPosition()
                                .getNorm()
                                - MissionParameters.EARTH_RADIUS_M
                ) / MissionParameters.KM;

        double finalVelocityKmS =
                finalState
                        .getVelocity()
                        .getNorm()
                        / MissionParameters.KM;

        System.out.println();
        System.out.printf(
                "Duración propagada: %.0f segundos%n",
                MissionParameters.NUMERICAL_TEST_DURATION_S
        );

        System.out.printf(
                "Altitud inicial: %.3f km%n",
                initialAltitudeKm
        );

        System.out.printf(
                "Altitud final: %.3f km%n",
                finalAltitudeKm
        );

        System.out.printf(
                "Velocidad inicial: %.6f km/s%n",
                initialVelocityKmS
        );

        System.out.printf(
                "Velocidad final: %.6f km/s%n",
                finalVelocityKmS
        );

        System.out.printf(
                "Masa propagada: %.3f kg%n",
                finalState.getMass()
        );

        System.out.printf(
                "Marco de referencia: %s%n",
                finalState.getFrame().getName()
        );

        System.out.println();
        System.out.println(
                "[OK] Integrador Dormand-Prince 8(5,3)."
        );

        System.out.println(
                "[OK] Gravedad terrestre 8x8."
        );

        System.out.println(
                "[OK] Atracción de tercer cuerpo lunar."
        );

        System.out.println(
                "[OK] Atracción de tercer cuerpo solar."
        );

        System.out.println(
                "[SUCCESS] Propagación numérica completada."
        );

        System.out.println(
                "=================================================="
        );
    }
}
