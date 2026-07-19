package com.nasa.simulador.physics;

import org.hipparchus.ode.nonstiff.AdaptiveStepsizeIntegrator;
import org.hipparchus.ode.nonstiff.DormandPrince853Integrator;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.OrbitType;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.ToleranceProvider;
import org.orekit.propagation.numerical.NumericalPropagator;

/**
 * Construye y configura el propagador numérico de la misión.
 */
public final class NumericalPropagatorFactory {

    private NumericalPropagatorFactory() {
        // Evita crear instancias.
    }

    /**
     * Crea un propagador numérico con los modelos de fuerza
     * requeridos para el Entregable 4.
     *
     * @param initialOrbit órbita inicial de estacionamiento
     * @return propagador numérico configurado
     */
    public static NumericalPropagator create(
            Orbit initialOrbit
    ) {

        System.out.println();
        System.out.println(
                "[INFO] Creando NumericalPropagator..."
        );

        /*
         * Se calculan tolerancias apropiadas para propagación
         * en coordenadas cartesianas.
         */
        double[][] tolerances =
                ToleranceProvider
                        .getDefaultToleranceProvider(
                                MissionParameters.POSITION_TOLERANCE_M
                        )
                        .getTolerances(
                                initialOrbit,
                                OrbitType.CARTESIAN
                        );

        /*
         * Integrador adaptativo Dormand-Prince 8(5,3).
         */
        AdaptiveStepsizeIntegrator integrator =
                new DormandPrince853Integrator(
                        MissionParameters.INTEGRATOR_MIN_STEP_S,
                        MissionParameters.INTEGRATOR_MAX_STEP_S,
                        tolerances[0],
                        tolerances[1]
                );

        integrator.setInitialStepSize(
                MissionParameters.INTEGRATOR_INITIAL_STEP_S
        );

        NumericalPropagator propagator =
                new NumericalPropagator(integrator);

        /*
         * La integración se realizará usando posición
         * y velocidad cartesianas.
         */
        propagator.setOrbitType(
                OrbitType.CARTESIAN
        );

        /*
         * Estado inicial con masa provisional.
         *
         * Se usa withMass porque el constructor Orbit + masa
         * está obsoleto desde Orekit 13.
         */
        SpacecraftState initialState =
                new SpacecraftState(initialOrbit)
                        .withMass(
                                MissionParameters
                                        .INITIAL_SPACECRAFT_MASS_KG
                        );

        propagator.setInitialState(initialState);

        /*
         * Gravedad terrestre 8x8, Luna y Sol.
         */
        ForceModelFactory.addRequiredForceModels(
                propagator
        );

        /*
         * Evita que el propagador reemplace automáticamente
         * su estado inicial al terminar una propagación.
         */
        propagator.setResetAtEnd(false);

        System.out.println(
                "[SUCCESS] NumericalPropagator configurado."
        );

        return propagator;
    }
}
