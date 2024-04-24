
package com.example.blackbox;

// import all libraries needed
import Controller.Translation;
import Model.BlackBoxBoard;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import utils.ReadyButtonClickedListener;
import Controller.GameState;

import java.util.ArrayList;
import java.util.List;


public class Main extends Application {

    public static List<Point2D> atomPositions;
    public static List<Point2D> expPositions;
    Group gridGroup = new Group();
    Group gridGroup2 = new Group();
    BlackBoxBoard sBoard = new BlackBoxBoard(); //setter board instance
    BlackBoxBoard eBoard = new BlackBoxBoard(); //experimenter board instance
    Translation translation = new Translation(sBoard);

    Translation etranslation = new Translation(eBoard);
    private Stage window;
    private GameState gameState;
    private GameState gameState2;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {


//------------------------start screen

        Button startButton = new Button("Start New Game");
        startButton.setStyle("-fx-font-family: 'Arial'; " +
                "-fx-font-size: 40px; " +
                "-fx-padding: 20 70; " +
                "-fx-font-weight: 800; " +
                "-fx-font-style: oblique; " +
                "-fx-background-radius: 28; " +
                "-fx-background-color:#C3ECD0; " +
                "-fx-border-color: #14933C; " +
                "-fx-border-width: 5; " +
                "-fx-border-radius: 25;");

        Label welcomeMessage = new Label("Welcome to BlackBox+!");
        welcomeMessage.setStyle("-fx-font-family: 'Arial'; " +
                "-fx-font-size: 40px; " +
                "-fx-text-fill: lightgrey; " +
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
        StackPane buttonsStackPane = new StackPane();

        //group for hex cells so grid can be manipulated as a unit (for layout purposes).

//        centerStackPane.setMinWidth(Region.USE_PREF_SIZE);
//        centerStackPane.setMaxWidth(Region.USE_COMPUTED_SIZE);
        BorderPane.setMargin(centerStackPane, new Insets(0, 0, 0, 100));

        HexCellGenerator.resetStartingPositions();//resetting the static variables between grid generation calls.

        HexCellGenerator.generateHexCells(gridGroup);//adding hex cells to group.
        HexCellGenerator.printHexCellsAndCenters(); //TEST
        RayCircle.generateRayCircles(gridGroup); //adding ray circles to same group.

        gameState = new GameState();
        generateReadyButton(buttonsStackPane, gameState, primaryStage);
        BorderPane.setMargin(buttonsStackPane, new Insets(0, 0, 50, 30));

        centerStackPane.getChildren().add(gridGroup);

        translation.linkMaps();


        Label turn = generateTopText();
        //setting text to the centre in the root top container.
        VBox topContainer = new VBox();
        topContainer.getChildren().add(turn);
        VBox.setMargin(turn, new Insets(50, 0, 0, 50));
        topContainer.setAlignment(Pos.TOP_CENTER);

        Label setterInstructions = generateSetterInstructions();
        VBox rightContainer = new VBox();
        rightContainer.getChildren().add(setterInstructions);
        rightContainer.setAlignment(Pos.TOP_RIGHT);
        rightContainer.setMaxWidth(500);



        root.setTop(topContainer);
        root.setRight(rightContainer);
        root.setCenter(centerStackPane);
        root.setBottom(buttonsStackPane);
        root.setStyle("-fx-background-color: black;");

        return mainGameScene;
    }

    private void showPlayerChoiceScreen(Stage primaryStage, Scene mainGameScene) {
        //label for the name entry prompt
        Label nameLabel = new Label("Enter your name:"); //unecessary for time being. - personal choice.
        nameLabel.setTextFill(Color.WHITE);

//        text field for the name entry
        TextField nameField = new TextField();
        nameField.setMaxWidth(200);


        Button setterButton = new Button("setter");
        setterButton.setStyle("-fx-background-color: blue; -fx-text-fill: white; -fx-font-size: 20; -fx-shape: 'M0,50 a50,50 0 1,0 100,0 a50,50 0 1,0 -100,0'; -fx-min-width: 100; -fx-min-height: 100; -fx-max-width: 100; -fx-max-height: 100;");

        Button experimenterButton = new Button("experimenter");
        experimenterButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 20; -fx-shape: 'M0,50 a50,50 0 1,0 100,0 a50,50 0 1,0 -100,0'; -fx-min-width: 100; -fx-min-height: 100; -fx-max-width: 100; -fx-max-height: 100;");


        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(nameLabel, nameField, new Label("& select a role"), setterButton, experimenterButton);


        Scene playerChoiceScene = new Scene(vbox, primaryStage.getWidth(), primaryStage.getHeight()); // Use the primary stage's width and height for full screen
        vbox.setStyle("-fx-background-color: black;");

        setterButton.setOnAction(e -> {
            primaryStage.setScene(mainGameScene);
            primaryStage.setFullScreen(true); //change to the main game scene
        });

        //action event for experimenter button
        experimenterButton.setOnAction(e -> {
            primaryStage.setScene(mainGameScene);
            primaryStage.setFullScreen(true); // TODO: trigger random atoms & go straight to experimenter stage.
        });


        primaryStage.setScene(playerChoiceScene);
        primaryStage.setFullScreen(true);
    }


    //this method handles displaying the intermediate screen between the setter finishing their turn and the experimenter starting their turn.
    private void showExperimenterTurnScreen(Stage primaryStage) {
        Label chosenAtomsLabel = new Label("You have chosen your atoms!");
        chosenAtomsLabel.setFont(new Font("Arial", 24));
        chosenAtomsLabel.setTextFill(Color.WHITE);

        Label experimenterTurnLabel = new Label("it is now the experimenter's turn...");
        experimenterTurnLabel.setFont(new Font("Arial", 18));
        experimenterTurnLabel.setTextFill(Color.WHITE);

        Button continueButton = new Button("continue to experimenter");
        continueButton.setFont(new Font("Arial", 16));
        continueButton.setOnAction(event -> {
            showExperimenterScreen(primaryStage);
        });

        VBox vbox = new VBox(10, chosenAtomsLabel, experimenterTurnLabel, continueButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);

        Scene experimenterTurnScene = new Scene(vbox, primaryStage.getWidth(), primaryStage.getHeight());
        vbox.setStyle("-fx-background-color: black;");

        primaryStage.setScene(experimenterTurnScene);
        primaryStage.setFullScreen(true);
    }


    //this method handles displaying the main game screen where the experimenter is actually playing.
    private void showExperimenterScreen(Stage primaryStage) {


        BorderPane root2 = new BorderPane(); //root is BorderPane layout as this is best suited as the base template for our Game UI.
        Scene experimenterScene = new Scene(root2);
        StackPane centerStackPane2 = new StackPane(); //centre container in root is StackPane for main game stage hexagon grid.
        StackPane buttonsStackPane2 = new StackPane();
        //group for hex cells so grid can be manipulated as a unit (for layout purposes).

//        centerStackPane.setMinWidth(Region.USE_PREF_SIZE);
//        centerStackPane.setMaxWidth(Region.USE_COMPUTED_SIZE);
        BorderPane.setMargin(centerStackPane2, new Insets(0, 0, 0, 100));

        HexCellGenerator.resetStartingPositions(); /*resetting here is crucial as it directly affects the layout if the static variables,
        (the startig x and y points) for the hex cells and ray circles are remain shifted from the last call to grid generation.*/

        HexCellGenerator.generateHexCells(gridGroup2);//adding hex cells to group.
        HexCellGenerator.printHexCellsAndCenters(); //TEST
        RayCircle.generateRayCircles(gridGroup2); //adding ray circles to same group.

        gameState2 = new GameState();
        generateReadyButton(buttonsStackPane2, gameState2, primaryStage);
        BorderPane.setMargin(buttonsStackPane2, new Insets(0, 0, 50, 30));

        centerStackPane2.getChildren().add(gridGroup2);

        translation.linkMaps();


        Label turn2 = generateTopExptText();
        //setting text to the centre in the root top container.
        VBox topContainer2 = new VBox();
        topContainer2.getChildren().add(turn2);
        VBox.setMargin(turn2, new Insets(50, 0, 0, 50));
        topContainer2.setAlignment(Pos.TOP_CENTER);

        Label experimenterInstructions = generateExperimenterInstructions();
        VBox rightContainer2 = new VBox();
        rightContainer2.getChildren().add(experimenterInstructions);
        rightContainer2.setAlignment(Pos.TOP_RIGHT);
        VBox.setMargin(rightContainer2, new Insets(0, 100, 0, 0));
        rightContainer2.setMaxWidth(300);
        VBox rayMarkerKey = generateRayMarkerKey();
        BorderPane.setAlignment(rayMarkerKey, Pos.TOP_LEFT);


        VBox rightPanel = createRightPanel();
        root2.setRight(rightPanel);


        root2.setLeft(rayMarkerKey);
        root2.setTop(topContainer2);
        root2.setRight(rightContainer2);
        root2.setCenter(centerStackPane2);
        root2.setBottom(buttonsStackPane2);
        root2.setStyle("-fx-background-color: black;");

        primaryStage.setScene(experimenterScene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    private VBox createRightPanel() {
        Button fireRayButton = createFireRayButton();  // Assume this method returns a configured Button
        fireRayButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");

        VBox rightContainer = new VBox(10); // 10 is the spacing between elements
        rightContainer.getChildren().add(fireRayButton);
        rightContainer.setAlignment(Pos.CENTER); // Center align the content
        rightContainer.setPadding(new Insets(15)); // Padding around the VBox

        return rightContainer;
    }




    private Label generateTopText() {
        Label playerTurn = new Label("Setter's turn.");
        //css inline styling
        playerTurn.setStyle("-fx-font-family: 'Arial Rounded MT Bold'; -fx-font-size: 60px;");
        //borderpane margin and alignment
        playerTurn.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        BorderPane.setMargin(playerTurn, new Insets(100, 0, 0, 0));

        return playerTurn;
    }
    private Label generateTopExptText() { //generating the top text for the experimenter screen.
        Label exptTurn = new Label("Experimenter's turn.");
        //css inline styling
        exptTurn.setStyle("-fx-font-family: 'Arial Rounded MT Bold'; -fx-font-size: 60px;");
        //borderpane margin and alignment
        exptTurn.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        BorderPane.setMargin(exptTurn, new Insets(100, 0, 0, 0));

        return exptTurn;
    }

    private Label generateSetterInstructions() {
        Label instructions = new Label("Please place 6 atoms inside your chosen hex cells." +
                "After you are finished, click the Ready button below the board.");
        instructions.setStyle("-fx-font-family: 'Arial Rounded MT Bold'; -fx-font-size: 16px;");
        instructions.setWrapText(true);
        instructions.setMaxWidth(500);
        instructions.setTextAlignment(TextAlignment.CENTER);
        return instructions;
    }

    private Button createFireRayButton() {
        Button fireRayButton = new Button("Fire Ray");
        fireRayButton.setStyle("-fx-font-family: 'Arial Rounded MT Bold'; -fx-font-size: 16px;");
        fireRayButton.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 1px;");

        //event handler for fire ray button click
        fireRayButton.setOnAction(event -> {
            int rayNumber = RayCircle.getCurrentlyClickedRayNumber();
            if (rayNumber != -1) {
                // If a valid ray number is clicked, handle the firing of the ray here
                System.out.println("Firing ray: " + rayNumber);
                // TODO: Add the code to handle the ray firing action
            } else {
                // No ray circle is currently selected
                System.out.println("No ray selected!");
            }
        });

        return fireRayButton;
    }

    private Label generateExperimenterInstructions() {
        Label instructionsExpt = new Label("You can choose a ray by selecting on it. The chosen ray can be changed by simply clicking on the ray you would like to choose instead." +
                "When you want to fire the ray, click the [Fire Ray] button below. The results of your ray will be shown in ray markers, indicated by the changing colour of the ray nodes.");
        instructionsExpt.setStyle("-fx-font-family: 'Arial Rounded MT Bold'; -fx-font-size: 16px;");
        instructionsExpt.setWrapText(true);
        instructionsExpt.setMaxWidth(500);
        instructionsExpt.setTextAlignment(TextAlignment.CENTER);
        return instructionsExpt;
    }
    private VBox generateRayMarkerKey() {
        //white circle to represent reflected ray marker
        Circle whiteCircle = new Circle(10, Color.WHITE);
        whiteCircle.setStroke(Color.BLACK); // So it's visible on a white background
        Label whiteLabel = new Label("Reflected");
        whiteLabel.setStyle("-fx-font-family: 'Arial Rounded MT Bold'; -fx-font-size: 16px; -fx-text-fill: black;");
        HBox whiteKey = new HBox(5, whiteCircle, whiteLabel); // 5 is the spacing between items

        //black circle to reflect absorbed ray marker
        Circle blackCircle = new Circle(10, Color.BLACK);
        Label blackLabel = new Label("Direct hit/Absorbed");
        blackLabel.setStyle("-fx-font-family: 'Arial Rounded MT Bold'; -fx-font-size: 16px; -fx-text-fill: black;");
        HBox blackKey = new HBox(5, blackCircle, blackLabel); // 5 is the spacing between items

        //combining the hboxes into a vbox to align vertically
        VBox keyBox = new VBox(10, whiteKey, blackKey); // 10 is the spacing between keys
        keyBox.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-padding: 10; -fx-background-color: white;");
        keyBox.setAlignment(Pos.CENTER);

        return keyBox;
    }
    private boolean isReadyClicked = false;


    public void generateReadyButton(StackPane buttonsStackPane, ReadyButtonClickedListener listener, Stage primaryStage) {
        Button ready = new Button("READY");

        //event handler for button click
        ready.setOnAction(event -> {
            if (AtomGenerator.atomCount == 6) {//max final atom check
                List<Point2D> atomPositions = collectAtomPositions(); //collecting the final atom positions.

                List<BlackBoxBoard.Point3D> setterAtomList = translation.get3DAtomMatch(atomPositions);
                sBoard.placeSetterAtoms(setterAtomList);
                eBoard.placeSetterAtoms(setterAtomList);
                sBoard.printBoard();
                //eBoard.printBoard();

                //print the positions
                System.out.println("Atom Positions:");
                for (Point2D position : atomPositions) {
                    System.out.println(position.toString());
                }

                gameState.setSetterAtomPositions(atomPositions);
                gameState.onReadyClicked();
                showExperimenterTurnScreen(primaryStage);
            } else {
                //alert popup to remind setter to place exactly 6 atoms.
                Alert readyButtonAlert = new Alert(Alert.AlertType.WARNING);
                readyButtonAlert.initOwner(primaryStage);
                readyButtonAlert.setTitle("Warning");
                readyButtonAlert.setHeaderText(null);
                readyButtonAlert.setContentText("Please place exactly 6 atoms in the hex board.");
                readyButtonAlert.showAndWait();
            }
        });
        //some inline CSS for button styling.
        ready.setStyle("-fx-font-family: 'Arial Rounded MT Bold'; -fx-font-size: 20px;-fx-padding: 10 30");
        StackPane.setAlignment(ready, Pos.BOTTOM_CENTER);
        //stack pane margin and alignments.
//        StackPane.setMargin(ready, new Insets(5, 10, 60, 20));
        buttonsStackPane.getChildren().add(ready);
    }


    private List<Point2D> collectAtomPositions() {
        List<Point2D> positions = new ArrayList<>();
        for (Node node : gridGroup.getChildren()) {
            if (node instanceof Circle && "atom".equals(node.getUserData())) { //making sure only atom Circle instances are referenced.
                Circle atom = (Circle) node;
                Point2D center = new Point2D(atom.getLayoutX(), atom.getLayoutY());
                positions.add(center);
            }
        }
        return positions;
    }



}


