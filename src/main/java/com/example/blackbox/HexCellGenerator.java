package com.example.blackbox;

import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;

public class HexCellGenerator {

    private static final int MAX_ATOMS = 6;
     private static int atomCount = 0;

     static private int xStartHex = 567;
     static private int yStartHex = 130;

    static void generateHexCells(Group root) {
        int XVal = 68;

        int col = 5;
        for (int j = 1; j <= 5; j++) {
            for (int i = 1; i <= col; i++) {
                Polygon hexagon = createHexCell(xStartHex + (i * XVal), yStartHex);
                addHoverEffectHex(hexagon);
                hexagon.setOnMouseClicked(HexCellGenerator::handleHexagonClick);
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
                Polygon hexagon = createHexCell(xStartHex + (i * XVal), yStartHex);
                addHoverEffectHex(hexagon);
                hexagon.setOnMouseClicked(HexCellGenerator::handleHexagonClick);
                root.getChildren().add(hexagon);
            }
            col -= 1;
            xStartHex += (int) 34.5;
            yStartHex += 59;
        }
    }

    static private Polygon createHexCell(double layoutX, double layoutY) {
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

    static private void handleHexagonClick(MouseEvent event) {
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

    static private Circle findAtomInHexagon(Group root, Polygon hexagon) {
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

    private static boolean isPointInHexagon(double x, double y, Polygon hexagon) {
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

    private static Circle createAtom(double centerX, double centerY) {
        Circle atom = new Circle(20, Color.RED);
        atom.setLayoutX(centerX);
        atom.setLayoutY(centerY);
        return atom;
    }

    private static void addHoverEffectHex(Polygon hexagon) {
        hexagon.setOnMouseEntered(event -> hexagon.setFill(Color.LIGHTGOLDENRODYELLOW));
        hexagon.setOnMouseExited(event -> hexagon.setFill(Color.BLACK));
    }
}