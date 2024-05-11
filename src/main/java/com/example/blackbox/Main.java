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
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.util.Duration;
import Controller.GameState;

import java.util.*;

import static Controller.GameState.calcScore;

public class Main extends Application {

    public static List<Point2D> atomPositions;
    public static boolean isRandomGame = false;
    Group gridGroup = new Group();
    Group gridGroup2 = new Group();
    Group gridGroup3 = new Group();
    public static boolean isExperimenter;
    BlackBoxBoard sBoard = new BlackBoxBoard(); //setter board instance

    //only translation instance for setter board as experimenter board does not need atom translation with the logic.
    Translation translation = new Translation(sBoard); //experimenter eBoard's atoms has direct comparisons with 2D Object ArrayLists from the UI only.
    public static boolean isEBoard = false;
    //----------constants

    public static final Color BACKGROUND_COLOR = Color.DARKGOLDENROD;



    private GameState gameState;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        RayNode.initializeNodes();

//------------------------start screen

        Button startButton = new Button("Start New Game"); //adding a start button and styling using CSS.
        startButton.setStyle("-fx-font-family: 'Droid Sans Mono'; " +
                "-fx-font-size: 50px; " +
                "-fx-padding: 20 70; " +
                "-fx-font-weight: 800; " +
                "-fx-font-style: oblique; " +
                "-fx-background-radius: 28; " +
                "-fx-background-color:#ffc967; " +
                "-fx-border-color: #bd7a00; " +
                "-fx-border-width: 5; " +
                "-fx-border-radius: 25;");

        Label welcomeMessage = new Label("Welcome to BlackBox+!");
        welcomeMessage.setStyle("-fx-font-family: 'Droid Sans Mono';" +
                "-fx-font-size: 40px; " +
                "-fx-text-fill: #ffc967; " +
                "-fx-padding: 20 70; " +
                "-fx-font-weight: 800; " +
                "-fx-font-style: oblique; ");

