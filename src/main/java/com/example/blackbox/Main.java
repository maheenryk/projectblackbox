package com.example.blackbox;

import com.example.blackbox.viewutil.RayCircle;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    private static final int MAX_ATOMS = 6;
    private int atomCount = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

       BorderPane root = new BorderPane(); //root is BorderPane layout as this is best suited as the base template for our Game UI.
        Scene scene = new Scene(root);
        StackPane centerStackPane = new StackPane(); //centre container in root is StackPane for main game stage hexagon grid.
        Group gridGroup = new Group(); //group for hex cells so grid can be manipulated as a unit (for layout purposes).

        generateHexCells(gridGroup); //adding hex cells to group.
        generateRayCircles(gridGroup); //adding raycircles to same group.

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


        // Ensure layout is updated
//        gridGroup.applyCss();
//        gridGroup.layout();
//


        root.setStyle("-fx-background-color: #84847f;");
        primaryStage.setTitle("BlackBox+");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();

    }

    public Point2D calculatePolygonCenter(Polygon polygon) {
        double sumX = 0;
        double sumY = 0;
        int pointCount = polygon.getPoints().size() / 2; // Each point has two coordinates (X, Y)

        for (int i = 0; i < polygon.getPoints().size(); i += 2) {
            sumX += polygon.getPoints().get(i); // X coordinate
            sumY += polygon.getPoints().get(i + 1); // Y coordinate
        }

        return new Point2D(sumX / pointCount, sumY / pointCount);
    }


    int xStartHex = 567;
    int yStartHex = 130;
    private void generateHexCells(Group root) {
        int XVal = 68;

        int col = 5;
        for (int j = 1; j <= 5; j++) {
            for (int i = 1; i <= col; i++) {
                Polygon hexagon = createHexCell(xStartHex+(i*XVal), yStartHex);
                addHoverEffectHex(hexagon);
                hexagon.setOnMouseClicked(this::handleHexagonClick);
                root.getChildren().add(hexagon);
            }
            col += 1;
            xStartHex -= (int) 34.5;
            yStartHex += 59;
        }

        xStartHex += 68;
        col = 8;
        for (int j = 1; j <= 4; j++) {
            for (int i = 1; i <= col; i++) {
                Polygon hexagon = createHexCell(xStartHex+(i*XVal), yStartHex);
                addHoverEffectHex(hexagon);
                hexagon.setOnMouseClicked(this::handleHexagonClick);
                root.getChildren().add(hexagon);
            }
            col -= 1;
            xStartHex += (int) 34.5;
            yStartHex += 59;
        }
    }

    private Polygon createHexCell(double layoutX, double layoutY) {
        Polygon hexagon = new Polygon(
                0.0, 0.0,
                34.64, 20.0,
                34.64, 60.0,
                0.0, 80.0,
                -34.64, 60.0,
                -34.64, 20.0
        );
        hexagon.setFill(Color.BLACK);
        hexagon.setLayoutX(layoutX);
        hexagon.setLayoutY(layoutY);
        hexagon.setStroke(Color.web("#4242ff"));
        hexagon.setStrokeWidth(2);
        hexagon.setStrokeType(StrokeType.INSIDE);
        return hexagon;
    }

    private void handleHexagonClick(MouseEvent event) {
        if (atomCount < MAX_ATOMS) {
            Polygon clickedHexagon = (Polygon) event.getSource();

            Group gridGroup = (Group) clickedHexagon.getParent();

            // Check if the hexagon already contains an atom
            Circle existingAtom = findAtomInHexagon(gridGroup, clickedHexagon);
            if (existingAtom == null) {
                // Calculating the center based on the hexagon's vertices
                double centerX = clickedHexagon.getLayoutBounds().getCenterX() + clickedHexagon.getLayoutX();
                double centerY = clickedHexagon.getLayoutBounds().getCenterY() + clickedHexagon.getLayoutY();

                // Create an atom and add it to the gridGroup (same group as the hexagons)
                Circle atom = createAtom(centerX, centerY);
                gridGroup.getChildren().add(atom);

                atomCount++;

                atom.setOnMouseClicked(atomEvent -> {
                    gridGroup.getChildren().remove(atom);
                    atomCount--;
                });
            }
        }
    }

    private void getCentreCoords(Polygon hexcell){
        double centerX = hexcell.getLayoutBounds().getCenterX();
        double centerY = hexcell.getLayoutBounds().getCenterY();
    }

    private Circle findAtomInHexagon(Group root, Polygon hexagon) {
        // Iterate through the children of the root to find an atom in the same hexagon
        for (javafx.scene.Node node : root.getChildren()) {
            if (node instanceof Circle atom) {
                if (isPointInHexagon(atom.getLayoutX(), atom.getLayoutY(), hexagon)) {
                    return atom; // Adjust the margin as needed
                }
            }
        }
        return null;
    }

    private boolean isPointInHexagon(double x, double y, Polygon hexagon) {
        // Get the bounds of the hexagon
        javafx.geometry.Bounds hexagonBounds = hexagon.getBoundsInParent();

        double margin = 20.0;
        // Reduce the bounds by the margin because the ray circles were being detected as atoms
        double minX = hexagonBounds.getMinX() + margin;
        double minY = hexagonBounds.getMinY() + margin;
        double maxX = hexagonBounds.getMaxX() - margin;
        double maxY = hexagonBounds.getMaxY() - margin;

        // Check if the point (x, y) is inside the modified bounds of the hexagon
        return x >= minX && x <= maxX && y >= minY && y <= maxY;
    }

    private Circle createAtom(double centerX, double centerY) {
        Circle atom = new Circle(20, Color.RED);
        atom.setLayoutX(centerX);
        atom.setLayoutY(centerY);
        return atom;
    }

    private void addHoverEffectHex(Polygon hexagon) {
        hexagon.setOnMouseEntered(event -> hexagon.setFill(Color.LIGHTGOLDENRODYELLOW));
        hexagon.setOnMouseExited(event -> hexagon.setFill(Color.BLACK));
    }

