package com.example.blackbox;

// import all libraries needed
import Controller.Translation;
import Model.*;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.util.Duration;
import utils.ReadyButtonClickedListener;
import Controller.GameState;

import java.util.*;

import static Controller.GameState.calcScore;


public class Main extends Application {

    public static List<Point2D> atomPositions;
    public static List<Point2D> expPositions;
    Group gridGroup = new Group();
    Group gridGroup2 = new Group();
    BlackBoxBoard sBoard = new BlackBoxBoard(); //setter board instance
    BlackBoxBoard eBoard = new BlackBoxBoard(); //experimenter board instance

    //only translation instance for setter board as experimenter board does not need atom translation with the logic.
    Translation translation = new Translation(sBoard); //experimenter eBoard's atoms has direct comparisons with 2D Object ArrayLists from the UI only.
    public static boolean isEBoard = false;
    //----------constants

    public static final Color BACKGROUND_COLOR = Color.DARKGOLDENROD;
    public static final Color SECONDARY_COLOR = Color.DARKGRAY;


//    private Stage window;
    private GameState gameState;
    // private GameState gameState2;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        RayNode.initializeNodes();

//------------------------start screen

        Button startButton = new Button("Start New Game");
        startButton.setStyle("-fx-font-family: 'Arial'; " +
                "-fx-font-size: 40px; " +
                "-fx-padding: 20 70; " +
                "-fx-font-weight: 800; " +
                "-fx-font-style: oblique; " +
                "-fx-background-radius: 28; " +
                "-fx-background-color:#ffc967; " +
                "-fx-border-color: #bd7a00; " +
                "-fx-border-width: 5; " +
                "-fx-border-radius: 25;");

        Label welcomeMessage = new Label("Welcome to BlackBox+!");
        welcomeMessage.setStyle("-fx-font-family: 'Arial'; " +
                "-fx-font-size: 40px; " +
                "-fx-text-fill: #ffc967; " +
                "-fx-padding: 20 70; " +
                "-fx-font-weight: 800; " +
                "-fx-font-style: oblique; ");


        VBox layout = new VBox(20);
        layout.getChildren().addAll(welcomeMessage, startButton);
        layout.setAlignment(Pos.CENTER);
        VBox.setMargin(welcomeMessage, new Insets(50, 100, 40, 100));
        VBox.setMargin(startButton, new Insets(60, 100, 40, 100));
        Scene startScene = new Scene(layout, 300, 250);
        layout.setStyle("-fx-background-color: black;");

        startButton.setOnAction(e -> {
            Scene mainGameScene = createMainGameScene(primaryStage);
            showPlayerChoiceScreen(primaryStage, mainGameScene);
            primaryStage.setFullScreen(true);
        });

