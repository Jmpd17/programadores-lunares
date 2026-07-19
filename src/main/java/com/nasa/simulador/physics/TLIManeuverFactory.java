package com.nasa.simulador.physics;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.attitudes.AttitudeProvider;
import org.orekit.attitudes.LofOffset;
import org.orekit.forces.maneuvers.ImpulseManeuver;
import org.orekit.frames.LOFType;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.events.DateDetector;
import org.orekit.time.AbsoluteDate;

/**
 * Construye la maniobra impulsiva de inyección translunar.
 */
public final class TLIManeuverFactory {

    private TLIManeuverFactory() {
        // Evita crear instancias.
    }

    /**
     * Construye una maniobra TLI configurable.
     *
     * @param initialOrbit órbita inicial
     * @param parameters parámetros de la maniobra
     * @return maniobra impulsiva configurada
     */
    public static ImpulseManeuver create(
            Orbit initialOrbit,
            TLIParameters parameters
    ) {

        AbsoluteDate ignitionDate =
                getIgnitionDate(
                        initialOrbit,
                        parameters
                );

        /*
         * El detector activa la maniobra en la fecha indicada.
         */
        DateDetector ignitionDetector =
                new DateDetector(ignitionDate);

        /*
         * El marco VNC permite expresar el delta-v con:
         *
         * X = dirección de la velocidad.
         * Y = normal al plano orbital.
         * Z = componente co-normal.
         */
        AttitudeProvider maneuverAttitude =
                new LofOffset(
                        initialOrbit.getFrame(),
                        LOFType.VNC
                );

        Vector3D deltaVInVnc =
                parameters
                        .getDirectionVnc()
                        .scalarMultiply(
                                parameters
                                        .getDeltaVMagnitudeMps()
                        );

        return new ImpulseManeuver(
                ignitionDetector,
                maneuverAttitude,
                deltaVInVnc,
                parameters.getSpecificImpulseSeconds()
        );
    }

    /**
     * Calcula la fecha de encendido.
     *
     * @param initialOrbit órbita inicial
     * @param parameters parámetros TLI
     * @return fecha absoluta del encendido
     */
    public static AbsoluteDate getIgnitionDate(
            Orbit initialOrbit,
            TLIParameters parameters
    ) {

        return initialOrbit
                .getDate()
                .shiftedBy(
                        parameters.getIgnitionOffsetSeconds()
                );
    }
}
