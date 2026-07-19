package com.nasa.simulador;

import com.nasa.simulador.physics.TLIImpulseValidation;

/**
 * Punto principal de entrada del simulador Artemis II.
 */
public final class Main {

    private Main() {
        // Evita crear instancias.
    }

    /**
     * Inicializa Orekit y ejecuta la validación TLI.
     *
     * @param args argumentos recibidos por consola
     */
    public static void main(String[] args) {

        System.out.println(
                "========================================"
        );

        System.out.println(
                "   ARTEMIS II MISSION SIMULATOR"
        );

        System.out.println(
                "========================================"
        );

        try {

            OrekitConfig.init();

            TLIImpulseValidation.run();

        } catch (Exception e) {

            System.err.println();
            System.err.println(
                    "[ERROR] La ejecución falló."
            );

            System.err.println(
                    "Causa: " + e.getMessage()
            );

            e.printStackTrace();
            System.exit(1);
        }
    }
}