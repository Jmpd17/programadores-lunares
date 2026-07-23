package com.nasa.simulador;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;

/**
 * Verifica OAM-1: inicialización del contexto de datos de Orekit.
 */
class OrekitConfigTest {

    @Test
    void oam1InicializaOrekitYRegistraDatos() {

        assertDoesNotThrow(OrekitConfig::init);

        DataProvidersManager manager =
                DataContext.getDefault()
                        .getDataProvidersManager();

        assertFalse(
                manager.getProviders().isEmpty(),
                "Debe existir al menos un proveedor de datos registrado."
        );

        assertFalse(
                manager.getLoadedDataNames().isEmpty(),
                "Orekit debe haber cargado al menos una fuente de datos."
        );
    }
}
