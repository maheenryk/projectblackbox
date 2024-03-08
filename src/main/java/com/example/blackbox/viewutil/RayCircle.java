package com.example.blackbox.viewutil;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class RayCircle extends StackPane {
    private Circle circle;
    private Text rayText;

    public RayCircle(double radius, Color fill) {

        circle = new Circle(radius);
        circle.setFill(fill);
        circle.setStroke(Color.BLACK);
        circle.setStrokeType(StrokeType.INSIDE);

        // text for ray number
        rayText = new Text();
        rayText.setFont(Font.font("Roboto Slab", 12));
        rayText.setFill(Color.WHITE);

        addHoverEffect();

        //StackPane.setAlignment(rayText, javafx.geometry.Pos.CENTER);
        // adding circle and text to StackPane
        getChildren().addAll(circle, rayText);
    }


    private void addHoverEffect() { //method for adding hover effect to custom RayCircle class.
        Color originalColor = (Color) circle.getFill();
        Color hoverColor = Color.DEEPPINK;
        circle.setOnMouseEntered((MouseEvent event) -> circle.setFill(hoverColor));
        circle.setOnMouseExited((MouseEvent event) -> circle.setFill(originalColor));
        rayText.setOnMouseEntered((MouseEvent event) -> circle.setFill(hoverColor));
        rayText.setOnMouseExited((MouseEvent event) -> circle.setFill(originalColor));
    }
    public void setRayText(String text) {
        rayText.setText(text);
    }

    public Text getRayText() {
        return rayText;
    }
}