//    private void addHoverEffectCirc(Circle atom) {
//        atom.setOnMouseEntered(event -> atom.setFill(Color.WHITE));
//        atom.setOnMouseExited(event -> atom.setFill(Color.web("#ab8641")));
//    }


    private void generateRayCircles(Group root) { //method for generating nodes (ray circles)
        //for loops using createRayCircle method to generate circles
        //with ray numbers in circles stacked as text.


        //arrays containing ray numbers organised using compass directions for the 6 edges of the main hexagon. each edge
        //is filled using a separate for loop.
        int[] rayNumNorth = {1, 54, 53, 52, 51, 50, 49, 48, 47, 46};
        int[] rayNumNorthWest = {2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] rayNumNorthEast = {45, 44, 43, 42, 41, 40, 39, 38, 37};
        int[] rayNumSouthWest = {18, 17, 16, 15, 14, 13, 12, 11};
        int[] rayNumSouthEast = {29, 30, 31, 32, 33, 34, 35, 36};
        int[] rayNumSouth = {19, 20, 21, 22, 23, 24, 25, 26, 27, 28};

        double circleXStartNorth = 605;
        double circleYStartNorth = 130;

        //generating circles for north edge of hexagon ------------
        for (int i = 0; i < rayNumNorth.length; i++) {
            int rayNumber = rayNumNorth[i];
            RayCircle circle = createRayCircle(circleXStartNorth + (i*34), circleYStartNorth, rayNumber);
            root.getChildren().add(circle);
        }

        //northwest edge----------------------------------------
        double NWCircleXStart = circleXStartNorth - 17;
        double NWCircleYStart = circleYStartNorth + 29.5;

        for (int leftRayNumber : rayNumNorthWest) {
            RayCircle circle = createRayCircle(NWCircleXStart, NWCircleYStart, leftRayNumber);
            root.getChildren().add(circle);

            NWCircleXStart -= 17;
            NWCircleYStart += 29.5;
        }

        //northeast edge----------------------------------------
        double NECircleXStart = circleXStartNorth + ((rayNumNorth.length * 34) - 17); //starting at the rightmost node of north edge.
        double NECircleYStart = circleYStartNorth + 29.5;

        for (int rightRayNumber : rayNumNorthEast) {
            RayCircle circle = createRayCircle(NECircleXStart, NECircleYStart, rightRayNumber);
            root.getChildren().add(circle);

            NECircleXStart += 17;
            NECircleYStart += 29.5;
        }

        double circleXStartSouth = 605;
        double circleYStartSouth = 659;

        //south edge -----------------------------------------
        for (int i = 0; i < rayNumSouth.length; i++) {
            int rayNumber = rayNumSouth[i];
            RayCircle circle = createRayCircle(circleXStartSouth + (i*34), circleYStartSouth, rayNumber);
            root.getChildren().add(circle);
        }

        //southwest edge----------------------------------------
        double SWCircleXStart = circleXStartSouth - 17;
        double SWCircleYStart = circleYStartSouth - 29.5;

        for (int leftRayNumber : rayNumSouthWest) {
            RayCircle circle = createRayCircle(SWCircleXStart, SWCircleYStart, leftRayNumber);
            root.getChildren().add(circle);

            SWCircleXStart -= 17;
            SWCircleYStart -= 29.5;
        }

        //southeast edge ----------------------------------------
        double SECircleXStart = circleXStartSouth + (rayNumSouth.length * 34) - 17; //starting at the rightmost node of south edge.
        double SECircleYStart = circleYStartSouth - 29.5;

        for (int rightRayNumber : rayNumSouthEast) {
            RayCircle circle = createRayCircle(SECircleXStart, SECircleYStart, rightRayNumber);
            root.getChildren().add(circle);

            SECircleXStart += 17;
            SECircleYStart -= 29.5;
        }




    }

//    private void handleRayCircleClick(RayCircle circle) {
//        circle.setOnMouseClicked(event -> {
//            if (circle.getFill().equals(Color.RED)) {
//                // Change color to another color when clicked
//                circle.setFill(Color.BLUE); // Change to your desired color
//            } else {
//                // Change color back to the original color when clicked again
//                circle.setFill(Color.RED); // Change to your original color
//            }
//        });
//    }

    private RayCircle createRayCircle(double layoutX, double layoutY, int number) {
        RayCircle circle = new RayCircle(12.0, Color.web("#4242ff"));
        circle.setLayoutX(layoutX);
        circle.setLayoutY(layoutY);

        // Set ray number text
        circle.setRayText(String.valueOf(number));

        return circle;
    }

    private Label generateTopText() {
        Label playerTurn = new Label("Setter's turn.");
        playerTurn.setStyle("-fx-text-fill: darkred;");;
        playerTurn.setFont(Font.font("Arial", FontWeight.BOLD, 45));
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