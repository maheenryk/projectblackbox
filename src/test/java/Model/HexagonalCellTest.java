package Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HexagonalCellTest {
    HexCell cell;
    BlackBoxBoard.Point3D point;
    BlackBoxBoard.Point3D CIPoint;

    // set up before each test creates a new cell and points

    @BeforeEach
    void setUp() {
        cell = new HexCell();
        point = new BlackBoxBoard.Point3D(0, 0, 0);
        CIPoint = new BlackBoxBoard.Point3D(1, -1, 0);
    }

    // test to check cell state at the start before atoms are placed

    @Test
    void testCellState() {
        assertFalse(cell.hasAtom(), "Cell should not have an atom at the start");
        assertTrue(cell.getCIPoints().isEmpty(), "CI Points list should be empty at the start");
    }

    //check an atom can be set
    @Test
    void testSetAtom() {
        cell.setAtom(new Atom());
        assertTrue(cell.hasAtom(), "Cell should have an atom after setting one");
    }

    // test to check CI points can be added and gotten

    @Test
    void testAddCIPoints() {
        cell.setCIPoints(CIPoint);
        assertFalse(cell.getCIPoints().isEmpty(), "CI Points list should not be empty after adding a point");
        assertEquals(1, cell.getCIPoints().size(), "There should be one CI point");
        assertEquals(CIPoint, cell.getCIPoints().get(0), "The CI point should match the one added");
    }

    //test to check if  cell positions for edge and corner are valid

    @Test
    void testEdgeAndCornerCellDetection() {
        assertFalse(HexCell.isEdgeCell(new BlackBoxBoard.Point3D(0, 0, 0)), "Center point shouldn't be an edge cell");
        assertTrue(HexCell.isEdgeCell(new BlackBoxBoard.Point3D(4, 0, -4)), "This should be  an edge cell");
        assertTrue(HexCell.isCornerCell(new BlackBoxBoard.Point3D(4, 0, -4)), "This should be a corner cell");
    }
}
