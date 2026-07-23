package com.nasa.simulador.physics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Verifica las conversiones, normalización y validación de TLIParameters.
 */
class TLIParametersTest {

    @Test
    void convierteUnidadesYNormalizaLaDireccion() {

        TLIParameters parameters = new TLIParameters(
                3.2,
                1.25,
                2.0,
                0.0,
                0.0,
                450.0
        );

        assertEquals(3200.0, parameters.getDeltaVMagnitudeMps(), 1.0e-9);
        assertEquals(4500.0, parameters.getIgnitionOffsetSeconds(), 1.0e-9);
        assertEquals(1.0, parameters.getDirectionVnc().getNorm(), 1.0e-12);
        assertEquals(1.0, parameters.getDirectionVnc().getX(), 1.0e-12);
        assertEquals(450.0, parameters.getSpecificImpulseSeconds(), 1.0e-12);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, 0.0})
    void rechazaDeltaVNoPositivo(double deltaV) {

        assertThrows(
                IllegalArgumentException.class,
                () -> new TLIParameters(
                        deltaV,
                        0.5,
                        1.0,
                        0.0,
                        0.0,
                        450.0
                )
        );
    }

    @Test
    void rechazaDireccionNula() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new TLIParameters(
                        3.15,
                        0.5,
                        0.0,
                        0.0,
                        0.0,
                        450.0
                )
        );
    }
}
