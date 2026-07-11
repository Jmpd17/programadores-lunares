package com.nasa.simulador;

import java.io.File;

import org.orekit.data.DataContext;
import org.orekit.data.DirectoryCrawler;

public final class OrekitConfig {

    private OrekitConfig() {
        // Evita crear objetos de esta clase.
    }

    public static void init() {

        System.out.println("[INFO] Configurando Orekit...");

        File orekitData = new File("orekit-data");

        if (!orekitData.exists() || !orekitData.isDirectory()) {
            throw new IllegalStateException(
                    "No se encontró la carpeta orekit-data en: "
                    + orekitData.getAbsolutePath()
            );
        }

        DataContext.getDefault()
                .getDataProvidersManager()
                .addProvider(new DirectoryCrawler(orekitData));

        System.out.println("[SUCCESS] Orekit configurado correctamente.");
    }
}
