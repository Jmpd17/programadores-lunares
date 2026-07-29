package com.nasa.simulador.physics;

import org.hipparchus.geometry.euclidean.threed.Vector3D;

/**
 * Contiene los parámetros configurables de la maniobra TLI.
 *
 * <p>La dirección se expresa mediante el marco orbital VNC:</p>
 *
 * <ul>
 *     <li>X: dirección de la velocidad.</li>
 *     <li>Y: dirección del momento angular orbital.</li>
 *     <li>Z: dirección co-normal.</li>
 * </ul>
 */
public final class TLIParameters {

    private final double deltaVMagnitudeMps;
    private final double ignitionOffsetSeconds;
    private final Vector3D directionVnc;
    private final double specificImpulseSeconds;

    /**
     * Construye los parámetros de una maniobra TLI.
     *
     * @param deltaVMagnitudeKmS magnitud del delta-v en km/s
     * @param ignitionOffsetHours horas desde la inserción orbital
     * @param directionX componente X en VNC
     * @param directionY componente Y en VNC
     * @param directionZ componente Z en VNC
     * @param specificImpulseSeconds impulso específico en segundos
     */
    public TLIParameters(
            double deltaVMagnitudeKmS,
            double ignitionOffsetHours,
            double directionX,
            double directionY,
            double directionZ,
            double specificImpulseSeconds
    ) {

        validatePositive(
                deltaVMagnitudeKmS,
                "La magnitud del delta-v"
        );

        validateNonNegative(
                ignitionOffsetHours,
                "El desplazamiento del encendido"
        );

        validatePositive(
                specificImpulseSeconds,
                "El impulso específico"
        );

        Vector3D rawDirection = new Vector3D(
                directionX,
                directionY,
                directionZ
        );

        if (!Double.isFinite(rawDirection.getNorm())
                || rawDirection.getNorm() == 0.0) {

            throw new IllegalArgumentException(
                    "La dirección del delta-v no puede ser nula."
            );
        }

        this.deltaVMagnitudeMps =
                deltaVMagnitudeKmS * MissionParameters.KM;

        this.ignitionOffsetSeconds =
                ignitionOffsetHours * MissionParameters.HOUR;

        this.directionVnc = rawDirection.normalize();

        this.specificImpulseSeconds =
                specificImpulseSeconds;
    }

    /**
     * Crea la configuración predeterminada del prototipo.
     *
     * @return parámetros TLI predeterminados
     */
    public static TLIParameters createDefault() {

        return new TLIParameters(
                MissionParameters.DEFAULT_TLI_DELTA_V_KM_S,
                MissionParameters.DEFAULT_TLI_IGNITION_OFFSET_H,
                1.0,
                0.0,
                0.0,
                MissionParameters.DEFAULT_TLI_ISP_S
        );
    }

    public double getDeltaVMagnitudeMps() {
        return deltaVMagnitudeMps;
    }

    public double getIgnitionOffsetSeconds() {
        return ignitionOffsetSeconds;
    }

    public Vector3D getDirectionVnc() {
        return directionVnc;
    }

    public double getSpecificImpulseSeconds() {
        return specificImpulseSeconds;
    }

    private static void validatePositive(
            double value,
            String parameterName
    ) {

        if (!Double.isFinite(value) || value <= 0.0) {
            throw new IllegalArgumentException(
                    parameterName
                            + " debe ser un número mayor que cero."
            );
        }
    }

    private static void validateNonNegative(
            double value,
            String parameterName
    ) {

        if (!Double.isFinite(value) || value < 0.0) {
            throw new IllegalArgumentException(
                    parameterName
                            + " debe ser un número igual o mayor que cero."
            );
        }
    }
}
