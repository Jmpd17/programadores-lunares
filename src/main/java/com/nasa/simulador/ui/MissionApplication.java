package com.nasa.simulador.ui;

import java.util.List;

import org.hipparchus.geometry.euclidean.threed.Vector3D;

import com.nasa.simulador.OrekitConfig;
import com.nasa.simulador.physics.ArtemisMissionSimulation;
import com.nasa.simulador.physics.MissionParameters;
import com.nasa.simulador.physics.MissionSimulationResult;
import com.nasa.simulador.physics.TLIParameters;
import com.nasa.simulador.physics.TrajectoryPoint;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Interfaz JavaFX del simulador Artemis II.
 *
 * <p>Ejecuta Orekit en segundo plano, precalcula la trayectoria
 * y posteriormente la reproduce mediante AnimationTimer.</p>
 */
public final class MissionApplication extends Application {

    private static final double SPACE_WIDTH = 900.0;
    private static final double SPACE_HEIGHT = 650.0;
    private static final double VIEW_MARGIN = 55.0;

    /**
     * Aceleración base usada para que una misión de varios días
     * pueda demostrarse en pocos minutos.
     */
    private static final double BASE_SIMULATION_SECONDS_PER_REAL_SECOND =
            60.0;

    private TextField deltaVField;
    private TextField ignitionField;
    private TextField altitudeField;

    private Label timeValue;
    private Label earthAltitudeValue;
    private Label moonDistanceValue;
    private Label speedValue;
    private Label statusLabel;

    private Button executeButton;
    private Button playButton;
    private Button pauseButton;
    private Slider speedSlider;

    private Pane spacePane;
    private Group earthGroup;
    private Group moonGroup;
    private Group spacecraftGroup;
    private Polyline trajectoryTrail;

    private MissionSimulationResult simulationResult;
    private Task<MissionSimulationResult> simulationTask;
    private AnimationTimer animationTimer;

    private int currentPointIndex;
    private long lastFrameNanos;
    private double playbackSimulationSeconds;

    private double coordinateScale = 1.0;
    private double coordinateOffsetX;
    private double coordinateOffsetY;

    @Override
    public void start(Stage stage) {

        createAnimationTimer();

        BorderPane root = new BorderPane();

        root.setStyle(
                "-fx-background-color: #08111f;"
                        + "-fx-font-family: Arial;"
        );

        root.setTop(createHeader());
        root.setCenter(createSpaceView());
        root.setRight(createControlPanel());
        root.setBottom(createStatusBar());

        Scene scene = new Scene(
                root,
                1280,
                760
        );

        stage.setTitle(
                "Artemis II Mission Simulator"
        );
        stage.setMinWidth(1100);
        stage.setMinHeight(700);
        stage.setScene(scene);
        stage.show();

        resetVisualObjects();
    }

