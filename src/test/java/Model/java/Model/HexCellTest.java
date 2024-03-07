package Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HexCellTest {

    // Test the toString method when no atom is present
    @Test
    void toStringNoAtomTest() {
        HexCell cell = new HexCell();
        String expected = "HexCell: atom is not present";
        assertEquals(expected, cell.toString(), "toString should match expected string when no atom is present");
    }

    // Test the toString method when an atom is present
    @Test
    void toStringWithAtomTest() {
        HexCell cell = new HexCell();
        Atom atom = new Atom(); // Create an Atom with valid coordinates
        cell.setAtom(atom);
        String expected = "HexCell: atom is present";
        assertEquals(expected, cell.toString(), "toString should match expected string when an atom is present");
    }
}
