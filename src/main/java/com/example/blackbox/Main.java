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

    Group gridGroup = new Group();
    BlackBoxBoard b = new BlackBoxBoard(); //setter board instance
//    BlackBoxBoard eBoard = new BlackBoxBoard(); //experimenter board instance
    Translation translation = new Translation(b);
    private Stage window;
    private GameState gameState;
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

 //---------------------playerChoice screen


//-------------------main game screen
        BorderPane root = new BorderPane(); //root is BorderPane layout as this is best suited as the base template for our Game UI.
        Scene mainGameScene = new Scene(root);
        StackPane centerStackPane = new StackPane(); //centre container in root is StackPane for main game stage hexagon grid.
        StackPane buttonsStackPane = new StackPane();
        //group for hex cells so grid can be manipulated as a unit (for layout purposes).

//        centerStackPane.setMinWidth(Region.USE_PREF_SIZE);
//        centerStackPane.setMaxWidth(Region.USE_COMPUTED_SIZE);
        BorderPane.setMargin(centerStackPane, new Insets(0, 0, 0, 100));
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
        rightContainer.setAlignment(Pos.CENTER_RIGHT);
        rightContainer.setMaxWidth(100);


        root.setTop(topContainer);
        root.setRight(rightContainer);
        root.setCenter(centerStackPane);
        root.setBottom(buttonsStackPane);
        root.setStyle("-fx-background-color: #84847f;");



        startButton.setOnAction(e -> {
            showPlayerChoiceScreen(primaryStage, mainGameScene);
        });

        primaryStage.setTitle("BlackBox+");
        primaryStage.setScene(startScene);
        primaryStage.setFullScreen(true);
        primaryStage.show();

    }

    private void showPlayerChoiceScreen(Stage primaryStage, Scene mainGameScene) {
        //label for the name entry prompt
        Label nameLabel = new Label("Enter your name:");
        nameLabel.setTextFill(Color.WHITE);

        //text field for the name entry
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
        vbox.setStyle("-fx-background-color: grey;");

        setterButton.setOnAction(e -> {
            primaryStage.setScene(mainGameScene);
            primaryStage.setFullScreen(true); //change to the main game scene
        });

        //action event for experimenter button
        experimenterButton.setOnAction(e -> {
            primaryStage.setScene(mainGameScene);
            primaryStage.setFullScreen(true); //TODO: trigger random atoms & go straight to experimenter stage.
        });


        primaryStage.setScene(playerChoiceScene);
        primaryStage.setFullScreen(true);
    }

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
            //TODO: Handle the action when the continue button is clicked
        });

        VBox vbox = new VBox(10, chosenAtomsLabel, experimenterTurnLabel, continueButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);

        Scene experimenterTurnScene = new Scene(vbox, primaryStage.getWidth(), primaryStage.getHeight());
        vbox.setStyle("-fx-background-color: black;");

        primaryStage.setScene(experimenterTurnScene);
        primaryStage.setFullScreen(true);
    }

    private void showExperimenterScreen(Stage primaryStage) {
        //scene for the experimenter
        BorderPane root = new BorderPane();

        Scene experimenterScene = new Scene(root);
        primaryStage.setScene(experimenterScene);
        primaryStage.show();
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

    private Label generateSetterInstructions() {
        Label instructions = new Label("Please place 6 atoms inside your chosen hex cells." +
                "After you are finished, click the Ready button below the board.");
        instructions.setStyle("-fx-font-family: 'Arial Rounded MT Bold'; -fx-font-size: 20px;");
        instructions.setWrapText(true);
        instructions.setMaxWidth(150);
        instructions.setTextAlignment(TextAlignment.CENTER);
        return instructions;
    }

    private boolean isReadyClicked = false;

    public void generateReadyButton(StackPane buttonsStackPane, ReadyButtonClickedListener listener, Stage primaryStage) {
        Button ready = new Button("READY");

        //event handler for button click
        ready.setOnAction(event -> {
            if (AtomGenerator.atomCount == 6) {//max final atom check
                List<Point2D> atomPositions = collectAtomPositions(); //collecting the final atom positions.

                List<BlackBoxBoard.Point3D> setterAtomList = translation.get3DAtomMatch(atomPositions);
                b.placeSetterAtoms(setterAtomList);
                b.printBoard();

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





