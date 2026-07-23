package com.nasa.simulador.physics;

import java.util.Objects;

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
import org.orekit.time.AbsoluteDate;

/**
 * Registra los eventos principales de la misión:
 *
 * <ul>
 *     <li>Mayor acercamiento lunar posterior a la TLI.</li>
 *     <li>Cruce descendente de la interfaz de reentrada.</li>
 * </ul>
 */
public final class MissionEvents {

    private final AbsoluteDate ignitionDate;

    private SpacecraftState lunarPeriapsisState;

    private double minimumMoonDistanceM =
            Double.POSITIVE_INFINITY;

    private SpacecraftState reentryState;

    /**
     * Crea el administrador de eventos.
     *
     * @param ignitionDate fecha real del encendido TLI
     */
    public MissionEvents(AbsoluteDate ignitionDate) {

        this.ignitionDate = Objects.requireNonNull(
                ignitionDate,
                "La fecha de encendido TLI no puede ser nula."
        );
    }

    /**
     * Conecta los detectores al propagador.
     *
     * @param propagator propagador numérico
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

                                if (increasing) {
                                    return Action.CONTINUE;
                                }

                                reentryState = state;

                                System.out.println();
                                System.out.println(
                                        "[EVENTO] Interfaz de "
                                                + "reentrada detectada."
                                );

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

    /**
     * Registra solamente acercamientos ocurridos después de la TLI.
     */
    private void recordLunarApproach(
            SpacecraftState state,
            CelestialBody moon
    ) {

        double secondsAfterTli =
                state.getDate()
                        .durationFrom(ignitionDate);

        /*
         * Evita registrar mínimos anteriores al encendido
         * o demasiado cercanos al inicio de la misión.
         */
        if (secondsAfterTli
                < MissionParameters.MIN_LUNAR_EVENT_DELAY_S) {

            return;
        }

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

        if (distance < minimumMoonDistanceM) {

            minimumMoonDistanceM = distance;
            lunarPeriapsisState = state;
        }
    }

    /**
     * Indica si se encontró algún mínimo post-TLI.
     *
     * @return verdadero cuando existe un candidato
     */
    public boolean hasLunarApproachCandidate() {
        return lunarPeriapsisState != null;
    }

    /**
     * Indica si el acercamiento cumple el criterio
     * definido para considerarse sobrevuelo lunar.
     *
     * @return verdadero cuando el periapsis es válido
     */
    public boolean hasLunarPeriapsis() {

        return hasLunarApproachCandidate()
                && getLunarPeriapsisAltitudeM()
                <= MissionParameters
                        .MAX_VALID_LUNAR_FLYBY_ALTITUDE_M;
    }

    /**
     * Indica si se detectó la reentrada.
     *
     * @return verdadero cuando la nave cruzó 120 km
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
     * Calcula la altitud sobre la superficie lunar.
     *
     * @return altitud en metros o NaN
     */
    public double getLunarPeriapsisAltitudeM() {

        if (!hasLunarApproachCandidate()) {
            return Double.NaN;
        }

        return minimumMoonDistanceM
                - MissionParameters.MOON_RADIUS_M;
    }
}