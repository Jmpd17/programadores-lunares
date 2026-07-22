package com.nasa.simulador.ui;

import com.nasa.simulador.physics.MissionParameters;

import javafx.application.Application;
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
 * Interfaz principal del simulador de misión lunar Artemis II.
 *
 * <p>Esta primera versión establece la estructura visual.
 * La integración con Orekit se realizará en el siguiente paso.</p>
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

    /**
     * Construye y muestra la interfaz.
     *
     * @param stage ventana principal
     */
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

    /**
     * Crea el encabezado de la aplicación.
     */
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

    /**
     * Crea la visualización espacial inicial.
     */
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

    /**
     * Crea un texto para identificar objetos espaciales.
     */
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

    /**
     * Crea el panel lateral con parámetros y telemetría.
     */
    private ScrollPane createControlPanel() {

        VBox panel = new VBox(12);

        panel.setPadding(
                new Insets(18)
        );

        panel.setPrefWidth(330);

        panel.setStyle(
                "-fx-background-color: #102030;"
        );

        Label parametersTitle =
                createSectionTitle(
                        "PARÁMETROS DE MISIÓN"
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
                parametersTitle,
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
                createSectionTitle("TELEMETRÍA")
        );

        timeValue = createTelemetryValue("0.000 h");
        earthAltitudeValue =
                createTelemetryValue("185.000 km");
        moonDistanceValue =
                createTelemetryValue("-- km");
        speedValue =
                createTelemetryValue("-- km/s");

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

    /**
     * Crea los botones principales.
     */
    private HBox createActionButtons() {

        Button executeButton =
                new Button("Ejecutar simulación");

        Button resetButton =
                new Button("Reiniciar");

        executeButton.setStyle(
                "-fx-background-color: #1976a8;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-weight: bold;"
        );

        resetButton.setStyle(
                "-fx-background-color: #374b5c;"
                        + "-fx-text-fill: white;"
        );

        executeButton.setOnAction(event ->
                statusLabel.setText(
                        "Interfaz lista. "
                                + "Falta conectar el motor Orekit."
                )
        );

        resetButton.setOnAction(event ->
                resetInterface()
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

    /**
     * Crea los controles de reproducción.
     */
    private VBox createPlaybackControls() {

        Label title =
                createSectionTitle("REPRODUCCIÓN");

        Button playButton =
                new Button("Reproducir");

        Button pauseButton =
                new Button("Pausar");

        Slider speedSlider =
                new Slider(1, 1000, 1);

        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(250);
        speedSlider.setBlockIncrement(10);

        Label speedLabel =
                new Label("Escala de tiempo: 1x");

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

    /**
     * Crea la barra inferior de estado.
     */
    private HBox createStatusBar() {

        statusLabel = new Label(
                "Sistema preparado."
        );

        statusLabel.setTextFill(
                Color.web("#b9d8ec")
        );

        HBox statusBar =
                new HBox(statusLabel);

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

    /**
     * Restablece los valores visuales iniciales.
     */
    private void resetInterface() {

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

        timeValue.setText("0.000 h");
        earthAltitudeValue.setText("185.000 km");
        moonDistanceValue.setText("-- km");
        speedValue.setText("-- km/s");

        spacecraft.setCenterX(300);
        spacecraft.setCenterY(310);

        statusLabel.setText(
                "Interfaz reiniciada."
        );
    }

    /**
     * Inicia JavaFX.
     *
     * @param args argumentos de consola
     */
    public static void main(String[] args) {
        launch(args);
    }
}
