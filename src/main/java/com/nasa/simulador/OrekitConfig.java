package com.nasa.simulador;

import java.io.File;
import java.util.Set;
import java.util.TreeSet;

import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.forces.gravity.potential.GravityFieldFactory;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.IERSConventions;

/**
 * Inicializa y valida los datos externos utilizados por Orekit.
 */
public final class OrekitConfig {

    private static final String DATA_DIRECTORY = "orekit-data";

    private static boolean initialized = false;

    private OrekitConfig() {
        // Evita la creación de instancias.
    }

    /**
     * Inicializa el contexto de datos de Orekit.
     */
    public static synchronized void init() {

        if (initialized) {
            return;
        }

        System.out.println("[INFO] Inicializando Orekit...");

        File dataDirectory = new File(DATA_DIRECTORY);

        validateDirectory(dataDirectory);

        DataProvidersManager manager = DataContext.getDefault()
                .getDataProvidersManager();

        manager.clearProviders();
        manager.clearLoadedDataNames();

        manager.addProvider(new DirectoryCrawler(dataDirectory));

        System.out.println(
                "[INFO] Fuente de datos: "
                        + dataDirectory.getAbsolutePath()
        );

        validateRequiredData();
        showLoadedData(manager);

        initialized = true;

        System.out.println(
                "[SUCCESS] Orekit inicializado con datos completos."
        );
    }

    /**
     * Verifica la existencia y contenido de orekit-data.
     */
    private static void validateDirectory(File dataDirectory) {

        if (!dataDirectory.exists()) {
            throw new IllegalStateException(
                    "No se encontró orekit-data en: "
                            + dataDirectory.getAbsolutePath()
            );
        }

        if (!dataDirectory.isDirectory()) {
            throw new IllegalStateException(
                    "La ruta orekit-data no corresponde a una carpeta."
            );
        }

        File[] content = dataDirectory.listFiles();

        if (content == null || content.length == 0) {
            throw new IllegalStateException(
                    "La carpeta orekit-data está vacía."
            );
        }
    }

    /**
     * Fuerza la carga de los datos necesarios para el motor orbital.
     */
    private static void validateRequiredData() {

        System.out.println("[INFO] Validando UTC y segundos intercalares...");
        TimeScalesFactory.getUTC();

        System.out.println(
                "[INFO] Validando orientación terrestre IERS 2010..."
        );

        FramesFactory.getITRF(
                IERSConventions.IERS_2010,
                false
        );

        System.out.println(
                "[INFO] Validando gravedad terrestre 8x8..."
        );

        GravityFieldFactory.getNormalizedProvider(8, 8);

        System.out.println(
                "[INFO] Validando efemérides de la Luna y el Sol..."
        );

        AbsoluteDate validationDate = new AbsoluteDate(
                2026,
                1,
                1,
                0,
                0,
                0.0,
                TimeScalesFactory.getUTC()
        );

        Frame frame = FramesFactory.getGCRF();

        CelestialBodyFactory.getMoon()
                .getPVCoordinates(validationDate, frame);

        CelestialBodyFactory.getSun()
                .getPVCoordinates(validationDate, frame);
    }

    /**
     * Muestra los archivos que Orekit cargó realmente.
     */
    private static void showLoadedData(
            DataProvidersManager manager
    ) {

        Set<String> loadedData = new TreeSet<>(
                manager.getLoadedDataNames()
        );

        if (loadedData.isEmpty()) {
            throw new IllegalStateException(
                    "Orekit no cargó ningún archivo de datos."
            );
        }

        System.out.println();
        System.out.println(
                "[INFO] Cantidad de archivos cargados: "
                        + loadedData.size()
        );

        for (String dataName : loadedData) {
            System.out.println("       - " + dataName);
        }

        System.out.println();
    }
}
