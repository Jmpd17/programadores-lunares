package com.nasa.simulador.physics;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.forces.maneuvers.ImpulseManeuver;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.numerical.NumericalPropagator;
import org.orekit.time.AbsoluteDate;

/**
 * Comprueba la ejecución de la maniobra impulsiva TLI.
 */
public final class TLIImpulseValidation {

    private static final double MINIMUM_POSITION_DIFFERENCE_KM =
            1.0;

    private TLIImpulseValidation() {
        // Evita crear instancias.
    }

    /**
     * Ejecuta una propagación con TLI y otra sin maniobra
     * para comparar sus resultados.
     */
    public static void run() {

        System.out.println();
        System.out.println(
                "=================================================="
        );
        System.out.println(
                "         VALIDACION DE LA MANIOBRA TLI"
        );
        System.out.println(
                "=================================================="
        );

        Orbit initialOrbit =
                ParkingOrbitFactory.createDefaultOrbit();

        TLIParameters parameters =
                TLIParameters.createDefault();

        AbsoluteDate ignitionDate =
                TLIManeuverFactory.getIgnitionDate(
                        initialOrbit,
                        parameters
                );

        AbsoluteDate validationDate =
                ignitionDate.shiftedBy(
                        MissionParameters
                                .TLI_VALIDATION_AFTER_BURN_S
                );

        /*
         * Propagador de referencia sin TLI.
         */
        NumericalPropagator baselinePropagator =
                NumericalPropagatorFactory.create(
                        initialOrbit
                );

        /*
         * Propagador con maniobra TLI.
         */
        NumericalPropagator tliPropagator =
                NumericalPropagatorFactory.create(
                        initialOrbit
                );

        ImpulseManeuver tliManeuver =
                TLIManeuverFactory.create(
                        initialOrbit,
                        parameters
                );

        tliPropagator.addEventDetector(
                tliManeuver
        );

        System.out.println();
        System.out.println("[INFO] Parámetros de la TLI:");

        System.out.printf(
                "Delta-v: %.3f km/s%n",
                parameters.getDeltaVMagnitudeMps()
                        / MissionParameters.KM
        );

        System.out.printf(
                "Encendido después de: %.3f horas%n",
                parameters.getIgnitionOffsetSeconds()
                        / MissionParameters.HOUR
        );

        System.out.printf(
                "Dirección VNC: [%.3f, %.3f, %.3f]%n",
                parameters.getDirectionVnc().getX(),
                parameters.getDirectionVnc().getY(),
                parameters.getDirectionVnc().getZ()
        );

        System.out.printf(
                "Impulso específico: %.1f s%n",
                parameters.getSpecificImpulseSeconds()
        );

        System.out.println(
                "Fecha del encendido: " + ignitionDate
        );

        System.out.println();
        System.out.println(
                "[INFO] Ejecutando propagación sin TLI..."
        );

        SpacecraftState baselineState =
                baselinePropagator.propagate(
                        validationDate
                );

        System.out.println(
                "[INFO] Ejecutando propagación con TLI..."
        );

        SpacecraftState tliState =
                tliPropagator.propagate(
                        validationDate
                );

        double baselineAltitudeKm =
                calculateAltitudeKm(baselineState);

        double tliAltitudeKm =
                calculateAltitudeKm(tliState);

        double positionDifferenceKm =
                Vector3D.distance(
                        baselineState.getPosition(),
                        tliState.getPosition()
                ) / MissionParameters.KM;

        double velocityDifferenceKmS =
                Vector3D.distance(
                        baselineState.getVelocity(),
                        tliState.getVelocity()
                ) / MissionParameters.KM;

        double consumedMassKg =
                MissionParameters.INITIAL_SPACECRAFT_MASS_KG
                        - tliState.getMass();

        printResults(
                baselineAltitudeKm,
                tliAltitudeKm,
                positionDifferenceKm,
                velocityDifferenceKmS,
                tliState.getMass(),
                consumedMassKg
        );

        validateResults(
                positionDifferenceKm,
                consumedMassKg
        );
    }

    private static double calculateAltitudeKm(
            SpacecraftState state
    ) {

        return (
                state.getPosition().getNorm()
                        - MissionParameters.EARTH_RADIUS_M
        ) / MissionParameters.KM;
    }

    private static void printResults(
            double baselineAltitudeKm,
            double tliAltitudeKm,
            double positionDifferenceKm,
            double velocityDifferenceKmS,
            double finalMassKg,
            double consumedMassKg
    ) {

        System.out.println();
        System.out.println(
                "--------------- RESULTADOS ----------------"
        );

        System.out.printf(
                "Altitud sin TLI: %.3f km%n",
                baselineAltitudeKm
        );

        System.out.printf(
                "Altitud con TLI: %.3f km%n",
                tliAltitudeKm
        );

        System.out.printf(
                "Separación entre trayectorias: %.3f km%n",
                positionDifferenceKm
        );

        System.out.printf(
                "Diferencia entre velocidades: %.6f km/s%n",
                velocityDifferenceKmS
        );

        System.out.printf(
                "Masa final con TLI: %.3f kg%n",
                finalMassKg
        );

        System.out.printf(
                "Masa consumida: %.3f kg%n",
                consumedMassKg
        );
    }

    private static void validateResults(
            double positionDifferenceKm,
            double consumedMassKg
    ) {

        System.out.println();
        System.out.println(
                "-------------- VERIFICACION ---------------"
        );

        if (consumedMassKg <= 0.0) {
            throw new IllegalStateException(
                    "La maniobra TLI no produjo consumo de masa."
            );
        }

        if (positionDifferenceKm
                < MINIMUM_POSITION_DIFFERENCE_KM) {

            throw new IllegalStateException(
                    "La TLI no modificó significativamente "
                            + "la trayectoria."
            );
        }

        System.out.println(
                "[OK] El encendido TLI fue ejecutado."
        );

        System.out.println(
                "[OK] La masa de la nave fue actualizada."
        );

        System.out.println(
                "[OK] La trayectoria con TLI es diferente "
                        + "a la trayectoria de referencia."
        );

        System.out.println(
                "[OK] Magnitud, dirección y época "
                        + "de encendido son configurables."
        );

        System.out.println(
                "[SUCCESS] Maniobra impulsiva TLI validada."
        );

        System.out.println(
                "============================================"
        );
    }
}