package com.nasa.simulador;

import com.nasa.simulador.physics.ArtemisMissionSimulation;
import com.nasa.simulador.physics.MissionSimulationResult;
import com.nasa.simulador.physics.TLIParameterSearch;
import com.nasa.simulador.physics.TLIParameters;

/**
 * Punto principal del simulador Artemis II.
 */
public final class Main {

    private Main() {
        // Evita crear instancias.
    }

    /**
     * Busca los mejores parámetros TLI y ejecuta la misión.
     *
     * @param args argumentos de consola
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

            TLIParameters bestParameters =
                    TLIParameterSearch.findBest();

            MissionSimulationResult result =
                    ArtemisMissionSimulation.run(
                            bestParameters
                    );

            System.out.println();
            System.out.printf(
                    "[INFO] Puntos para JavaFX: %d%n",
                    result.getTrajectory().size()
            );

            System.out.printf(
                    "[INFO] Periapsis lunar válido: %s%n",
                    result.getEvents()
                            .hasLunarPeriapsis()
            );

            System.out.printf(
                    "[INFO] Reentrada detectada: %s%n",
                    result.isReentryDetected()
            );

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