package com.nasa.simulador.physics;

import org.hipparchus.ode.nonstiff.AdaptiveStepsizeIntegrator;
import org.hipparchus.ode.nonstiff.DormandPrince853Integrator;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.OrbitType;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.ToleranceProvider;
import org.orekit.propagation.numerical.NumericalPropagator;

/**
 * Construye el propagador numérico de la misión.
 */
public final class NumericalPropagatorFactory {

    private NumericalPropagatorFactory() {
        // Evita crear instancias.
    }

    /**
     * Crea un propagador mostrando información.
     *
     * @param initialOrbit órbita inicial
     * @return propagador configurado
     */
    public static NumericalPropagator create(
            Orbit initialOrbit
    ) {

        return createInternal(
                initialOrbit,
                true
        );
    }

    /**
     * Crea un propagador sin imprimir mensajes.
     *
     * <p>Se utiliza durante la búsqueda automática
     * de parámetros para evitar llenar la consola.</p>
     *
     * @param initialOrbit órbita inicial
     * @return propagador configurado
     */
    public static NumericalPropagator createQuiet(
            Orbit initialOrbit
    ) {

        return createInternal(
                initialOrbit,
                false
        );
    }

    private static NumericalPropagator createInternal(
            Orbit initialOrbit,
            boolean verbose
    ) {

        if (verbose) {
            System.out.println();
            System.out.println(
                    "[INFO] Creando NumericalPropagator..."
            );
        }

        double[][] tolerances =
                ToleranceProvider
                        .getDefaultToleranceProvider(
                                MissionParameters
                                        .POSITION_TOLERANCE_M
                        )
                        .getTolerances(
                                initialOrbit,
                                OrbitType.CARTESIAN
                        );

        AdaptiveStepsizeIntegrator integrator =
                new DormandPrince853Integrator(
                        MissionParameters
                                .INTEGRATOR_MIN_STEP_S,
                        MissionParameters
                                .INTEGRATOR_MAX_STEP_S,
                        tolerances[0],
                        tolerances[1]
                );

        integrator.setInitialStepSize(
                MissionParameters
                        .INTEGRATOR_INITIAL_STEP_S
        );

        NumericalPropagator propagator =
                new NumericalPropagator(integrator);

        propagator.setOrbitType(
                OrbitType.CARTESIAN
        );

        SpacecraftState initialState =
                new SpacecraftState(initialOrbit)
                        .withMass(
                                MissionParameters
                                        .INITIAL_SPACECRAFT_MASS_KG
                        );

        propagator.setInitialState(
                initialState
        );

        ForceModelFactory.addRequiredForceModels(
                propagator,
                verbose
        );

        propagator.setResetAtEnd(false);

        if (verbose) {
            System.out.println(
                    "[SUCCESS] NumericalPropagator configurado."
            );
        }

        return propagator;
    }
}
