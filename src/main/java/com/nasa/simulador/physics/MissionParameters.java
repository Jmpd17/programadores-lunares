package com.nasa.simulador.physics;

import org.hipparchus.util.FastMath;
import org.orekit.utils.Constants;

/**
 * Centraliza los parámetros utilizados por el motor orbital.
 */
public final class MissionParameters {

    /** Conversión de kilómetros a metros. */
    public static final double KM = 1_000.0;

    /** Conversión de horas a segundos. */
    public static final double HOUR = 3_600.0;

    /** Radio ecuatorial terrestre WGS84. */
    public static final double EARTH_RADIUS_M =
            Constants.WGS84_EARTH_EQUATORIAL_RADIUS;

    /** Achatamiento terrestre WGS84. */
    public static final double EARTH_FLATTENING =
            Constants.WGS84_EARTH_FLATTENING;

    /** Parámetro gravitacional terrestre. */
    public static final double EARTH_MU =
            Constants.EGM96_EARTH_MU;

    /** Radio ecuatorial aproximado de la Luna. */
    public static final double MOON_RADIUS_M =
            Constants.MOON_EQUATORIAL_RADIUS;

    /** Altitud inicial de la órbita de estacionamiento. */
    public static final double PARKING_ALTITUDE_M =
            185.0 * KM;

    /** Inclinación inicial de la órbita. */
    public static final double PARKING_INCLINATION_RAD =
            FastMath.toRadians(28.5);

    /** Componentes de excentricidad circular. */
    public static final double PARKING_EXCENTRICITY_X = 0.0;
    public static final double PARKING_EXCENTRICITY_Y = 0.0;

    /** Masa inicial provisional de la nave. */
    public static final double INITIAL_SPACECRAFT_MASS_KG =
            26_000.0;

    /** Precisión posicional para calcular tolerancias. */
    public static final double POSITION_TOLERANCE_M =
            10.0;

    /** Configuración del integrador numérico. */
    public static final double INTEGRATOR_MIN_STEP_S = 0.01;
    public static final double INTEGRATOR_MAX_STEP_S = 300.0;
    public static final double INTEGRATOR_INITIAL_STEP_S = 30.0;

    /** Duración de la prueba numérica inicial. */
    public static final double NUMERICAL_TEST_DURATION_S =
            3_600.0;

    /** Magnitud predeterminada del delta-v TLI. */
    public static final double DEFAULT_TLI_DELTA_V_KM_S =
            3.15;

    /** Encendido TLI después de la inserción orbital. */
    public static final double DEFAULT_TLI_IGNITION_OFFSET_H =
            0.5;

    /** Impulso específico provisional. */
    public static final double DEFAULT_TLI_ISP_S =
            450.0;

    /** Tiempo de validación después del encendido. */
    public static final double TLI_VALIDATION_AFTER_BURN_S =
            2.0 * HOUR;

    /**
     * Intervalo entre puntos entregados a la interfaz.
     *
     * <p>Cinco minutos producen varios miles de muestras
     * durante una misión translunar.</p>
     */
    public static final double TRAJECTORY_SAMPLE_STEP_S =
            300.0;

    /** Cantidad mínima de puntos exigida. */
    public static final int MIN_TRAJECTORY_POINTS =
            500;

    /** Altitud de la interfaz de reentrada terrestre. */
    public static final double REENTRY_INTERFACE_ALTITUDE_M =
            120.0 * KM;

    /** Intervalo máximo para buscar eventos. */
    public static final double EVENT_MAX_CHECK_S =
            600.0;

    /** Precisión temporal de los eventos. */
    public static final double EVENT_THRESHOLD_S =
            0.1;

    /**
     * Duración máxima de seguridad.
     *
     * <p>La propagación se detiene antes si se detecta
     * la reentrada.</p>
     */
    public static final double MAX_MISSION_DURATION_S =
            16.0 * Constants.JULIAN_DAY;

    private MissionParameters() {
        // Evita crear instancias.
    }

    /**
 * Tiempo mínimo después de la TLI para aceptar
 * un posible acercamiento lunar.
 */
public static final double MIN_LUNAR_EVENT_DELAY_S =
        24.0 * HOUR;

/**
 * Altitud máxima aceptada para considerar que ocurrió
 * un sobrevuelo lunar válido.
 *
 * Este valor es un criterio académico del prototipo.
 */
public static final double MAX_VALID_LUNAR_FLYBY_ALTITUDE_M =
        100_000.0 * KM;

/**
 * Duración utilizada para buscar el acercamiento lunar.
 */
public static final double TLI_SEARCH_DURATION_S =
        7.0 * Constants.JULIAN_DAY;

/**
 * Intervalo utilizado para revisar la distancia lunar
 * durante la búsqueda.
 */
public static final double TLI_SEARCH_SAMPLE_STEP_S =
        1_800.0;

/**
 * Rango de delta-v que se probará automáticamente.
 */
public static final double TLI_SEARCH_MIN_DELTA_V_KM_S =
        3.05;

public static final double TLI_SEARCH_MAX_DELTA_V_KM_S =
        3.25;

public static final double TLI_SEARCH_DELTA_V_STEP_KM_S =
        0.025;

/**
 * Cantidad de momentos de encendido probados
 * durante una órbita de estacionamiento.
 */
public static final int TLI_SEARCH_IGNITION_SAMPLES =
        18;

}
