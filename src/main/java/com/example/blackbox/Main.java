package com.example.blackbox;

// import all libraries needed
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    // maximum amount of atoms placed is always 6
    private static final int MAX_ATOMS = 6;

    // declare variable to keep track of atoms placed on the grid
    private int atomCount = 0;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        AnchorPane root = new AnchorPane();

        // call the following functions in the root anchorpane
        generateHexCells(root);

        generateRayCircles(root);

        generateText(root);

        generateReadyButton(root);

        // scene specifications
        Scene scene = new Scene(root, 1550, 800);
        root.setStyle("-fx-background-color: #84847f;");
        primaryStage.setTitle("BlackBox+");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // the x value for vertex 1 of the first hexagon
    int xStartHex = 567;

    // the y value for vertex 1 of the first hexagon
    int yStartHex = 130;
    private void generateHexCells(AnchorPane root) {

        // the amount that the hexagons are being spaced by on the x-axis
        int XVal = 68;

        // start with 5 columns and rows
        int rows = 5;
        int col = 5;

        // generate 9 rows of hexagons
        for (int j = 1; j <= 9; j++) {
            for (int i = 1; i <= col; i++) {
                Polygon hexagon = createHexCell(xStartHex+(i*XVal), yStartHex);
                addHoverEffectHex(hexagon);
                hexagon.setOnMouseClicked(this::handleHexagonClick);
                root.getChildren().add(hexagon);
            }

            // decrease rows after a row has been generated
            rows -= 1;
            // keep increasing the columns until the first 5 rows are generated
            if (rows > 0) {
                col += 1;
                xStartHex -= (int) 34.5;
                yStartHex += 59;
            }

            // when the fifth row has been generated, change the variables to start generating the last 4 rows
            else if (rows == 0){
                xStartHex += (int) 34.5;
                yStartHex += (int) 59;
                col -= 1;
            }

            // generate the last 4 rows
            else {
                col -= 1;
                xStartHex += (int) 34.5;
                yStartHex += 59;
            }

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
        hexagon.setStroke(Color.web("#ab8641"));
        hexagon.setStrokeWidth(2);
        hexagon.setStrokeType(StrokeType.INSIDE);
        return hexagon;
    }

    void handleHexagonClick(MouseEvent event) {
        if (atomCount < MAX_ATOMS) {
            Polygon clickedHexagon = (Polygon) event.getSource();
            AnchorPane root = (AnchorPane) clickedHexagon.getParent();

            // Check if the hexagon already contains an atom
            Circle existingAtom = findAtomInHexagon(root, clickedHexagon);
            if (existingAtom == null) {
                // If no atom exists, get the center coordinates of the clicked hexagon
                double centerX = clickedHexagon.getLayoutX();
                double centerY = clickedHexagon.getLayoutY() + 40;

                // Create an atom and add it to the root
                Circle atom = createAtom(centerX, centerY);
                ((AnchorPane) clickedHexagon.getParent()).getChildren().add(atom);

                // Increment the atom count
                atomCount++;

                // Add an event handler to the atom for removing itself when clicked
                atom.setOnMouseClicked(atomEvent -> {
                    ((AnchorPane) atom.getParent()).getChildren().remove(atom);
                    // Decrement the atom count when an atom is removed
                    atomCount--;
                });
            }
        }
    }

    private Circle findAtomInHexagon(AnchorPane root, Polygon hexagon) {
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
        hexagon.setOnMouseEntered(event -> hexagon.setFill(Color.YELLOW));
        hexagon.setOnMouseExited(event -> hexagon.setFill(Color.BLACK));
    }

    private void addHoverEffectCirc(Circle atom) {
        atom.setOnMouseEntered(event -> atom.setFill(Color.WHITE));
        atom.setOnMouseExited(event -> atom.setFill(Color.web("#ab8641")));
    }

    private void generateRayCircles(AnchorPane root) {
        double circleXStart = 584.32;
        double circleYStart = 140;

        for (int i = 1; i <= 10; i++) {
            Circle circle = createRayCircle(circleXStart + (i*34), circleYStart);
            addHoverEffectCirc(circle);
            root.getChildren().add(circle);
        }

        circleXStart += 17;
        circleYStart += 30;
        int xDist = 10;
        for (int j = 1; j <= 9; j++) {
            for (int i = 0; i < 2; i++) {
                Circle circle = createRayCircle(circleXStart + (i * xDist * 34), circleYStart);
                addHoverEffectCirc(circle);
                root.getChildren().add(circle);
            }
            xDist += 1;
            circleXStart -= 17;
            circleYStart += 29.5;
        }

        xDist -= 2;
        circleXStart += 34;
        for (int j = 1; j <= 8; j++) {
            for (int i = 0; i < 2; i++) {
                Circle circle = createRayCircle(circleXStart + (i * xDist * 34), circleYStart);
                addHoverEffectCirc(circle);
                root.getChildren().add(circle);
            }
            xDist -= 1;
            circleXStart += 17;
            circleYStart += 29.5;
        }

        circleXStart = 584.32;
        for (int i = 1; i <= 10; i++) {
            Circle circle = createRayCircle(circleXStart + (i*34), circleYStart);
            addHoverEffectCirc(circle);
            root.getChildren().add(circle);
        }
    }

    private Circle createRayCircle(double layoutX, double layoutY) {
        Circle circle = new Circle(10.0, Color.web("#ab8641"));
        circle.setLayoutX(layoutX);
        circle.setLayoutY(layoutY);
        circle.setStroke(Color.BLACK);
        circle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        return circle;
    }

    private void generateText(AnchorPane root) {
        Text text = new Text("You are the setter");
        text.setFont(Font.font("Franklin Gothic Book", 34.0));
        text.setLayoutX(594.0);
        text.setLayoutY(77.0);
        text.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        text.setStrokeWidth(0.0);
        text.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        text.setWrappingWidth(357.26251220703125);

        root.getChildren().add(text);
    }

    private void generateReadyButton(AnchorPane root) {
        Button ready = new Button("READY");
        ready.setFont(Font.font("Franklin Gothic Book", 26));
        ready.setLayoutX(1300);
        ready.setLayoutY(650);

        root.getChildren().add(ready);
    }

    public int getAtomCount() {
        return atomCount;
    }
}