    private VBox createHeader() {

        Label title = new Label(
                "ARTEMIS II MISSION SIMULATOR"
        );

        title.setTextFill(Color.WHITE);
        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        24
                )
        );

        Label subtitle = new Label(
                "Programadores Lunares | Motor orbital Orekit"
        );

        subtitle.setTextFill(
                Color.web("#79c8ff")
        );

        VBox header = new VBox(
                4,
                title,
                subtitle
        );

        header.setPadding(
                new Insets(15, 20, 15, 20)
        );

        header.setStyle(
                "-fx-background-color: #102a43;"
                        + "-fx-border-color: #1b6ca8;"
                        + "-fx-border-width: 0 0 2 0;"
        );

        return header;
    }

    private Pane createSpaceView() {

        spacePane = new Pane();

        spacePane.setPrefSize(
                SPACE_WIDTH,
                SPACE_HEIGHT
        );

        spacePane.setMinSize(
                620,
                520
        );

        spacePane.setStyle(
                "-fx-background-color: "
                        + "linear-gradient(to bottom, #02050a, #081525);"
        );

        trajectoryTrail = new Polyline();
        trajectoryTrail.setStroke(
                Color.web("#57b7ed", 0.75)
        );
        trajectoryTrail.setStrokeWidth(1.7);

        earthGroup = createEarthGroup();
        moonGroup = createMoonGroup();
        spacecraftGroup = createSpacecraftGroup();

        spacePane.getChildren().addAll(
                trajectoryTrail,
                earthGroup,
                moonGroup,
                spacecraftGroup
        );

        return spacePane;
    }

    private Group createEarthGroup() {

        Circle glow = new Circle(
                0,
                0,
                82,
                Color.web("#174f73", 0.35)
        );

        Circle earth = new Circle(
                0,
                0,
                63,
                Color.web("#247db3")
        );

        Circle landOne = new Circle(
                -18,
                -15,
                18,
                Color.web("#4da66d")
        );

        Circle landTwo = new Circle(
                21,
                18,
                11,
                Color.web("#4da66d")
        );

        Label label = createSpaceLabel(
                "TIERRA",
                -28,
                78
        );

        return new Group(
                glow,
                earth,
                landOne,
                landTwo,
                label
        );
    }

    private Group createMoonGroup() {

        Circle glow = new Circle(
                0,
                0,
                34,
                Color.web("#ffffff", 0.12)
        );

        Circle moon = new Circle(
                0,
                0,
                23,
                Color.web("#c8ced4")
        );

        Circle craterOne = new Circle(
                -7,
                -5,
                4,
                Color.web("#9ba2a8", 0.65)
        );

        Circle craterTwo = new Circle(
                8,
                7,
                3,
                Color.web("#9ba2a8", 0.65)
        );

        Label label = createSpaceLabel(
                "LUNA",
                -18,
                34
        );

        return new Group(
                glow,
                moon,
                craterOne,
                craterTwo,
                label
        );
    }

    private Group createSpacecraftGroup() {

        Circle spacecraft = new Circle(
                0,
                0,
                6,
                Color.web("#ffcc4d")
        );

        Circle glow = new Circle(
                0,
                0,
                11,
                Color.web("#ffcc4d", 0.18)
        );

        Label label = createSpaceLabel(
                "NAVE",
                -15,
                -27
        );

        return new Group(
                glow,
                spacecraft,
                label
        );
    }

    private Label createSpaceLabel(
            String text,
            double x,
            double y
    ) {

        Label label = new Label(text);

        label.setTextFill(
                Color.web("#c9e7ff")
        );
        label.setLayoutX(x);
        label.setLayoutY(y);

        return label;
    }

    private ScrollPane createControlPanel() {

        VBox panel = new VBox(12);

        panel.setPadding(
                new Insets(18)
        );
        panel.setPrefWidth(330);
        panel.setStyle(
                "-fx-background-color: #102030;"
        );

        deltaVField = createInput(
                String.format(
                        "%.3f",
                        MissionParameters
                                .DEFAULT_TLI_DELTA_V_KM_S
                )
        );

        ignitionField = createInput(
                String.format(
                        "%.3f",
                        MissionParameters
                                .DEFAULT_TLI_IGNITION_OFFSET_H
                )
        );

        altitudeField = createInput(
                String.format(
                        "%.0f",
                        MissionParameters
                                .PARKING_ALTITUDE_M
                                / MissionParameters.KM
                )
        );

        panel.getChildren().addAll(
                createSectionTitle(
                        "PARÁMETROS DE MISIÓN"
                ),
                createField(
                        "Delta-v TLI (km/s)",
                        deltaVField
                ),
                createField(
                        "Encendido TLI (horas)",
                        ignitionField
                ),
                createField(
                        "Órbita inicial (km)",
                        altitudeField
                ),
                createActionButtons(),
                new Separator(),
                createSectionTitle(
                        "TELEMETRÍA"
                )
        );

        timeValue = createTelemetryValue(
                "0.000 h"
        );

        earthAltitudeValue =
                createTelemetryValue(
                        "185.000 km"
                );

        moonDistanceValue =
                createTelemetryValue(
                        "-- km"
                );

        speedValue =
                createTelemetryValue(
                        "-- km/s"
                );

        panel.getChildren().addAll(
                createTelemetryRow(
                        "Tiempo:",
                        timeValue
                ),
                createTelemetryRow(
                        "Altitud terrestre:",
                        earthAltitudeValue
                ),
                createTelemetryRow(
                        "Distancia lunar:",
                        moonDistanceValue
                ),
                createTelemetryRow(
                        "Velocidad:",
                        speedValue
                ),
                new Separator(),
                createPlaybackControls()
        );

        ScrollPane scrollPane =
                new ScrollPane(panel);

        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
                "-fx-background: #102030;"
                        + "-fx-background-color: #102030;"
        );

        return scrollPane;
    }

    private HBox createActionButtons() {

        executeButton =
                new Button(
                        "Ejecutar simulación"
                );

        Button resetButton =
                new Button(
                        "Reiniciar"
                );

        executeButton.setStyle(
                "-fx-background-color: #1976a8;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-weight: bold;"
        );

        resetButton.setStyle(
                "-fx-background-color: #374b5c;"
                        + "-fx-text-fill: white;"
        );

        executeButton.setOnAction(
                event -> executeSimulation()
        );

        resetButton.setOnAction(
                event -> resetInterface()
        );

        HBox buttons = new HBox(
                8,
                executeButton,
                resetButton
        );

        buttons.setAlignment(
                Pos.CENTER_LEFT
        );

        return buttons;
    }

    private VBox createPlaybackControls() {

        Label title =
                createSectionTitle(
                        "REPRODUCCIÓN"
                );

        playButton =
                new Button(
                        "Reproducir"
                );

        pauseButton =
                new Button(
                        "Pausar"
                );

        playButton.setDisable(true);
        pauseButton.setDisable(true);

        playButton.setOnAction(
                event -> playAnimation()
        );

        pauseButton.setOnAction(
                event -> pauseAnimation()
        );

        speedSlider =
                new Slider(
                        1,
                        1000,
                        100
                );

        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(250);
        speedSlider.setBlockIncrement(25);

        Label speedLabel =
                new Label(
                        "Escala de tiempo: 100x"
                );

        speedLabel.setTextFill(
                Color.LIGHTGRAY
        );

        speedSlider.valueProperty()
                .addListener(
                        (observable, oldValue, newValue) ->
                                speedLabel.setText(
                                        String.format(
                                                "Escala de tiempo: %.0fx",
                                                newValue.doubleValue()
                                        )
                                )
                );

        HBox buttons = new HBox(
                8,
                playButton,
                pauseButton
        );

        return new VBox(
                8,
                title,
                buttons,
                speedSlider,
                speedLabel
        );
    }

    private HBox createStatusBar() {

        statusLabel = new Label(
                "Sistema preparado."
        );

        statusLabel.setTextFill(
                Color.web("#b9d8ec")
        );

        HBox statusBar =
                new HBox(
                        statusLabel
                );

        statusBar.setPadding(
                new Insets(8, 15, 8, 15)
        );

        statusBar.setStyle(
                "-fx-background-color: #0b1b29;"
                        + "-fx-border-color: #1b6ca8;"
                        + "-fx-border-width: 1 0 0 0;"
        );

        return statusBar;
    }

    private void executeSimulation() {

        try {

            pauseAnimation();
            clearTrajectoryAnimation();

            double deltaV =
                    parseNumber(
                            deltaVField,
                            "Delta-v"
                    );

            double ignitionHours =
                    parseNumber(
                            ignitionField,
                            "Tiempo de encendido"
                    );

            double altitudeKm =
                    parseNumber(
                            altitudeField,
                            "Altitud inicial"
                    );

            validateParameters(
                    deltaV,
                    ignitionHours,
                    altitudeKm
            );

            TLIParameters parameters =
                    new TLIParameters(
                            deltaV,
                            ignitionHours,
                            1.0,
                            0.0,
                            0.0,
                            MissionParameters
                                    .DEFAULT_TLI_ISP_S
                    );

            setSimulationControlsDisabled(true);

            statusLabel.setText(
                    "Calculando trayectoria con Orekit..."
            );

            simulationTask =
                    new Task<>() {

                        @Override
                        protected MissionSimulationResult call() {

                            OrekitConfig.init();

                            return ArtemisMissionSimulation.run(
                                    parameters,
                                    altitudeKm
                            );
                        }
                    };

            simulationTask.setOnSucceeded(
                    event -> {

                        simulationResult =
                                simulationTask.getValue();

                        setSimulationControlsDisabled(false);
                        prepareAnimation();

                        statusLabel.setText(
                                "Simulación completada: "
                                        + simulationResult
                                                .getTrajectory()
                                                .size()
                                        + " puntos. Pulsa Reproducir."
                        );
                    }
            );

            simulationTask.setOnFailed(
                    event -> {

                        setSimulationControlsDisabled(false);

                        Throwable error =
                                simulationTask.getException();

                        statusLabel.setText(
                                "Error en la simulación: "
                                        + (
                                        error == null
                                                ? "causa desconocida"
                                                : error.getMessage()
                                )
                        );

                        if (error != null) {
                            error.printStackTrace();
                        }
                    }
            );

            Thread simulationThread =
                    new Thread(
                            simulationTask,
                            "orekit-simulation-thread"
                    );

            simulationThread.setDaemon(true);
            simulationThread.start();

        } catch (IllegalArgumentException error) {

            statusLabel.setText(
                    "Parámetros inválidos: "
                            + error.getMessage()
            );
        }
    }

    private void setSimulationControlsDisabled(
            boolean simulationRunning
    ) {

        executeButton.setDisable(
                simulationRunning
        );

        playButton.setDisable(
                simulationRunning || simulationResult == null
        );

        pauseButton.setDisable(
                simulationRunning || simulationResult == null
        );
    }

    private void prepareAnimation() {

        List<TrajectoryPoint> points =
                simulationResult.getTrajectory();

        if (points.isEmpty()) {
            throw new IllegalStateException(
                    "La simulación no produjo puntos."
            );
        }

        calculateCoordinateTransform(points);
        clearTrajectoryAnimation();

        currentPointIndex = 0;
        playbackSimulationSeconds =
                points.get(0).getElapsedSeconds();
        lastFrameNanos = 0L;

        updateSceneWithPoint(
                points.get(0),
                true
        );

        playButton.setDisable(false);
        pauseButton.setDisable(false);
    }

    private void calculateCoordinateTransform(
            List<TrajectoryPoint> points
    ) {

        double minX = 0.0;
        double maxX = 0.0;
        double minY = 0.0;
        double maxY = 0.0;

        for (TrajectoryPoint point : points) {

            Vector3D spacecraftPosition =
                    point.getPositionM();

            Vector3D moonPosition =
                    point.getMoonPositionM();

            minX = Math.min(
                    minX,
                    Math.min(
                            spacecraftPosition.getX(),
                            moonPosition.getX()
                    )
            );

            maxX = Math.max(
                    maxX,
                    Math.max(
                            spacecraftPosition.getX(),
                            moonPosition.getX()
                    )
            );

            minY = Math.min(
                    minY,
                    Math.min(
                            spacecraftPosition.getY(),
                            moonPosition.getY()
                    )
            );

            maxY = Math.max(
                    maxY,
                    Math.max(
                            spacecraftPosition.getY(),
                            moonPosition.getY()
                    )
            );
        }

        double rangeX =
                Math.max(
                        1.0,
                        maxX - minX
                );

        double rangeY =
                Math.max(
                        1.0,
                        maxY - minY
                );

        double width =
                Math.max(
                        SPACE_WIDTH,
                        spacePane.getWidth()
                );

        double height =
                Math.max(
                        SPACE_HEIGHT,
                        spacePane.getHeight()
                );

        double scaleX =
                (width - 2.0 * VIEW_MARGIN)
                        / rangeX;

        double scaleY =
                (height - 2.0 * VIEW_MARGIN)
                        / rangeY;

        coordinateScale =
                Math.min(
                        scaleX,
                        scaleY
                );

        coordinateOffsetX =
                VIEW_MARGIN - minX * coordinateScale;

        coordinateOffsetY =
                height - VIEW_MARGIN
                        + minY * coordinateScale;

        moveGroupToPosition(
                earthGroup,
                Vector3D.ZERO
        );
    }

    private void createAnimationTimer() {

        animationTimer =
                new AnimationTimer() {

                    @Override
                    public void handle(long now) {

                        if (simulationResult == null) {
                            stop();
                            return;
                        }

                        if (lastFrameNanos == 0L) {
                            lastFrameNanos = now;
                            return;
                        }

                        double realSeconds =
                                (now - lastFrameNanos)
                                        / 1_000_000_000.0;

                        lastFrameNanos = now;

                        double simulationRate =
                                BASE_SIMULATION_SECONDS_PER_REAL_SECOND
                                        * speedSlider.getValue();

                        playbackSimulationSeconds +=
                                realSeconds * simulationRate;

                        advanceAnimationToTime(
                                playbackSimulationSeconds
                        );
                    }
                };
    }

    private void advanceAnimationToTime(
            double targetElapsedSeconds
    ) {

        List<TrajectoryPoint> points =
                simulationResult.getTrajectory();

        while (currentPointIndex + 1
                < points.size()
                && points.get(currentPointIndex + 1)
                        .getElapsedSeconds()
                        <= targetElapsedSeconds) {

            currentPointIndex++;

            updateSceneWithPoint(
                    points.get(currentPointIndex),
                    true
            );
        }

        if (currentPointIndex
                >= points.size() - 1) {

            pauseAnimation();

            statusLabel.setText(
                    "Reproducción finalizada. "
                            + buildMissionSummary()
            );
        }
    }

    private void playAnimation() {

        if (simulationResult == null
                || simulationResult
                        .getTrajectory()
                        .isEmpty()) {

            statusLabel.setText(
                    "Primero ejecuta la simulación."
            );
            return;
        }

        if (currentPointIndex
                >= simulationResult
                        .getTrajectory()
                        .size() - 1) {

            prepareAnimation();
        }

        lastFrameNanos = 0L;
        animationTimer.start();

        statusLabel.setText(
                "Reproduciendo trayectoria..."
        );
    }

    private void pauseAnimation() {

        if (animationTimer != null) {
            animationTimer.stop();
        }

        lastFrameNanos = 0L;

        if (simulationResult != null) {
            statusLabel.setText(
                    "Reproducción pausada."
            );
        }
    }

    private void updateSceneWithPoint(
            TrajectoryPoint point,
            boolean appendTrail
    ) {

        double spacecraftX =
                toScreenX(
                        point.getPositionM()
                );

        double spacecraftY =
                toScreenY(
                        point.getPositionM()
                );

        spacecraftGroup.setLayoutX(
                spacecraftX
        );

        spacecraftGroup.setLayoutY(
                spacecraftY
        );

        moveGroupToPosition(
                moonGroup,
                point.getMoonPositionM()
        );

        if (appendTrail) {

            trajectoryTrail
                    .getPoints()
                    .addAll(
                            spacecraftX,
                            spacecraftY
                    );
        }

        updateTelemetry(point);
    }

    private void moveGroupToPosition(
            Group group,
            Vector3D position
    ) {

        group.setLayoutX(
                toScreenX(position)
        );

        group.setLayoutY(
                toScreenY(position)
        );
    }

    private double toScreenX(
            Vector3D position
    ) {

        return coordinateOffsetX
                + position.getX()
                * coordinateScale;
    }

    private double toScreenY(
            Vector3D position
    ) {

        return coordinateOffsetY
                - position.getY()
                * coordinateScale;
    }

    private void updateTelemetry(
            TrajectoryPoint point
    ) {

        timeValue.setText(
                String.format(
                        "%.3f h",
                        point.getElapsedSeconds()
                                / MissionParameters.HOUR
                )
        );

        earthAltitudeValue.setText(
                String.format(
                        "%.3f km",
                        point.getEarthAltitudeM()
                                / MissionParameters.KM
                )
        );

        moonDistanceValue.setText(
                String.format(
                        "%.3f km",
                        point.getMoonDistanceM()
                                / MissionParameters.KM
                )
        );

        speedValue.setText(
                String.format(
                        "%.6f km/s",
                        point.getSpeedMps()
                                / MissionParameters.KM
                )
        );
    }

    private String buildMissionSummary() {

        if (simulationResult == null) {
            return "";
        }

        return String.format(
                "Periapsis lunar: %s | Reentrada: %s",
                simulationResult
                        .getEvents()
                        .hasLunarPeriapsis()
                        ? "detectado"
                        : "no detectado",
                simulationResult
                        .isReentryDetected()
                        ? "detectada"
                        : "no detectada"
        );
    }

    private void clearTrajectoryAnimation() {

        currentPointIndex = 0;
        playbackSimulationSeconds = 0.0;
        lastFrameNanos = 0L;

        if (trajectoryTrail != null) {
            trajectoryTrail
                    .getPoints()
                    .clear();
        }
    }

    private void resetVisualObjects() {

        double width =
                Math.max(
                        SPACE_WIDTH,
                        spacePane == null
                                ? SPACE_WIDTH
                                : spacePane.getWidth()
                );

        double height =
                Math.max(
                        SPACE_HEIGHT,
                        spacePane == null
                                ? SPACE_HEIGHT
                                : spacePane.getHeight()
                );

        earthGroup.setLayoutX(
                width * 0.25
        );

        earthGroup.setLayoutY(
                height * 0.55
        );

        moonGroup.setLayoutX(
                width * 0.75
        );

        moonGroup.setLayoutY(
                height * 0.35
        );

        spacecraftGroup.setLayoutX(
                width * 0.34
        );

        spacecraftGroup.setLayoutY(
                height * 0.52
        );
    }

    private double parseNumber(
            TextField field,
            String parameterName
    ) {

        try {

            return Double.parseDouble(
                    field.getText()
                            .trim()
                            .replace(',', '.')
            );

        } catch (NumberFormatException error) {

            throw new IllegalArgumentException(
                    parameterName
                            + " debe contener un número válido."
            );
        }
    }

    private void validateParameters(
            double deltaV,
            double ignitionHours,
            double altitudeKm
    ) {

        if (!Double.isFinite(deltaV)
                || deltaV <= 0.0) {

            throw new IllegalArgumentException(
                    "El delta-v debe ser mayor que cero."
            );
        }

        if (!Double.isFinite(ignitionHours)
                || ignitionHours < 0.0) {

            throw new IllegalArgumentException(
                    "El encendido no puede ser negativo."
            );
        }

        if (!Double.isFinite(altitudeKm)
                || altitudeKm <= 120.0) {

            throw new IllegalArgumentException(
                    "La órbita inicial debe superar los 120 km."
            );
        }
    }

    private Label createSectionTitle(
            String text
    ) {

        Label label = new Label(text);

        label.setTextFill(
                Color.web("#79c8ff")
        );

        label.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        14
                )
        );

        return label;
    }

    private TextField createInput(
            String value
    ) {

        TextField field =
                new TextField(value);

        field.setStyle(
                "-fx-background-color: #eaf4fa;"
                        + "-fx-text-fill: #102030;"
        );

        return field;
    }

    private VBox createField(
            String name,
            TextField field
    ) {

        Label label = new Label(name);

        label.setTextFill(
                Color.LIGHTGRAY
        );

        return new VBox(
                4,
                label,
                field
        );
    }

    private Label createTelemetryValue(
            String value
    ) {

        Label label = new Label(value);

        label.setTextFill(
                Color.web("#ffcc4d")
        );

        label.setFont(
                Font.font(
                        "Consolas",
                        FontWeight.BOLD,
                        13
                )
        );

        return label;
    }

    private HBox createTelemetryRow(
            String name,
            Label value
    ) {

        Label label = new Label(name);

        label.setTextFill(
                Color.LIGHTGRAY
        );

        HBox row = new HBox(
                10,
                label,
                value
        );

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        return row;
    }

    private void resetInterface() {

        pauseAnimation();

        if (simulationTask != null
                && simulationTask.isRunning()) {

            simulationTask.cancel();
        }

        simulationResult = null;

        deltaVField.setText(
                String.format(
                        "%.3f",
                        MissionParameters
                                .DEFAULT_TLI_DELTA_V_KM_S
                )
        );

        ignitionField.setText(
                String.format(
                        "%.3f",
                        MissionParameters
                                .DEFAULT_TLI_IGNITION_OFFSET_H
                )
        );

        altitudeField.setText(
                String.format(
                        "%.0f",
                        MissionParameters
                                .PARKING_ALTITUDE_M
                                / MissionParameters.KM
                )
        );

        timeValue.setText(
                "0.000 h"
        );

        earthAltitudeValue.setText(
                "185.000 km"
        );

        moonDistanceValue.setText(
                "-- km"
        );

        speedValue.setText(
                "-- km/s"
        );

        clearTrajectoryAnimation();
        resetVisualObjects();

        executeButton.setDisable(false);
        playButton.setDisable(true);
        pauseButton.setDisable(true);

        statusLabel.setText(
                "Interfaz reiniciada."
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}
