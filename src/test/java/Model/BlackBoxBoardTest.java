package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BlackBoxBoardTest {
    private BlackBoxBoard board;  // declare  the board variable

    // set up a new board before each test
    @BeforeEach
    public void setUp() {

        BlackBoxBoard.randomAtoms.clear(); // clear  random atoms at the start of each test cos of its static nature  to make sure we get 6 atoms
        board = new BlackBoxBoard();  //  initialize the board
    }

    // test to ensure the board initializes with the correct number of hex cells
    @Test
    public void testHexCellCount() {
        int count = board.getBoardSize();  // gets the number of hex cells from the board
        assertEquals(61, count, "Board should have exactly 61 hex cells.");
    }

    // test that the correct number of atoms are randomly placed on the board
    @Test
    public void testRandomAtomPlacement() {
        assertEquals(6, BlackBoxBoard.randomAtoms.size(), "Should place exactly 6 atoms.");

    }

    // tests coordinates within the board limits
    @Test
    public void testCoordinateValidity() {
        assertTrue(board.isValidCoordinate(0, 0, 0), "Center point should be valid.");
        assertFalse(board.isValidCoordinate(5, 0, -5), "Coordinate should be invalid.");
    }

    // test the functionality of getting hexcells by coordinates
    @Test
    public void testCellGetter() {
        BlackBoxBoard.Point3D centerPoint = new BlackBoxBoard.Point3D(0, 0, 0);
        assertNotNull(board.getCell(centerPoint), "Should not return null.");
    }

    // tests that placed atoms are correctly registered on the board
    @Test
    public void testAtomPlacement() {
        BlackBoxBoard.Point3D testPoint = new BlackBoxBoard.Point3D(1, -1, 0);  // point for testing
        board.placeAtom(testPoint);  // place an atom at the point
        HexCell cell = board.getCell(testPoint);  // get the cell from the board
        assertTrue(cell.hasAtom(), "Cell should have an atom after setting one.");
    }
}
