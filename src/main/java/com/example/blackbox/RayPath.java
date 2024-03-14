package com.example.blackbox;

import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
public class RayPath extends Pane {

    private Line rayPath;
    private Circle rayCircle;
    private PathTransition pathTransition;

    public RayPath(double startX, double startY, double endX, double endY) {

        rayPath = new Line(startX, startY, endX, endY);
        rayPath.setStroke(Color.TRANSPARENT);

        rayCircle = new Circle(5, Color.DEEPSKYBLUE);

        getChildren().addAll(rayPath, rayCircle);


        pathTransition = new PathTransition();
        pathTransition.setNode(rayCircle);
        pathTransition.setPath(rayPath);
        pathTransition.setCycleCount(pathTransition.INDEFINITE);
        pathTransition.setDuration(Duration.seconds(3));

        pathTransition.play();
    }

    public void startAnimation() {
        pathTransition.play();
    }

    public void stopAnimation() {
        pathTransition.stop();
    }
}