        primaryStage.setTitle("BlackBox+");
        primaryStage.setScene(startScene);
        primaryStage.setFullScreen(true);
        primaryStage.show();

    }

    private Scene createMainGameScene(Stage primaryStage) {

        BorderPane root = new BorderPane();
        Scene mainGameScene = new Scene(root);

        StackPane centerStackPane = new StackPane();

        // Generate hex cells and ray circles
        AtomGenerator.resetAtomCount(); //making sure atom count is reset to 0.
        HexCellGenerator.resetStartingPositions();
        HexCellGenerator.generateHexCells(gridGroup);
        RayCircle.generateRayCircles(gridGroup);

        // Generate game state and ready button
        gameState = new GameState();
        StackPane buttonsStackPane = new StackPane();
        generateReadyButton(buttonsStackPane, gameState, primaryStage, false);
        BorderPane.setMargin(buttonsStackPane, new Insets(0, 0, 50, 30));

        // Add grid group to center stack pane
        centerStackPane.getChildren().add(gridGroup);

        // Link maps for translation
        translation.linkMaps();

        // Generate top text and setter instructions
        Label turn = generateTopText();
        VBox topContainer = new VBox();
        topContainer.getChildren().add(turn);
        VBox.setMargin(turn, new Insets(50, 0, 0, 50));
        topContainer.setAlignment(Pos.TOP_CENTER);
        
        
        root.setTop(topContainer);
        root.setCenter(centerStackPane);
        root.setBottom(buttonsStackPane);
        root.setStyle("-fx-background-color: black;");

        return mainGameScene;
    }

    //this method handles displaying the main game screen where the experimenter is playing.
    public void showExperimenterScreen(Stage primaryStage) { // TODO: change to private
        AtomGenerator.resetAtomCount();//resetting atom count
        isEBoard = true;//setting the flag to true so ray circles can have click effect functionality..

        BorderPane root2 = new BorderPane(); //root is BorderPane layout as this is best suited as the base template for our Game UI.
        Scene experimenterScene = new Scene(root2);
        StackPane centerStackPane2 = new StackPane(); //centre container in root is StackPane for main game stage hexagon grid.
        StackPane buttonsStackPane2 = new StackPane();
        //group for hex cells so grid can be manipulated as a unit (for layout purposes).

        BorderPane.setMargin(centerStackPane2, new Insets(0, 0, 0, 100));

        HexCellGenerator.resetStartingPositions(); /*resetting here is crucial as it directly affects the layout if the static variables,
        (the starting x and y points) for the hex cells and ray circles remain shifted from the last call to grid generation.*/

        HexCellGenerator.generateHexCells(gridGroup2);//adding hex cells to group.
        HexCellGenerator.printHexCellsAndCenters(); //TEST
        RayCircle.generateRayCircles(gridGroup2); //adding ray circles to same group.

        gameState = new GameState();
        generateReadyButton(buttonsStackPane2, gameState, primaryStage, true);
        Button fireRayButton = createFireRayButton();
        BorderPane.setMargin(buttonsStackPane2, new Insets(0, 0, 50, 30));
        buttonsStackPane2.getChildren().add(fireRayButton);

        centerStackPane2.getChildren().add(gridGroup2);

        translation.linkMaps();


        Label turn2 = generateTopExptText();
        //setting text to the centre in the root top container.
        VBox topContainer2 = new VBox();
        topContainer2.getChildren().add(turn2);
        VBox.setMargin(turn2, new Insets(50, 0, 0, 50));
        topContainer2.setAlignment(Pos.TOP_CENTER);


        AnchorPane rightContainer = new AnchorPane();
        rightContainer.getChildren().add(fireRayButton);
        AnchorPane.setBottomAnchor(fireRayButton, 300.0);
        AnchorPane.setRightAnchor(fireRayButton, 85.0);
        AnchorPane.setRightAnchor(rightContainer, 150.0);
        BorderPane.setMargin(rightContainer, new Insets(0, 0, 0, 120));

//        rightContainer2.setPrefSize(150, 150);
        VBox rayMarkerKey = generateRayMarkerKey();
        BorderPane.setAlignment(rayMarkerKey, Pos.TOP_LEFT);

        root2.setLeft(rayMarkerKey);
        root2.setTop(topContainer2);
        root2.setRight(rightContainer);
        root2.setCenter(centerStackPane2);
        root2.setBottom(buttonsStackPane2);
        root2.setStyle("-fx-background-color: black;");

        primaryStage.setScene(experimenterScene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    private void showPlayerChoiceScreen(Stage primaryStage, Scene mainGameScene) {


        Button setterButton = new Button("setter");
        //setterButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 20; -fx-shape: 'M0,50 a50,50 0 1,0 100,0 a50,50 0 1,0 -100,0'; -fx-min-width: 100; -fx-min-height: 100; -fx-max-width: 100; -fx-max-height: 100;");

        Button experimenterButton = new Button("experimenter");
        //experimenterButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 20; -fx-shape: 'M0,50 a50,50 0 1,0 100,0 a50,50 0 1,0 -100,0'; -fx-min-width: 100; -fx-min-height: 100; -fx-max-width: 100; -fx-max-height: 100;")

        setterButton.setStyle("-fx-background-color: #bd7a00; -fx-text-fill: #1a1a1a; -fx-font-size: 20; -fx-shape: 'M0,50 a50,50 0 1,0 100,0 a50,50 0 1,0 -100,0'; -fx-min-width: 150; -fx-min-height: 150; -fx-max-width: 150; -fx-max-height: 150; -fx-font-weight: bold");
        experimenterButton.setStyle("-fx-background-color: #1a1a1a; -fx-text-fill: #bd7a00; -fx-font-size: 20; -fx-shape: 'M0,50 a50,50 0 1,0 100,0 a50,50 0 1,0 -100,0'; -fx-min-width: 150; -fx-min-height: 150; -fx-max-width: 150; -fx-max-height: 150; -fx-font-weight: bold");;


        HBox hbox = new HBox(50);
        hbox.setAlignment(Pos.CENTER);
        Label selectRole = new Label("SELECT A ROLE: ");
        selectRole.setStyle("-fx-font-family: 'Droid Sans Mono';-fx-text-fill: #ffc967; -fx-font-size: 30px; -fx-font-weight: bold");
        hbox.getChildren().addAll(selectRole, setterButton, experimenterButton);


        Scene playerChoiceScene = new Scene(hbox, primaryStage.getWidth(), primaryStage.getHeight()); // Use the primary stage's width and height for full screen
        hbox.setStyle("-fx-background-color: black;");

        setterButton.setOnAction(e -> {
            primaryStage.setScene(mainGameScene);
            primaryStage.setFullScreen(true); //change to the main game scene
        });

        //action event for experimenter button
        experimenterButton.setOnAction(e -> {
            isEBoard = true;
            showExperimenterScreen(primaryStage);
            primaryStage.setFullScreen(true);
        });


        primaryStage.setScene(playerChoiceScene);
        primaryStage.setFullScreen(true);
    }


    //this method handles displaying the intermediate screen between the setter finishing their turn and the experimenter starting their turn.
    private void showContinueToExperimenterScreen(Stage primaryStage) {
        Label chosenAtomsLabel = new Label("You have chosen your atoms!");
        chosenAtomsLabel.setStyle("-fx-font-family: 'Droid Sans Mono'; -fx-font-weight: bold; -fx-font-size: 60;-fx-text-fill: #ffc967");
//        chosenAtomsLabel.setTextFill(Color.WHITE);

        Label experimenterTurnLabel = new Label("it is now the experimenter's turn...");
        experimenterTurnLabel.setStyle("-fx-font-family: 'Droid Sans Mono'; -fx-font-weight: bold; -fx-font-size: 50;-fx-text-fill: #ffc967");
//        experimenterTurnLabel.setTextFill(Color.WHITE);

        Button continueButton = new Button("continue to experimenter");
        //inline css styling for the button
        continueButton.setStyle("-fx-font-family: 'Droid Sans Mono'; -fx-font-weight: bold; -fx-font-size: 50;-fx-text-fill: #bd7a00;-fx-background-color:#1a1a1a;-fx-background-radius: 10;-fx-border-radius: 10;-fx-border-color: #bd7a00;");
        continueButton.setOnAction(event -> {
            showExperimenterScreen(primaryStage);
        });

        Region spacer = new Region(); //adding a region between the text in the vbox and the button for styling
        spacer.setPrefHeight(40);

        VBox vbox = new VBox(10, chosenAtomsLabel, spacer, experimenterTurnLabel, continueButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(30);


        Scene experimenterTurnScene = new Scene(vbox, primaryStage.getWidth(), primaryStage.getHeight());
        vbox.setStyle("-fx-background-color: black;");

        primaryStage.setScene(experimenterTurnScene);
        primaryStage.setFullScreen(true);
    }



    private Label generateTopText() {
        Label playerTurn = new Label("SETTER'S TURN.");
        //css inline styling
        playerTurn.setStyle("-fx-font-family: 'Droid Sans Mono'; -fx-font-size: 60px; -fx-font-weight: bold; -fx-text-fill: #ffc967");
        //borderpane margin and alignment
        playerTurn.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        BorderPane.setMargin(playerTurn, new Insets(100, 0, 0, 0));

        return playerTurn;
    }
    private Label generateTopExptText() { //generating the top text for the experimenter screen.
        Label exptTurn = new Label("EXPERIMENTER'S TURN.");
        //css inline styling
        exptTurn.setStyle("-fx-font-family:'Droid Sans Mono'; -fx-font-size: 60px; -fx-font-weight: bold; -fx-text-fill: #ffc967");
        //borderpane margin and alignment
        exptTurn.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        BorderPane.setMargin(exptTurn, new Insets(100, 0, 0, 0));

        return exptTurn;
    }

    private Label generateSetterInstructions() {
        Label instructions = new Label("Please place 6 atoms inside your chosen hex cells. " +
                "After you are finished, click the Ready button below the board.");
        instructions.setStyle("-fx-font-family: 'Droid Sans Mono'; -fx-font-size: 16px; -fx-font-weight: bold");
        instructions.setWrapText(true);
        instructions.setMaxWidth(400);
        instructions.setTextAlignment(TextAlignment.CENTER);
        return instructions;
    }

    private Button createFireRayButton() {
        Button fireRayButton = new Button("FIRE RAY");
        fireRayButton.setStyle("-fx-font-family: 'Droid Sans Mono'; -fx-font-size: 18px; " +
                "-fx-background-color: #F6DE90; -fx-text-fill: black; -fx-border-color: black; " +
                "-fx-border-width: 4px; -fx-font-weight: bold; -fx-border-radius: 2px; -fx-padding: 14px;");

        fireRayButton.setAlignment(Pos.BASELINE_CENTER);
        Tooltip tooltip = new Tooltip("ℹ Click on this button to fire a ray and refer to the ray marker key to analyse the result.");
        tooltip.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-opacity: 0.7;");
        tooltip.setMaxWidth(150);
        tooltip.setWrapText(true);
        tooltip.setShowDelay(Duration.millis(170));
        Tooltip.install(fireRayButton, tooltip);


        fireRayButton.setOnAction(event -> {
            int rayNumber = RayCircle.getCurrentlyClickedRayNumber();
            if (rayNumber > 0 && rayNumber < 55) {
                System.out.println("Firing ray: " + rayNumber);
                Ray ray = new Ray(sBoard, rayNumber);
                int[] nodeNumbers = Ray.printRayInfo(ray);

                RayCircle entryRayCircle = RayCircle.findRayCircleByNumber(nodeNumbers[0]);
                RayCircle exitRayCircle = RayCircle.findRayCircleByNumber(nodeNumbers[1]);


                //set random color to entry/exit pair of ray circle for ray markers
                if (entryRayCircle != null && exitRayCircle != null && !(ray.isRayReversed())) {
                    Color currentColorPair = RayCircle.getNextColor(); //get the next color for the pair
                    entryRayCircle.setPermanentColor(currentColorPair);  //set same color pair for ray markers
                    exitRayCircle.setPermanentColor(currentColorPair);
                } else if(ray.isRayReversed()) {
                    entryRayCircle.setPermanentColor(Color.WHITE);
                    entryRayCircle.setTextColor(Color.BLACK); //setting number text to black so it's easier to see against white background.
                }
                else if(ray.isAbsorbed() || nodeNumbers[1] == -1) {
                    entryRayCircle.setPermanentColor(Color.BLACK);
                }
                else {
                    System.out.println("No ray selected!");//testing if ray is either unfounded or reversed.
                }

            }
        });
        return fireRayButton;
    }


//    private Text generateExperimenterInstructions() {
//        Text instructionsExpt = new Text("YOU CAN CHOOSE A RAY BY SELECTING ON IT. THE CHOSEN RAY CAN BE CHANGED BY SIMPLY CLICKING ON THE RAY YOU WOULD LIKE TO CHOOSE INSTEAD." +
//                "WHEN YOU WANT TO FIRE THE RAY, CLICK THE [FIRE RAY] BUTTON BELOW. THE RESULTS OF YOUR RAY WILL BE SHOWN IN RAY MARKERS, INDICATED BY THE CHANGING COLOUR OF THE RAY NODES.");
//        instructionsExpt.setStyle("-fx-font-family: 'Droid Sans Mono'; -fx-font-size: 16px;");
//        instructionsExpt.setWrappingWidth(200);
//        instructionsExpt.maxWidth(150);
//        instructionsExpt.setTextAlignment(TextAlignment.CENTER);
//        StackPane exptpane = new StackPane(instructionsExpt);
//        exptpane.setPadding(new Insets(50,50,50,50));
//        instructionsExpt.setFill(SECONDARY_COLOR);
//        return instructionsExpt;
//    }

//    public void start(Stage primaryStage) {
//
//        Button infoButton = new Button("ℹ");
//        infoButton.setStyle("-fx-font-size: 18;");
//
//
//        Label infoLabel = new Label("YOU CAN CHOOSE A RAY BY SELECTING ON IT. THE CHOSEN RAY CAN BE CHANGED BY SIMPLY CLICKING ON THE RAY YOU WOULD LIKE TO CHOOSE INSTEAD." +
//                "WHEN YOU WANT TO FIRE THE RAY, CLICK THE [FIRE RAY] BUTTON BELOW. THE RESULTS OF YOUR RAY WILL BE SHOWN IN RAY MARKERS, INDICATED BY THE CHANGING COLOUR OF THE RAY NODE");
//        infoLabel.setStyle("-fx-font-family: 'Droid Sans Mono'; -fx-font-size: 16px;");
//        VBox popupContent = new VBox(10);
//        popupContent.getChildren().add(infoLabel);
//        popupContent.setAlignment(Pos.CENTER);
//
//        Stage popupStage = new Stage();
//        popupStage.setScene(new Scene(popupLayout, 200, 100));
//
//
//        infoButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                popupStage.show();
//            }
//        });
    private VBox generateRayMarkerKey() {
        //white circle to represent reflected ray marker
        Circle whiteCircle = new Circle(10, Color.WHITE);
        whiteCircle.setStroke(Color.BLACK);
        Label whiteLabel = new Label("REFLECTED");
        whiteLabel.setStyle("-fx-font-family: 'Droid Sans Mono'; -fx-font-size: 16px; -fx-text-fill: black;");
        HBox whiteKey = new HBox(5, whiteCircle, whiteLabel);

        //black circle to reflect absorbed ray marker
        Circle blackCircle = new Circle(10, Color.BLACK);
        Label blackLabel = new Label("DIRECT HIT/ABSORBED");
        blackLabel.setStyle("-fx-font-family: 'Droid Sans Mono'; -fx-font-size: 16px; -fx-text-fill: black;");
        HBox blackKey = new HBox(5, blackCircle, blackLabel);

        //colored pair to reflect two normal ray markers.
        Circle colorCircle = new Circle(10, Color.PINK);
        Label colorLabel = new Label("x2  ENTRY & EXIT POINTS");
        colorLabel.setStyle("-fx-font-family: 'Droid Sans Mono'; -fx-font-size: 16px; -fx-text-fill: black;");
        HBox colorKey = new HBox(5, colorCircle, colorLabel);
        Image image = new Image("file:src/main/resources/multicolor.jpeg");
        colorCircle.setFill(new ImagePattern(image));

        //combining the hboxes into a vbox to align vertically
        VBox keyBox = new VBox(8, whiteKey, blackKey, colorKey);
        keyBox.setStyle("-fx-border-color: black; -fx-border-width: 15; -fx-padding: 8px");
        keyBox.setBackground(Background.fill(SECONDARY_COLOR));
        keyBox.setAlignment(Pos.CENTER);

        return keyBox;
    }

    public void showResults(Stage primaryStage, Map<String, Integer> results) { //SCREEN TO DISPLAY RESULTS AND stats
        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);

        Label scoreLabel = new Label("Your score is: " + results.get("score"));
        scoreLabel.setStyle("-fx-font-family: 'Droid Sans Mono';-fx-text-fill: #ffc967; -fx-font-size: 40; -fx-font-weight: bold");
        scoreLabel.setTextFill(Color.WHITE);
        scoreLabel.setOpacity(0);
        scoreLabel.setTextAlignment(TextAlignment.CENTER);

        //stats label below the score label
        Label statsLabel = new Label(
                results.get("incorrectAtoms") + " atoms were incorrectly placed.\n" +
                        "Total rays fired: " + results.get("rayCount") + "\n" +
                        "Total ray markers: " + results.get("rayMarkers")
        );
        statsLabel.setStyle("-fx-font-family: 'Droid Sans Mono';-fx-text-fill: #ffc967; -fx-font-size: 30; -fx-font-weight: bold");
        statsLabel.setTextFill(Color.WHITE);
        statsLabel.setOpacity(0);
        statsLabel.setTextAlignment(TextAlignment.CENTER);

        //vertical layout for the labels
        VBox labelsBox = new VBox(10, scoreLabel, statsLabel);
        labelsBox.setAlignment(Pos.CENTER);

        root.getChildren().add(labelsBox);
        root.setStyle("-fx-background-color: black;");

        Scene resultScene = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());
        primaryStage.setScene(resultScene);
        primaryStage.setFullScreen(true);
        primaryStage.show();

        //creating fade transition with sequential fade for stats display
        FadeTransition fadeScore = new FadeTransition(Duration.seconds(3), scoreLabel);
        fadeScore.setFromValue(0);
        fadeScore.setToValue(1);

        FadeTransition fadeStats = new FadeTransition(Duration.seconds(1.5), statsLabel);
        fadeStats.setFromValue(0);
        fadeStats.setToValue(1);
        fadeStats.setDelay(Duration.seconds(1)); //delay for fade

        SequentialTransition sequentialTransition = new SequentialTransition(fadeScore, fadeStats);
        sequentialTransition.play();
    }

    private boolean isReadyClicked = false;


    public void generateReadyButton(StackPane buttonsStackPane, ReadyButtonClickedListener listener, Stage primaryStage, boolean isExperimenter) {
        Button ready = new Button("READY");
        //event handler for button click
        ready.setOnAction(event -> {
            List<Point2D> atomPositions = collectAtomPositions(gridGroup);//collecting the final atom positions (setter) in list of Point2D objects.
            System.out.println("atomPositions Positions:");
            for (Point2D position : atomPositions) {
                System.out.println("X: " + position.getX() + ", Y: " + position.getY());
            }
            List<Point2D> setterAtomPos = new ArrayList<>(atomPositions);
            if (isExperimenter == false) {//max final atom check
//                List<Point2D> atomPositions = collectAtomPositions();
                if (AtomGenerator.atomCount == 6) {
                    List<BlackBoxBoard.Point3D> setterAtomList = translation.get3DAtomMatch(atomPositions);
                    sBoard.placeSetterAtoms(setterAtomList);
                    sBoard.printBoard();

                    //print the positions
                    System.out.println("Atom Positions:");
                    for (Point2D position : atomPositions) {
                        System.out.println(position.toString());
                    }
                    gameState.setSetterAtomPositions(atomPositions);
                    gameState.onReadyClicked();
                    showContinueToExperimenterScreen(primaryStage);
                } else {

                    //alert popup to remind setter to place exactly 6 atoms.
                    Alert readyButtonAlert = new Alert(Alert.AlertType.WARNING);
                    DialogPane dialogPane = readyButtonAlert.getDialogPane();
                    readyButtonAlert.initOwner(primaryStage);
                    readyButtonAlert.setTitle("ATOMS : Warning");
                    readyButtonAlert.setHeaderText("🔴 x6  👾");
                    readyButtonAlert.setContentText("Please place exactly 6 atoms in the hex board.");
                    dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());
                    dialogPane.lookupAll("background");
                    readyButtonAlert.getDialogPane().setMinWidth(520);
                    readyButtonAlert.getDialogPane().setMinHeight(200);
                    readyButtonAlert.getDialogPane().setPrefWidth(600);
                    readyButtonAlert.getDialogPane().setPrefHeight(160);
                    Stage opacity = (Stage) readyButtonAlert.getDialogPane().getScene().getWindow();
                    opacity.setOpacity(0.9);

                    readyButtonAlert.showAndWait();
                }
            } else if (isExperimenter) {

                //eBoard.printBoard();
                List<Point2D> atomPositionsExperimenter = collectAtomPositions(gridGroup2); //collecting experimenter final atom positions.
                //gameState.setExpAtomPositions(atomPositionsExperimenter);
                System.out.println("Experimenter Atom Positions:");
                for (Point2D position : atomPositionsExperimenter) {
                    System.out.println("X: " + position.getX() + ", Y: " + position.getY());
                }
                System.out.println("SetterPOS Positions:");
                for (Point2D position : setterAtomPos) {
                    System.out.println("X: " + position.getX() + ", Y: " + position.getY());
                }


                    Alert readyButtonAlert2 = new Alert(Alert.AlertType.WARNING);
                    readyButtonAlert2.initOwner(primaryStage);
                    readyButtonAlert2.setTitle("FINISHED GUESSING?");
                    readyButtonAlert2.setHeaderText("YOU ARE ABOUT TO FINISH EXPERIMENTING AND REVEAL THE RESULTS...🤔");
                    readyButtonAlert2.setContentText("PLACE 6 ATOMS STRATEGICALLY TO MAXIMISE YOUR SCORE. PLACING LESS THAN 6 ATOMS MEANS ANY MISSING ATOMS WILL BE COUNTED AS INCORRECTLY PLACED ATOMS.");

                    //introducing button types continue and go back for the experimenter screen ready button alert.
                    ButtonType buttonTypeContinue = new ButtonType("REVEAL RESULTS →", ButtonBar.ButtonData.YES); //setting data to yes for continue
                    ButtonType buttonTypeGoBack = new ButtonType("← CONTINUE EXPERIMENTING", ButtonBar.ButtonData.NO);//setting data to no for go back
                    readyButtonAlert2.getButtonTypes().setAll(buttonTypeContinue, buttonTypeGoBack);//adding buttons to the alert

                    DialogPane dialogPane = readyButtonAlert2.getDialogPane();
//                    dialogPane.getStylesheets().add(getClass().getClassLoader().getResource("style.css").toExternalForm());
                      dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());
//                    dialogPane.getStylesheets().add(getClass().getResource("com/example/blackbox/style.css").toExternalForm());
                    dialogPane.lookupButton(buttonTypeContinue).getStyleClass().add("continue-button");
                    dialogPane.lookupButton(buttonTypeGoBack).getStyleClass().add("go-back-button");
                    dialogPane.lookupAll("background");

                    readyButtonAlert2.getDialogPane().setMinWidth(520);
                    readyButtonAlert2.getDialogPane().setMinHeight(200);
                    readyButtonAlert2.getDialogPane().setPrefWidth(600);
                    readyButtonAlert2.getDialogPane().setPrefHeight(160);

                    Stage opacity = (Stage) readyButtonAlert2.getDialogPane().getScene().getWindow();
                    opacity.setOpacity(0.9);

                    Optional<ButtonType> userReadyResult = readyButtonAlert2.showAndWait();
                if (userReadyResult.isPresent() && userReadyResult.get() == buttonTypeContinue) {
                    //user chose "Reveal Results", continue to the showResults screen
                    Map<String, Integer> results = calcScore(setterAtomPos, atomPositionsExperimenter, sBoard);
                    showResults(primaryStage, results);
                    gameState.onReadyClicked();
                } else {
                    //user chose "Continue Experimenting" or closed the alert, just close the dialog
                    readyButtonAlert2.close();
                }


            }

    });
        //some inline CSS for button styling.
        ready.setStyle("-fx-font-family: 'Droid Sans Mono'; -fx-font-size: 20px;-fx-padding: 10 30; -fx-font-weight: bold; -fx-background-color: #232323; -fx-text-fill: #ffc967");
        StackPane.setAlignment(ready, Pos.BOTTOM_CENTER);
        //stack pane margin and alignments.
//        StackPane.setMargin(ready, new Insets(5, 10, 60, 20));
        buttonsStackPane.getChildren().add(ready);
    }




    private List<Point2D> collectAtomPositions(Group grid) {
        List<Point2D> positions = new ArrayList<>();
        for (Node node : grid.getChildren()) {
            if (node instanceof Circle && "atom".equals(node.getUserData())) { //making sure only atom Circle instances are referenced.
                Circle atom = (Circle) node;
                Point2D center = new Point2D(atom.getLayoutX(), atom.getLayoutY());
                positions.add(center);
            }
        }
        return positions;
    }



}


