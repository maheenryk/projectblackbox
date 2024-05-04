package com.example.blackbox;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import java.util.List;
import java.util.ArrayList;
import Model.Ray;
import Model.BlackBoxBoard;


public class GameReveal {
    // Method to draw the setters  atoms on the board
    public static void displayAtom(Point2D position, Group targetGroup) {
        Circle atom = new Circle(position.getX(), position.getY(), 20);
        atom.setFill(Color.RED); //colour for atoms
        targetGroup.getChildren().add(atom);
    }

    // Method to reveal all atoms at the end of a game by iterating through setters atoms
    public static void revealSetterAtoms(List<Point2D> atomPositions, Group targetGroup) {
        for (Point2D position : atomPositions) {
            displayAtom(position, targetGroup);
        }
    }
    // Method to draw a single ray path takes in entry point and exit point to draw the path





}
