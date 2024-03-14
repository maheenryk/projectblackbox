package com.example.blackbox;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HexagonClickHandlerTest {

    @Test
    public void testHandleHexagonClick() {
        Main hexagonClickHandler = new Main();

        AnchorPane root = new AnchorPane();
        Polygon clickedHexagon = new Polygon(/* define hexagon points */);
        MouseEvent click = new MouseEvent(
                MouseEvent.MOUSE_CLICKED,  // EventType
                0, 0, 0, 0, MouseButton.PRIMARY, 1,
                false, false, false, false,
                true, false, false, false,
                false, false, null
        );


        // Call the method to be tested
        hexagonClickHandler.handleHexagonClick(click);

        // Assert that the expected changes have occurred
        assertEquals(1, hexagonClickHandler.getAtomCount()); // Assuming you have a method to get atomCount
        assertEquals(1, root.getChildren().size()); // Assuming you have a method to get the number of children
        assertTrue(root.getChildren().get(0) instanceof Circle); // Assuming the first child is a Circle
    }
}
