/**
 *
 *  HexCellGenerator handles the creation and locating of the individual hexagonal cells in the main game board.
 *  HexCellGenerator.java contains method generateHexCells which is called in the main method.
 */


package com.example.blackbox;

import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;

import static com.example.blackbox.AtomGenerator.*;

public class HexCellGenerator {


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

    private static void addHoverEffectHex(Polygon hexagon) {
        hexagon.setOnMouseEntered(event -> hexagon.setFill(Color.LIGHTGOLDENRODYELLOW));
        hexagon.setOnMouseExited(event -> hexagon.setFill(Color.BLACK));
    }
}