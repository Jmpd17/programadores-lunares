package com.nasa.simulador;

import java.io.File;

import org.orekit.data.DataContext;
import org.orekit.data.DirectoryCrawler;

public final class OrekitConfig {

    private static boolean initialized = false;

    private OrekitConfig() {
        // Evita crear objetos de esta clase.
    }

    public static synchronized void init() {

        if (initialized) {
            return;
        }

        System.out.println("[INFO] Inicializando Orekit...");

        File orekitData = new File("orekit-data");

        if (orekitData.exists() && orekitData.isDirectory()) {

            DataContext.getDefault()
                    .getDataProvidersManager()
                    .addProvider(new DirectoryCrawler(orekitData));

            System.out.println(
                    "[SUCCESS] Carpeta orekit-data cargada correctamente."
            );

        } else {

            System.out.println(
                    "[WARNING] No se encontró la carpeta orekit-data."
            );

            System.out.println(
                    "[INFO] El programa continuará en modo básico."
            );
        }

        initialized = true;
    }
}
