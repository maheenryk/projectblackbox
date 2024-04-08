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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.FontWeight;
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane(); //root is BorderPane layout as this is best suited as the base template for our Game UI.
        Scene scene = new Scene(root);
        StackPane centerStackPane = new StackPane(); //centre container in root is StackPane for main game stage hexagon grid.
        Group gridGroup = new Group(); //group for hex cells so grid can be manipulated as a unit (for layout purposes).

        HexCellGenerator.generateHexCells(gridGroup);//adding hex cells to group.
        RayCircle.generateRayCircles(gridGroup); //adding raycircles to same group.

        centerStackPane.getChildren().add(gridGroup);

        Label turn = generateTopText();
        //setting text to the centre in the root top container.
        VBox topContainer = new VBox();
        topContainer.getChildren().add(turn);
        VBox.setMargin(turn, new Insets(50, 0, 0, 0)); // Top margin
        topContainer.setAlignment(Pos.TOP_CENTER);


        root.setTop(topContainer);

        generateReadyButton(root);


        root.setCenter(centerStackPane);

        //calculation for centre coordinates.

        root.setStyle("-fx-background-color: #84847f;");
        primaryStage.setTitle("BlackBox+");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();

    }

    private Label generateTopText() {
        Label playerTurn = new Label("Setter's turn.");
        playerTurn.setStyle("-fx-text-fill: #4242ff;");;
        playerTurn.setFont(Font.font("Roboto Mono", FontWeight.BOLD, 45));
        playerTurn.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        BorderPane.setMargin(playerTurn, new Insets(100, 0, 0, 0));

        return playerTurn;
    }

    private void generateReadyButton(BorderPane root) {
        Button ready = new Button("READY");
        StackPane.setAlignment(ready, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(ready, new Insets(150));
        root.getChildren().add(ready);
    }



}