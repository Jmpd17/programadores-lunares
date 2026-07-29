package com.nasa.simulador.physics;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.nasa.simulador.OrekitConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.forces.ForceModel;
import org.orekit.forces.gravity.HolmesFeatherstoneAttractionModel;
import org.orekit.forces.gravity.ThirdBodyAttraction;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.numerical.NumericalPropagator;

/**
 * Verifica OAM-3: modelos de fuerza de la propagación numérica.
 */
class ForceModelFactoryTest {

    @BeforeAll
    static void initializeOrekit() {
        OrekitConfig.init();
    }

    @Test
    void oam3IncluyeGravedad8x8LunaYSol() {

        Orbit orbit = ParkingOrbitFactory.createDefaultOrbit();

        NumericalPropagator propagator =
                NumericalPropagatorFactory.createQuiet(orbit);

        List<ForceModel> forceModels =
                propagator.getAllForceModels();

        assertTrue(
                forceModels.stream()
                        .anyMatch(
                                HolmesFeatherstoneAttractionModel.class
                                        ::isInstance
                        ),
                "Debe existir el modelo de gravedad terrestre 8x8."
        );

        List<String> thirdBodyNames =
                forceModels.stream()
                        .filter(ThirdBodyAttraction.class::isInstance)
                        .map(ThirdBodyAttraction.class::cast)
                        .map(ThirdBodyAttraction::getBodyName)
                        .toList();

        assertTrue(
                thirdBodyNames.stream()
                        .anyMatch(
                                name -> name.equalsIgnoreCase(
                                        CelestialBodyFactory
                                                .getMoon()
                                                .getName()
                                )
                        ),
                "Debe existir la atracción gravitacional de la Luna."
        );

        assertTrue(
                thirdBodyNames.stream()
                        .anyMatch(
                                name -> name.equalsIgnoreCase(
                                        CelestialBodyFactory
                                                .getSun()
                                                .getName()
                                )
                        ),
                "Debe existir la atracción gravitacional del Sol."
        );
    }
}
