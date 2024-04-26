
package com.example.blackbox;

import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.paint.Color;
import java.util.List;
import java.util.Arrays;


public class RayCircle extends StackPane {
    private static final Map<Integer, RayCircle> rayCircleMap = new HashMap<>();
    private Circle circle;
    private Text rayText;
    private Color originalColor;
    private Color clickedColor = Color.RED;
    private Color hoverColor = Color.GRAY;
    private Color clickedHover = Color.GRAY;
    private static RayCircle currentlyClicked; //to store clicked state of ray circle.
    private boolean colorLocked = false;//flag to lock ray marker colour changes.
    private static int currentColorIndex = 0;  //track the color index
    private static final List<Color> rayMarkerColors = Arrays.asList( //list of unique identifiable colours for ray markers.

            Color.rgb(0, 140, 86), Color.rgb(255, 200, 0), Color.rgb(227, 0, 235),
            Color.rgb(255, 102, 51), Color.rgb(101, 201, 170), Color.rgb(255, 157, 0),
            Color.rgb(149, 0, 207),Color.rgb(136, 181, 45),Color.rgb(99, 69, 196),
            Color.rgb(255, 120, 187),Color.rgb(159, 192, 209),Color.rgb(255, 184, 122),
            Color.rgb(179, 255, 0),Color.rgb(53, 70, 110),Color.rgb(255, 25, 94),
            Color.rgb(255, 221, 69),Color.rgb(86, 105, 92),Color.rgb(32, 36, 46),
            Color.rgb(148, 108, 0),Color.rgb(148, 64, 0), Color.rgb(82, 8, 0),
            Color.rgb(171, 70, 100),Color.rgb(0, 145, 128),Color.rgb(66, 27, 0),
            Color.rgb(173, 173, 102),Color.rgb(210, 176, 255),Color.rgb(207, 255, 213),
            Color.rgb(166, 97, 60),Color.rgb(48, 30, 44),Color.rgb(87, 99, 186)
    );

    public void setTextColor(Color color) { //using to change text black against white background ray circle.
        rayText.setFill(color);
    }

    public static Color getNextColor() {
        Color color = rayMarkerColors.get(currentColorIndex);
        currentColorIndex = (currentColorIndex + 1) % rayMarkerColors.size();  //move to next index, wrap around if at end
        return color;
    }

    public void setColor(Color color) {
        circle.setFill(color);
    }


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
            if (currentlyClicked != null) {
                if (!currentlyClicked.colorLocked) {
                    currentlyClicked.circle.setFill(currentlyClicked.originalColor);
                }
            }
            currentlyClicked = this;
            if (!colorLocked) {
                circle.setFill(clickedColor);
            }
        });
    }


    private void addHoverEffect() {
        this.setOnMouseEntered(event -> {
            if (!colorLocked) {
                if (currentlyClicked != this) {
                    circle.setFill(hoverColor);
                } else {
                    circle.setFill(clickedHover);
                }
            }
        });

        this.setOnMouseExited(event -> {
            if (!colorLocked) {
                if (currentlyClicked != this) {
                    circle.setFill(originalColor);
                } else {
                    circle.setFill(clickedColor);
                }
            }
        });
    }
    public void setPermanentColor(Color color) {
        circle.setFill(color);
        colorLocked = true;  // Lock the color to prevent further changes
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
        rayCircleMap.put(number, circle);

        return circle;
    }
    public static RayCircle findRayCircleByNumber(int rayNodeNumber) {
        return rayCircleMap.get(rayNodeNumber);
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

//    List<Color> rayMarkerColors = Arrays.asList(
//            Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PURPLE, Color.BROWN, Color.HOTPINK, Color.LIGHTPINK,
//            Color.DARKBLUE, Color.LIGHTBLUE, Color.CORAL
//    );
//    Random random = new Random();
//    Color randomColor = rayMarkerColors.get(random.nextInt(rayMarkerColors.size()));


}