        //adding logo to start screen
        //adding the image from path
        Image logo = new Image("/maveboxlogo.png");
        // creating the image view
        ImageView imageView = new ImageView(logo);
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);//preserving ratio of imageview



        VBox layout = new VBox(20);
        layout.getChildren().addAll(imageView, welcomeMessage, startButton);
        layout.setAlignment(Pos.CENTER);
        VBox.setMargin(welcomeMessage, new Insets(50, 100, 40, 100));
        VBox.setMargin(startButton, new Insets(60, 100, 40, 100));
        Scene startScene = new Scene(layout, 300, 250);
        layout.setStyle("-fx-background-color: black;");


        startButton.setOnAction(e -> { //event handler for start button click
            Scene mainGameScene = createMainGameScene(primaryStage);
            showPlayerChoiceScreen(primaryStage, mainGameScene);// the player choice screen is added upon click
            primaryStage.setFullScreen(true); //setting the stage to fullscreen
        });

        primaryStage.setTitle("BlackBox+");
        primaryStage.setScene(startScene); //adding the start screen to primary stage.
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
        isExperimenter = false;
        generateReadyButton(buttonsStackPane, primaryStage);
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
        isExperimenter = true;
        generateReadyButton(buttonsStackPane2, primaryStage);
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


        //anchorpane for the right container in borderpane to align the button.
        AnchorPane rightContainer = new AnchorPane();
        rightContainer.getChildren().add(fireRayButton);
        AnchorPane.setBottomAnchor(fireRayButton, 300.0);
        AnchorPane.setRightAnchor(fireRayButton, 85.0);
        AnchorPane.setRightAnchor(rightContainer, 150.0);
        BorderPane.setMargin(rightContainer, new Insets(0, 0, 0, 120));


        if (atomPositions == null) {
            sBoard.placeSetterAtoms(BlackBoxBoard.randomAtoms);
            List<Point2D> temp = Translation.get2DAtomMatch(BlackBoxBoard.randomAtoms);
            atomPositions = new ArrayList<>();
            atomPositions.addAll(temp);
            System.out.println(atomPositions);
            isRandomGame = true;
        }

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
        experimenterButton.setStyle("-fx-background-color: #1a1a1a; -fx-text-fill: #bd7a00; -fx-font-size: 20; -fx-shape: 'M0,50 a50,50 0 1,0 100,0 a50,50 0 1,0 -100,0'; -fx-min-width: 150; -fx-min-height: 150; -fx-max-width: 150; -fx-max-height: 150; -fx-font-weight: bold");


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
        Label chosenAtomsLabel = new Label("You have chosen your atoms! ");
        chosenAtomsLabel.setStyle("-fx-font-family: 'Droid Sans Mono'; -fx-font-weight: bold; -fx-font-size: 60;-fx-text-fill: #ffc967");
        Label experimenterTurnLabel = new Label("it is now the experimenter's turn...");
        experimenterTurnLabel.setStyle("-fx-font-family: 'Droid Sans Mono'; -fx-font-weight: bold; -fx-font-size: 50;-fx-text-fill: #ffc967");


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

    private Label atomReveal() { //generating the top text for the experimenter screen.
        Label exptTurn = new Label("CORRECT ATOM PLACEMENT");
        //css inline styling
        exptTurn.setStyle("-fx-font-family:'Droid Sans Mono'; -fx-font-size: 60px; -fx-font-weight: bold; -fx-text-fill: #ffc967");
        //borderpane margin and alignment
        exptTurn.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        BorderPane.setMargin(exptTurn, new Insets(100, 0, 0, 0));

        return exptTurn;
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

    private VBox generateRayMarkerKey() {
        //white circle to represent reflected ray marker
        Circle whiteCircle = new Circle(10, Color.WHITE);
        whiteCircle.setStroke(Color.BLACK);
        Label whiteLabel = new Label("REFLECTED");
        whiteLabel.setStyle("-fx-font-family: 'Droid Sans Mono'; -fx-font-size: 16px; -fx-text-fill: #ffc967;");
        HBox whiteKey = new HBox(5, whiteCircle, whiteLabel);

        //black circle to reflect absorbed ray marker
        Circle blackCircle = new Circle(10, Color.BLACK);
        Label blackLabel = new Label("DIRECT HIT/ABSORBED");
        blackLabel.setStyle("-fx-font-family: 'Droid Sans Mono'; -fx-font-size: 16px; -fx-text-fill: #fac569;");
        HBox blackKey = new HBox(5, blackCircle, blackLabel);

        //colored pair to reflect two normal ray markers.
        Circle colorCircle = new Circle(10, Color.PINK);
        Label colorLabel = new Label("x2  ENTRY & EXIT POINTS");
        colorLabel.setStyle("-fx-font-family: 'Droid Sans Mono'; -fx-font-size: 16px; -fx-text-fill: #ffc967;");
        HBox colorKey = new HBox(5, colorCircle, colorLabel);
        Image image = new Image("/multicolor.jpeg");
        colorCircle.setFill(new ImagePattern(image));

        Circle atoms2Guess = new Circle(10, Color.RED);
        whiteCircle.setStroke(Color.BLACK);
        Label atomLabel;

        atomLabel = new Label("Atoms to Guess: " + atomPositions.size());
        atomLabel.setStyle("-fx-font-family: 'Droid Sans Mono'; -fx-font-size: 16px; -fx-text-fill: #ffc967;");
        HBox atomKey = new HBox(5, atoms2Guess, atomLabel);

        //combining the hboxes into a vbox to align vertically
        VBox keyBox = new VBox(20, whiteKey, blackKey, colorKey, atomKey);
        keyBox.setStyle("-fx-border-color: black; -fx-border-width: 15; -fx-padding: 8px");
        keyBox.setBackground(Background.fill(Color.rgb(23, 23, 23)));
        keyBox.setAlignment(Pos.CENTER);

        return keyBox;
    }

    public void showResults(Stage primaryStage, Map<String, Integer> results) { //SCREEN TO DISPLAY RESULTS AND stats
        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);

        StackPane buttonsStackPane3 = new StackPane();
        generateShowAtomsButton(buttonsStackPane3, primaryStage);

        Label scoreLabel = new Label("Your score is: " + results.get("score"));
        scoreLabel.setStyle("-fx-font-family: 'Droid Sans Mono';-fx-text-fill: #ffc967; -fx-font-size: 40; -fx-font-weight: bold");
        scoreLabel.setTextFill(Color.WHITE);
        scoreLabel.setOpacity(0);
        scoreLabel.setTextAlignment(TextAlignment.CENTER);

        //stats label below the score label
        Label statsLabel = new Label(
                results.get("correctAtoms") + " atoms were correctly placed.\n" +
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

        root.getChildren().addAll(labelsBox, buttonsStackPane3);
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


    public void generateShowAtomsButton(StackPane buttonsStackPane, Stage primaryStage) {
        Button showAtoms = new Button("Show Atoms");

        showAtoms.setOnAction(actionEvent -> {
            showGameReveal(primaryStage);
        });

        showAtoms.setStyle("-fx-font-family: 'Droid Sans Mono'; -fx-font-size: 40px;-fx-padding: 10 30; -fx-font-weight: bold; -fx-background-color: #232323; -fx-text-fill: #ffc967");
        StackPane.setAlignment(showAtoms, Pos.BOTTOM_CENTER);
        StackPane.setMargin(showAtoms, new Insets(0.0, 0.0, 50.0, 0.0));
        buttonsStackPane.getChildren().add(showAtoms);
    }

    // Final screen to show the actual correct placement of atoms
    public void showGameReveal(Stage primaryStage) {

        AtomGenerator.resetAtomCount();//resetting atom count

        BorderPane root3 = new BorderPane();
        Scene atomRevealScene = new Scene(root3);
        StackPane centerStackPane3 = new StackPane();

        BorderPane.setMargin(centerStackPane3, new Insets(0, 0, 0, 100));

        HexCellGenerator.resetStartingPositions();

        HexCellGenerator.generateHexCells(gridGroup3);//adding hex cells to group.
        HexCellGenerator.printHexCellsAndCenters(); //TEST
        RayCircle.generateRayCircles(gridGroup3); //adding ray circles to same group.

        GameReveal.revealSetterAtoms(atomPositions, gridGroup3);

        centerStackPane3.getChildren().add(gridGroup3);

        Label atomReveal = atomReveal();
        //setting text to the centre in the root top container.
        VBox topContainer2 = new VBox();
        topContainer2.getChildren().add(atomReveal);
        VBox.setMargin(atomReveal, new Insets(50, 0, 0, 50));
        topContainer2.setAlignment(Pos.TOP_CENTER);

        root3.setTop(topContainer2);
        root3.setCenter(centerStackPane3);
        root3.setStyle("-fx-background-color: black;");

        primaryStage.setScene(atomRevealScene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }


    public void generateReadyButton(StackPane buttonsStackPane, Stage primaryStage) {
        Button ready = new Button("READY");
        //event handler for button click
        ready.setOnAction(event -> {
            if (!isRandomGame) {
                atomPositions = collectAtomPositions(gridGroup);//collecting the final atom positions (setter) in list of Point2D objects.
                System.out.println("atomPositions Positions:");
                for (Point2D position : atomPositions) {
                    System.out.println("X: " + position.getX() + ", Y: " + position.getY());
                }
            }
            List<Point2D> setterAtomPos = new ArrayList<>(atomPositions);
            if (!isExperimenter) {//max final atom check

                if (AtomGenerator.atomCount >= 4) {
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
                    dialogPane.setId("setterPane"); //custom id for setter alert to style differently in stylesheet.
                    readyButtonAlert.initOwner(primaryStage);
                    readyButtonAlert.setTitle("ATOMS : Warning");
                    readyButtonAlert.setHeaderText("🔴 x6  👾");
                    readyButtonAlert.setContentText("Please place exactly 4 to 6 atoms in the hex board.");
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
            } else {

                List<Point2D> atomPositionsExperimenter = collectAtomPositions(gridGroup2); //collecting experimenter final atom positions.
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
                    int atomCount = 0;
                    if (isRandomGame) {
                        atomCount = 6;
                    }
                    else {
                        atomCount = atomPositions.size();
                    }
                    readyButtonAlert2.setContentText("PLACE " + atomCount + " ATOMS STRATEGICALLY TO MAXIMISE YOUR SCORE. ANY MISSING OR EXTRA ATOMS WILL BE COUNTED AS INCORRECTLY PLACED ATOMS.");

                    //introducing button types continue and go back for the experimenter screen ready button alert.
                    ButtonType buttonTypeContinue = new ButtonType("REVEAL RESULTS →", ButtonBar.ButtonData.YES); //setting data to yes for continue
                    ButtonType buttonTypeGoBack = new ButtonType("← CONTINUE EXPERIMENTING", ButtonBar.ButtonData.NO);//setting data to no for go back
                    readyButtonAlert2.getButtonTypes().setAll(buttonTypeContinue, buttonTypeGoBack);//adding buttons to the alert

                    DialogPane dialogPane = readyButtonAlert2.getDialogPane();
                      dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());
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
                    Map<String, Integer> results = calcScore(atomPositions, atomPositionsExperimenter, sBoard);
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


