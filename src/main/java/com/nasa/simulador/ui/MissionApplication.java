package com.nasa.simulador.ui;

import com.nasa.simulador.OrekitConfig;
import com.nasa.simulador.physics.ArtemisMissionSimulation;
import com.nasa.simulador.physics.MissionParameters;
import com.nasa.simulador.physics.MissionSimulationResult;
import com.nasa.simulador.physics.TLIParameters;
import com.nasa.simulador.physics.TrajectoryPoint;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Interfaz principal del simulador Artemis II.
 */
public final class MissionApplication extends Application {

    private static final double SPACE_WIDTH = 900.0;
    private static final double SPACE_HEIGHT = 650.0;

    private TextField deltaVField;
    private TextField ignitionField;
    private TextField altitudeField;

    private Label timeValue;
    private Label earthAltitudeValue;
    private Label moonDistanceValue;
    private Label speedValue;
    private Label statusLabel;

    private Circle spacecraft;
    private Button executeButton;

    private MissionSimulationResult simulationResult;
    private Task<MissionSimulationResult> simulationTask;

    @Override
    public void start(Stage stage) {

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

        Pane space = new Pane();

        space.setPrefSize(
                SPACE_WIDTH,
                SPACE_HEIGHT
        );

        space.setStyle(
                "-fx-background-color: "
                        + "linear-gradient(to bottom, #02050a, #081525);"
        );

        Circle earthGlow = new Circle(
                195,
                330,
                92,
                Color.web("#174f73", 0.35)
        );

        Circle earth = new Circle(
                195,
                330,
                72,
                Color.web("#247db3")
        );

        Circle earthLand = new Circle(
                175,
                310,
                22,
                Color.web("#4da66d")
        );

        Label earthLabel = createSpaceLabel(
                "TIERRA",
                165,
                420
        );

        Circle moonGlow = new Circle(
                700,
                220,
                38,
                Color.web("#ffffff", 0.12)
        );

        Circle moon = new Circle(
                700,
                220,
                25,
                Color.web("#c8ced4")
        );

        Label moonLabel = createSpaceLabel(
                "LUNA",
                680,
                270
        );

        Line referencePath = new Line(
                270,
                320,
                675,
                225
        );

        referencePath.setStroke(
                Color.web("#3e91c7", 0.45)
        );

        referencePath.getStrokeDashArray()
                .addAll(10.0, 8.0);

        spacecraft = new Circle(
                300,
                310,
                7,
                Color.web("#ffcc4d")
        );

        Label spacecraftLabel = createSpaceLabel(
                "NAVE",
                290,
                285
        );

        space.getChildren().addAll(
                earthGlow,
                earth,
                earthLand,
                earthLabel,
                moonGlow,
                moon,
                moonLabel,
                referencePath,
                spacecraft,
                spacecraftLabel
        );

        return space;
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

        Button playButton =
                new Button(
                        "Reproducir"
                );

        Button pauseButton =
                new Button(
                        "Pausar"
                );

        playButton.setDisable(true);
        pauseButton.setDisable(true);

        Slider speedSlider =
                new Slider(
                        1,
                        1000,
                        1
                );

        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(250);
        speedSlider.setBlockIncrement(10);

        Label speedLabel =
                new Label(
                        "Escala de tiempo: 1x"
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

            executeButton.setDisable(true);

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

                        executeButton.setDisable(false);

                        showInitialTelemetry();

                        statusLabel.setText(
                                "Simulación completada: "
                                        + simulationResult
                                                .getTrajectory()
                                                .size()
                                        + " puntos preparados."
                        );
                    }
            );

            simulationTask.setOnFailed(
                    event -> {

                        executeButton.setDisable(false);

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

    private void showInitialTelemetry() {

        if (simulationResult == null
                || simulationResult
                        .getTrajectory()
                        .isEmpty()) {

            return;
        }

        TrajectoryPoint point =
                simulationResult
                        .getTrajectory()
                        .get(0);

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

        spacecraft.setCenterX(300);
        spacecraft.setCenterY(310);

        executeButton.setDisable(false);

        statusLabel.setText(
                "Interfaz reiniciada."
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}
