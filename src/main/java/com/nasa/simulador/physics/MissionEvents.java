package com.nasa.simulador.physics;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.ode.events.Action;
import org.orekit.bodies.CelestialBody;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.events.AltitudeDetector;
import org.orekit.propagation.events.EventDetector;
import org.orekit.propagation.events.EventSlopeFilter;
import org.orekit.propagation.events.ExtremumApproachDetector;
import org.orekit.propagation.events.FilterType;
import org.orekit.propagation.events.handlers.EventHandler;
import org.orekit.propagation.numerical.NumericalPropagator;

/**
 * Registra los eventos importantes de la misión:
 *
 * <ul>
 *     <li>Periapsis del sobrevuelo lunar.</li>
 *     <li>Cruce descendente de la interfaz de reentrada.</li>
 * </ul>
 */
public final class MissionEvents {

    private SpacecraftState lunarPeriapsisState;
    private double minimumMoonDistanceM =
            Double.POSITIVE_INFINITY;

    private SpacecraftState reentryState;

    /**
     * Conecta los detectores al propagador.
     *
     * @param propagator propagador de la misión
     * @param earth modelo terrestre
     */
    public void attachTo(
            NumericalPropagator propagator,
            OneAxisEllipsoid earth
    ) {

        addLunarPeriapsisDetector(propagator);
        addReentryDetector(propagator, earth);
    }

    private void addLunarPeriapsisDetector(
            NumericalPropagator propagator
    ) {

        CelestialBody moon =
                CelestialBodyFactory.getMoon();

        /*
         * Detecta extremos de la distancia nave-Luna.
         */
        ExtremumApproachDetector rawDetector =
                new ExtremumApproachDetector(moon)
                        .withHandler(
                                new EventHandler() {

                                    @Override
                                    public Action eventOccurred(
                                            SpacecraftState state,
                                            EventDetector detector,
                                            boolean increasing
                                    ) {

                                        recordLunarApproach(
                                                state,
                                                moon
                                        );

                                        return Action.CONTINUE;
                                    }
                                }
                        );

        /*
         * La distancia deja de disminuir y comienza a aumentar
         * en el punto de mayor acercamiento.
         */
        EventDetector closestApproachOnly =
                new EventSlopeFilter<>(
                        rawDetector,
                        FilterType
                                .TRIGGER_ONLY_INCREASING_EVENTS
                );

        propagator.addEventDetector(
                closestApproachOnly
        );

        System.out.println(
                "[SUCCESS] Detector de periapsis lunar configurado."
        );
    }

    private void addReentryDetector(
            NumericalPropagator propagator,
            OneAxisEllipsoid earth
    ) {

        AltitudeDetector reentryDetector =
                new AltitudeDetector(
                        MissionParameters.EVENT_MAX_CHECK_S,
                        MissionParameters.EVENT_THRESHOLD_S,
                        MissionParameters
                                .REENTRY_INTERFACE_ALTITUDE_M,
                        earth
                ).withHandler(
                        new EventHandler() {

                            @Override
                            public Action eventOccurred(
                                    SpacecraftState state,
                                    EventDetector detector,
                                    boolean increasing
                            ) {

                                /*
                                 * Ignora un posible cruce ascendente.
                                 */
                                if (increasing) {
                                    return Action.CONTINUE;
                                }

                                reentryState = state;

                                System.out.println();
                                System.out.println(
                                        "[EVENTO] Interfaz de "
                                                + "reentrada detectada."
                                );

                                /*
                                 * La misión termina al cruzar
                                 * los 120 km en descenso.
                                 */
                                return Action.STOP;
                            }
                        }
                );

        propagator.addEventDetector(
                reentryDetector
        );

        System.out.println(
                "[SUCCESS] Detector de reentrada a 120 km configurado."
        );
    }

    private void recordLunarApproach(
            SpacecraftState state,
            CelestialBody moon
    ) {

        Vector3D moonPosition =
                moon.getPVCoordinates(
                        state.getDate(),
                        state.getFrame()
                ).getPosition();

        double distance =
                Vector3D.distance(
                        state.getPosition(),
                        moonPosition
                );

        /*
         * Se conserva únicamente el menor acercamiento
         * registrado durante toda la propagación.
         */
        if (distance < minimumMoonDistanceM) {

            minimumMoonDistanceM = distance;
            lunarPeriapsisState = state;
        }
    }

    /**
     * Indica si se encontró un periapsis lunar.
     *
     * @return verdadero si existe un evento lunar registrado
     */
    public boolean hasLunarPeriapsis() {
        return lunarPeriapsisState != null;
    }

    /**
     * Indica si se detectó la interfaz de reentrada.
     *
     * @return verdadero si la nave cruzó 120 km en descenso
     */
    public boolean hasReentry() {
        return reentryState != null;
    }

    public SpacecraftState getLunarPeriapsisState() {
        return lunarPeriapsisState;
    }

    public SpacecraftState getReentryState() {
        return reentryState;
    }

    public double getMinimumMoonDistanceM() {
        return minimumMoonDistanceM;
    }

    /**
     * Obtiene la altitud aproximada sobre la superficie lunar.
     *
     * @return altitud lunar en metros o NaN si no existe evento
     */
    public double getLunarPeriapsisAltitudeM() {

        if (!hasLunarPeriapsis()) {
            return Double.NaN;
        }

        return minimumMoonDistanceM
                - MissionParameters.MOON_RADIUS_M;
    }
}