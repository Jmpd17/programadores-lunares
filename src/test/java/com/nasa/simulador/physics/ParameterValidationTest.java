package com.nasa.simulador.physics;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Verifica UI-3: validación de parámetros configurables.
 */
class ParameterValidationTest {

    @ParameterizedTest
    @ValueSource(doubles = {
            -3.15,
            0.0,
            Double.NaN,
            Double.POSITIVE_INFINITY
    })
    void ui3RechazaDeltaVInvalido(double deltaV) {

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

    @ParameterizedTest
    @ValueSource(doubles = {
            -0.01,
            Double.NaN,
            Double.NEGATIVE_INFINITY
    })
    void ui3RechazaHoraDeEncendidoInvalida(double hours) {

        assertThrows(
                IllegalArgumentException.class,
                () -> new TLIParameters(
                        3.15,
                        hours,
                        1.0,
                        0.0,
                        0.0,
                        450.0
                )
        );
    }

    @ParameterizedTest
    @ValueSource(doubles = {
            -450.0,
            0.0,
            Double.NaN
    })
    void ui3RechazaImpulsoEspecificoInvalido(double isp) {

        assertThrows(
                IllegalArgumentException.class,
                () -> new TLIParameters(
                        3.15,
                        0.5,
                        1.0,
                        0.0,
                        0.0,
                        isp
                )
        );
    }

    @ParameterizedTest
    @ValueSource(doubles = {
            -100.0,
            0.0,
            120.0,
            Double.NaN,
            Double.POSITIVE_INFINITY
    })
    void ui3RechazaAltitudInicialInvalida(double altitudeKm) {

        assertThrows(
                IllegalArgumentException.class,
                () -> ParkingOrbitFactory.createOrbit(altitudeKm)
        );
    }

    @ParameterizedTest
    @CsvSource({
            "3.05, 0.0, 121.0",
            "3.15, 0.5, 185.0",
            "3.25, 2.0, 300.0"
    })
    void ui3AceptaConfiguracionesValidas(
            double deltaV,
            double ignitionHours,
            double altitudeKm
    ) {

        assertDoesNotThrow(
                () -> new TLIParameters(
                        deltaV,
                        ignitionHours,
                        1.0,
                        0.0,
                        0.0,
                        450.0
                )
        );

        assertDoesNotThrow(
                () -> ParkingOrbitFactory.createOrbit(altitudeKm)
        );
    }

    @Test
    void ui3ConservaLosValoresPredeterminados() {

        TLIParameters defaults = TLIParameters.createDefault();

        assertEquals(
                MissionParameters.DEFAULT_TLI_DELTA_V_KM_S
                        * MissionParameters.KM,
                defaults.getDeltaVMagnitudeMps(),
                1.0e-9
        );

        assertEquals(
                MissionParameters.DEFAULT_TLI_IGNITION_OFFSET_H
                        * MissionParameters.HOUR,
                defaults.getIgnitionOffsetSeconds(),
                1.0e-9
        );
    }
}
