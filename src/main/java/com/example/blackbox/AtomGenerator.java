/**
 *
 *  AtomGenerator.java handles the creating and deleting of atoms inside hex cells. It contains methods for detecting
 *   existing atoms in hex cells and dealing with the user interaction accordingly.
 */

package com.example.blackbox;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;



public class AtomGenerator {

    static final int MAX_ATOMS = 6;
    static int atomCount = 0;

    static Circle createAtom(double centerX, double centerY) {
        Circle atom = new Circle(20, Color.RED);
        atom.setLayoutX(centerX);
        atom.setLayoutY(centerY);
        atom.setUserData("atom"); //setting to identify apart from ray circles.
        return atom;
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

    static Circle findAtomInHexagon(Group root, Polygon hexagon) {

        // Iterate through the children of the root to find an atom in the same hexagon
        for (javafx.scene.Node node : root.getChildren()) {
            if (node instanceof Circle atom) {
                if (isPointInHexagon(atom.getLayoutX(), atom.getLayoutY(), hexagon)) {
                    //System.out.println("Atom at coords: (" + atom.getLayoutX() + ", " + atom.getLayoutY() + ")");//testing purposes
                    return atom;
                }
            }
        }
        return null;

    }



}

