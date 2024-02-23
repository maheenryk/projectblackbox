package Model;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BlackBoxBoardTest {


    @Test
    public void testHexCellCount() {
        BlackBoxBoard board = new BlackBoxBoard();
        int cellCount = board.getBoardSize(); // Implement getBoardSize in BlackBoxBoard

        assertEquals(37, cellCount, "Board size should equal exactly 37 hex cells.");
    }



}