/**
 *
 *  HexCellGenerator handles the creation and locating of the individual hexagonal cells in the main game board.
 *  HexCellGenerator.java contains method generateHexCells which is called in the main method.
 */


package com.example.blackbox;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.geometry.Point2D;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.control.Tooltip;

import java.util.LinkedHashMap;
import java.util.Map;


import static com.example.blackbox.AtomGenerator.*;
import static com.example.blackbox.Main.BACKGROUND_COLOR;

public class HexCellGenerator {

    static private final int ORIGINAL_X_START_HEX = 567;
    static private final int ORIGINAL_Y_START_HEX = 130;
    static void resetStartingPositions() { //method for resetting the starting generation positions of the hex cells between calls.
        xStartHex = ORIGINAL_X_START_HEX;
        yStartHex = ORIGINAL_Y_START_HEX;
    }

     static private int xStartHex = 567;
     static private int yStartHex = 130;
    static private int hexCellId = 1; /* counter for easy identification of individual hex cells. this
    is helpful instead of using the polygon object's hashcode ID as during logic testing,
    easily identifying individual hex cells (int 1 - 61) seems preferable than long random hashcodes.*/

    static private Map<Integer, Point2D> hexCellsMap = new LinkedHashMap<>();// LinkedHashMap to store each Polygon and its center in Point2D
    static void generateHexCells(Group root) {
        int XVal = 68;

        int col = 5;

        hexCellsMap.clear();

        hexCellId = 1; //resetting the counter for each generation.

        for (int j = 1; j <= 5; j++) {
            for (int i = 1; i <= col; i++) {

//                double centerX = xStartHex + (i * XVal) + 34.64; // x centre calculation //old calculations***
//                double centerY = yStartHex + 40; // y centre calculation


                Polygon hexagon = createHexCell(xStartHex + (i * XVal), yStartHex);
                addHoverEffectHex(hexagon);
                hexagon.setOnMouseClicked(event -> handleHexagonClick(event, root));//changed to pass the group dynamically to allow for multiple groups.
                double centerX = hexagon.getLayoutBounds().getCenterX() + hexagon.getLayoutX();
                double centerY = hexagon.getLayoutBounds().getCenterY() + hexagon.getLayoutY();
                Point2D center = new Point2D(centerX, centerY); //2-d coordinates stored in Point2d
                root.getChildren().add(hexagon);

                //-----------------------------------------------------------this section is devtools.
                Text hexIdText = new Text(String.valueOf(hexCellId));
                hexIdText.setFont(new Font(20));
                double textWidth = hexIdText.getLayoutBounds().getWidth();
                double textHeight = hexIdText.getLayoutBounds().getHeight();

                //centering the text
                hexIdText.setX(center.getX() - (textWidth/2));
                hexIdText.setY(center.getY() + (textHeight/3));
                hexIdText.setFill(Color.RED);

                //root.getChildren().add(hexIdText);
                //------------------------------------------------------------------------------------------


                hexCellsMap.put(hexCellId++, center); // Store hexagon with its center Point2D object in linked hash map.
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
                hexagon.setOnMouseClicked(event -> handleHexagonClick(event, root));//changed to pass the group dynamically to allow for multiple groups.
                double centerX = hexagon.getLayoutBounds().getCenterX() + hexagon.getLayoutX();
                double centerY = hexagon.getLayoutBounds().getCenterY() + hexagon.getLayoutY();
                Point2D center = new Point2D(centerX, centerY); //2-d coordinates stored in Point2d
                root.getChildren().add(hexagon);

                //-----------------------------------------------------------this section is devtools, only for checking the hexCellID.
                Text hexIdText = new Text(String.valueOf(hexCellId));
                hexIdText.setFont(new Font(20));
                double textWidth = hexIdText.getLayoutBounds().getWidth();
                double textHeight = hexIdText.getLayoutBounds().getHeight();


                hexIdText.setX(center.getX() - (textWidth/2));
                hexIdText.setY(center.getY() + (textHeight/3));
                hexIdText.setFill(Color.RED);

                //root.getChildren().add(hexIdText);
                //------------------------------------------------------------------------------------------

                hexCellsMap.put(hexCellId++, center);
            }
            col -= 1;
            xStartHex += (int) 34.5;
            yStartHex += 59;
        }
    }

    public static Map<Integer, Point2D> getHexCellsMap() {
        return hexCellsMap;
    }

    static void printHexCellsAndCenters() {
        System.out.println("Hex Cells IDs and Their Center Coordinates:");
        for (Map.Entry<Integer, Point2D> entry : hexCellsMap.entrySet()) {
            Integer id = entry.getKey();
            Point2D center = entry.getValue();
            System.out.println("Hex Cell ID: " + id + " - Center: (" + center.getX() + ", " + center.getY() + ")");
        }
    }



    public int gethexCellMapSize() { //getting size of hex cell linked hash map.
        return this.hexCellsMap.size();
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
        hexagon.setStroke(BACKGROUND_COLOR);
        hexagon.setStrokeWidth(2);
        hexagon.setStrokeType(StrokeType.INSIDE);
        return hexagon;
    }


    static private void handleHexagonClick(MouseEvent event, Group targetGroup) {
        if (atomCount < MAX_ATOMS) {
            Polygon clickedHexagon = (Polygon) event.getSource();
            Circle existingAtom = findAtomInHexagon(targetGroup, clickedHexagon);
            if (existingAtom == null) {
                double centerX = clickedHexagon.getLayoutBounds().getCenterX() + clickedHexagon.getLayoutX();
                double centerY = clickedHexagon.getLayoutBounds().getCenterY() + clickedHexagon.getLayoutY();
                Circle atom = createAtom(centerX, centerY);

                // circles of influence
                Circle circOfInf = new Circle(centerX, centerY, 70);
                circOfInf.setFill(Color.TRANSPARENT);
                circOfInf.setStroke(Color.GOLD);
                circOfInf.setStrokeWidth(1);
                circOfInf.getStrokeDashArray().addAll(5d, 5d);

                circOfInf.setStrokeLineCap(StrokeLineCap.ROUND);

                if (!Main.isExperimenter) {
                    targetGroup.getChildren().addAll(circOfInf, atom);
                }
                // circles of influence

                else {
                    targetGroup.getChildren().add(atom);
                }

                atomCount++;
                atom.setOnMouseClicked(atomEvent -> {
                    if (!Main.isExperimenter) {
                        targetGroup.getChildren().removeAll(circOfInf, atom);
                    }

                    else {
                        targetGroup.getChildren().remove(atom);
                    }
                    atomCount--;
                });
            }
        }
    }


    private static void addHoverEffectHex(Polygon hexagon) {
        hexagon.setOnMouseEntered(event ->
                hexagon.setFill(Color.LIGHTGOLDENRODYELLOW));
                Tooltip tooltip = new Tooltip("ℹ Click on a hex cell to place an atom guess. To remove, click on the atom again. Either 4 or 6 atoms can be placed as guesses.");
        tooltip.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-opacity: 0.7;-fx-font-family: 'Droid Sans Mono';-fx-font-weight: bold;");
        tooltip.setMaxWidth(150);
        tooltip.setWrapText(true);
        tooltip.setShowDelay(Duration.millis(170));
        Tooltip.install(hexagon, tooltip);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            tooltip.hide();
        }));
        timeline.play();
        hexagon.setOnMouseExited(event -> hexagon.setFill(Color.BLACK));
    }


}