
package com.example.blackbox;

import javafx.scene.Group;
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
    private Color originalColor;
    private Color clickedColor = Color.RED;
    private Color hoverColor = Color.BLACK;
    private static RayCircle currentlyClicked; //to store clicked state of ray circle.



    RayCircle(double radius, Color fill) {

        this.originalColor = fill;

        circle = new Circle(radius);
        circle.setFill(fill);
        circle.setStroke(Color.BLACK);
        circle.setStrokeType(StrokeType.INSIDE);

        // text for ray number
        rayText = new Text();
        rayText.setFont(Font.font("Roboto Slab", 12));
        rayText.setFill(Color.WHITE);

        addHoverEffect();
        addClickEffect();

        // adding circle and text to StackPane
        getChildren().addAll(circle, rayText);
    }



    private void addClickEffect() {
        this.setOnMouseClicked(event -> {
            if (currentlyClicked != null && currentlyClicked != this) {
                //returning the original colour of the raycircle
                currentlyClicked.circle.setFill(currentlyClicked.originalColor);
            }
            //changing colour
            circle.setFill(clickedColor);
            // update the current clicked
            currentlyClicked = this;
        });
    }


    private void addHoverEffect() {

        this.setOnMouseEntered(event -> {
            //only apply hover if not clicked
            if (currentlyClicked != this) {
                circle.setFill(hoverColor);
            }
        });
        this.setOnMouseExited(event -> { //upon mouse exit
            //return to original colour upon exit only if the circle is not clicked
            if (currentlyClicked != this) {
                circle.setFill(originalColor);
            }
            //if clicked, then the clicked colour remains upon exit
            else {
                circle.setFill(clickedColor);
            }
        });
    }

    public void setRayText(String text) {
        rayText.setText(text);
    }

    public Text getRayText() {
        return rayText;
    }

    static void generateRayCircles(Group root) { //method for generating nodes (ray circles)
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


    static RayCircle createRayCircle(double layoutX, double layoutY, int number) {
        RayCircle circle = new RayCircle(12.0, Color.web("#4242ff"));
        circle.setLayoutX(layoutX);
        circle.setLayoutY(layoutY);

        // Set ray number text
        circle.setRayText(String.valueOf(number));

        return circle;
    }

    public static int getCurrentlyClickedRayNumber() {
        if (currentlyClicked != null && currentlyClicked.rayText != null) {
            try {
                return Integer.parseInt(currentlyClicked.rayText.getText());
            } catch (NumberFormatException e) {
                System.err.println("Error parsing currently clicked RayCircle number: " + e.getMessage());
                return -1; // Return an invalid number if parsing fails
            }
        } else {
            return -1; // Return an invalid number if no RayCircle is currently clicked
        }
    }
}
