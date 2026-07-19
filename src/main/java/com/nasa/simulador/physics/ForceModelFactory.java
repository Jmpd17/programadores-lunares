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
 * Configura los modelos de fuerza requeridos por el motor orbital.
 */
public final class ForceModelFactory {

    private static final int GRAVITY_DEGREE = 8;
    private static final int GRAVITY_ORDER = 8;

    private ForceModelFactory() {
        // Evita crear instancias.
    }

    /**
     * Agrega al propagador los modelos de fuerza obligatorios.
     *
     * @param propagator propagador numérico que será configurado
     */
    public static void addRequiredForceModels(
            NumericalPropagator propagator
    ) {

        System.out.println(
                "[INFO] Configurando modelos de fuerza..."
        );

        /*
         * Marco terrestre rotatorio requerido para evaluar
         * los armónicos gravitatorios.
         */
        Frame earthFixedFrame = FramesFactory.getITRF(
                IERSConventions.IERS_2010,
                false
        );

        /*
         * Campo gravitatorio terrestre normalizado de grado
         * y orden 8x8.
         */
        NormalizedSphericalHarmonicsProvider gravityProvider =
                GravityFieldFactory.getNormalizedProvider(
                        GRAVITY_DEGREE,
                        GRAVITY_ORDER
                );

        /*
         * Define explícitamente la atracción central newtoniana
         * usando el valor μ del modelo gravitatorio cargado.
         */
        propagator.setMu(gravityProvider.getMu());

        propagator.addForceModel(
                new HolmesFeatherstoneAttractionModel(
                        earthFixedFrame,
                        gravityProvider
                )
        );

        /*
         * Atracciones de tercer cuerpo.
         */
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