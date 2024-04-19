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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import utils.ReadyButtonClickedListener;
import Controller.GameState;

import java.util.ArrayList;
import java.util.List;


public class Main extends Application {
    Group gridGroup = new Group();
    BlackBoxBoard b = new BlackBoxBoard();
    Translation translation = new Translation(b);
    private Stage window;
    private GameState gameState;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {


//------------------------start screen
        Button startButton = new Button("Start");
        startButton.setStyle("-fx-font-family: 'Arial Rounded MT Bold'; -fx-font-size: 20px;-fx-padding: 10 30");
        StackPane startLayout = new StackPane();
        startLayout.getChildren().add(startButton);
        Scene startScene = new Scene(startLayout, 300, 250);


        //-------------------main game screen
        BorderPane root = new BorderPane(); //root is BorderPane layout as this is best suited as the base template for our Game UI.
        Scene mainGameScene = new Scene(root);
        StackPane centerStackPane = new StackPane(); //centre container in root is StackPane for main game stage hexagon grid.
        StackPane buttonsStackPane = new StackPane();
        //group for hex cells so grid can be manipulated as a unit (for layout purposes).

        HexCellGenerator.generateHexCells(gridGroup);//adding hex cells to group.
        HexCellGenerator.printHexCellsAndCenters(); //TEST
        RayCircle.generateRayCircles(gridGroup); //adding ray circles to same group.

        gameState = new GameState();
        generateReadyButton(buttonsStackPane, gameState);

        centerStackPane.getChildren().add(gridGroup);

        translation.linkMaps();

        Label turn = generateTopText();
        //setting text to the centre in the root top container.
        VBox topContainer = new VBox();
        topContainer.getChildren().add(turn);
        VBox.setMargin(turn, new Insets(50, 0, 0, 0)); // Top margin
        topContainer.setAlignment(Pos.TOP_CENTER);


        root.setTop(topContainer);
        root.setCenter(centerStackPane);
        root.setBottom(buttonsStackPane);
        root.setStyle("-fx-background-color: #84847f;");


        startButton.setOnAction(e -> {
            boolean wasFullScreen = primaryStage.isFullScreen();
            primaryStage.setScene(mainGameScene);
            primaryStage.setFullScreen(wasFullScreen);
        });

        primaryStage.setTitle("BlackBox+");
        primaryStage.setScene(startScene);
        primaryStage.setFullScreen(true);
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

    private boolean isReadyClicked = false;

    public void generateReadyButton(StackPane buttonsStackPane, ReadyButtonClickedListener listener) {
        Button ready = new Button("READY");

        //event handler for button click
        ready.setOnAction(event -> {
            if (AtomGenerator.atomCount == 6) {//max final atom check
                List<Point2D> atomPositions = collectAtomPositions(); //collecting the final atom positions.
                translation.get3DAtomMatch(atomPositions);
                //print the positions
                System.out.println("Atom Positions:");
                for (Point2D position : atomPositions) {
                    System.out.println(position.toString());
                }

                gameState.setSetterAtomPositions(atomPositions);
                gameState.onReadyClicked();
            } else {
                //alert popup to remind setter to place exactly 6 atoms.
                Alert readyButtonAlert = new Alert(Alert.AlertType.WARNING);
                readyButtonAlert.setTitle("Warning");
                readyButtonAlert.setHeaderText(null);
                readyButtonAlert.setContentText("Please place exactly 6 atoms in hexagons.");
                readyButtonAlert.showAndWait();
            }
        });
        //some inline CSS for button styling.
        ready.setStyle("-fx-font-family: 'Arial Rounded MT Bold'; -fx-font-size: 20px;-fx-padding: 10 30");
        StackPane.setAlignment(ready, Pos.BOTTOM_CENTER);
        //stack pane margin and alignments.
        StackPane.setMargin(ready, new Insets(5, 10, 60, 20));
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





