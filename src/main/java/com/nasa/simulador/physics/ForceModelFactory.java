package com.nasa.simulador.physics;

import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.forces.gravity.HolmesFeatherstoneAttractionModel;
import org.orekit.forces.gravity.ThirdBodyAttraction;
import org.orekit.forces.gravity.potential.GravityFieldFactory;
import org.orekit.forces.gravity.potential.NormalizedSphericalHarmonicsProvider;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.propagation.numerical.NumericalPropagator;
import org.orekit.utils.IERSConventions;

/**
 * Configura los modelos de fuerza del motor orbital.
 */
public final class ForceModelFactory {

    private static final int GRAVITY_DEGREE = 8;
    private static final int GRAVITY_ORDER = 8;

    private ForceModelFactory() {
        // Evita crear instancias.
    }

    /**
     * Agrega los modelos de fuerza mostrando mensajes.
     *
     * @param propagator propagador numérico
     */
    public static void addRequiredForceModels(
            NumericalPropagator propagator
    ) {

        addRequiredForceModels(
                propagator,
                true
        );
    }

    /**
     * Agrega los modelos de fuerza.
     *
     * @param propagator propagador numérico
     * @param verbose indica si se muestran mensajes
     */
    static void addRequiredForceModels(
            NumericalPropagator propagator,
            boolean verbose
    ) {

        if (verbose) {
            System.out.println(
                    "[INFO] Configurando modelos de fuerza..."
            );
        }

        Frame earthFixedFrame =
                FramesFactory.getITRF(
                        IERSConventions.IERS_2010,
                        false
                );

        NormalizedSphericalHarmonicsProvider gravityProvider =
                GravityFieldFactory.getNormalizedProvider(
                        GRAVITY_DEGREE,
                        GRAVITY_ORDER
                );

        propagator.setMu(
                gravityProvider.getMu()
        );

        propagator.addForceModel(
                new HolmesFeatherstoneAttractionModel(
                        earthFixedFrame,
                        gravityProvider
                )
        );

        propagator.addForceModel(
                new ThirdBodyAttraction(
                        CelestialBodyFactory.getMoon()
                )
        );

        propagator.addForceModel(
                new ThirdBodyAttraction(
                        CelestialBodyFactory.getSun()
                )
        );

        if (verbose) {

            System.out.println(
                    "[SUCCESS] Gravedad terrestre 8x8 configurada."
            );

            System.out.println(
                    "[SUCCESS] Atracción gravitacional lunar configurada."
            );

            System.out.println(
                    "[SUCCESS] Atracción gravitacional solar configurada."
            );
        }
    }
}