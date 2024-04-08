package com.example.blackbox;

// import all libraries needed
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import utils.ReadyButtonClickedListener;
import Controller.GameState;

public class Main extends Application {

    private GameState gameState;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane(); //root is BorderPane layout as this is best suited as the base template for our Game UI.
        Scene scene = new Scene(root);
        StackPane centerStackPane = new StackPane(); //centre container in root is StackPane for main game stage hexagon grid.
        StackPane buttonsStackPane = new StackPane();
        Group gridGroup = new Group(); //group for hex cells so grid can be manipulated as a unit (for layout purposes).

        HexCellGenerator.generateHexCells(gridGroup);//adding hex cells to group.
        RayCircle.generateRayCircles(gridGroup); //adding ray circles to same group.

        gameState = new GameState();
        generateReadyButton(buttonsStackPane, gameState);

        centerStackPane.getChildren().add(gridGroup);

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
        primaryStage.setTitle("BlackBox+");
        primaryStage.setScene(scene);
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

        //eventhandler for button click
        ready.setOnAction(event -> {
            isReadyClicked = true;
            // method maybe
            listener.onReadyClicked(); //listener qualifier added here
        });
        //some inline CSS for button styling.
        ready.setStyle("-fx-font-family: 'Arial Rounded MT Bold'; -fx-font-size: 20px;-fx-padding: 10 30");
        StackPane.setAlignment(ready, Pos.BOTTOM_CENTER);
        //stackpane margin and alignments.
        StackPane.setMargin(ready, new Insets(5, 10, 60, 20));
        buttonsStackPane.getChildren().add(ready);
    }
}





