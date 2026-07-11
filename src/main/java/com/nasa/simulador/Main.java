package com.nasa.simulador;

public final class Main {

    private Main() {
        // Evita crear objetos de esta clase.
    }

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("   ARTEMIS II MISSION SIMULATOR");
        System.out.println("========================================");

        try {

            OrekitConfig.init();

            /*
             * Entregable actual:
             * ejecutar el Spike de trayectoria TLI.
             */
            TLIPrototype.run();

            /*
             * Para ejecutar también la prueba LEO,
             * puedes descomentar esta línea:
             *
             * LEOSimulation.run();
             */

        } catch (Exception e) {

            System.err.println();
            System.err.println("[ERROR] La ejecución falló.");
            System.err.println("Causa: " + e.getMessage());

            e.printStackTrace();

            System.exit(1);
        }
    }
}