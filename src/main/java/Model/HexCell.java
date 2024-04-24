package Model;

/* Define hex cell in our board
   Properties of HexCell :
    1.positions?
    2.create a reference to atom class to set or check if hex cell ha an atom present

 */


import java.util.ArrayList;
import java.util.List;

public class HexCell {
    /* 1.define instance variables
       2. make them immutable, so they cannot be changed once set.
    */

    /*private final int x;
      private final int y;
      private final int z;
      hexagon with atom present or not.
      also final as once game starts twe cannot add or remove atoms once they have been set.
    */

    //create an instance variable of type atom.
    private Atom atom;

    private CoIP coIP;

    private static int lastCellId = 0;

    // Instance variable for the object's ID
    private final int cellId;

    private List<BlackBoxBoard.Point3D> CIPoints;

    public HexCell() {
        //at the start all hex cells do not contain an atom
        this.atom = null;
        this.coIP = null;
        this.CIPoints = new ArrayList<>();
//        if (lastCellId > 61) {
//            throw new IllegalStateException("Cell Id exceeds the 61 max count of hex cells.");
//        }
        this.cellId = ++lastCellId;
    }

    //set atom
    public void setAtom(Atom atom) {
        this.atom = atom;
    }
    public boolean hasAtom() {
        return this.atom != null;
    }

    public boolean hasCIPoint() {
        return this.coIP != null;
    }

    public void setCIPoints(BlackBoxBoard.Point3D CIPoint) {
        this.CIPoints.add(CIPoint);
    }

    public void setCoIP (CoIP coIP) {
        this.coIP = coIP;
    }

    public List<BlackBoxBoard.Point3D> getCIPoints() {
        return this.CIPoints;
    }

    public int getCellId() {
        return cellId;
    }

    //check if a cell is an edge cell
    public static boolean isEdgeCell(BlackBoxBoard.Point3D point) {
        // an edge cell will be where  one coordinate is at its maximum or minimum value
        return  point.x == -4 || point.x == 4 ||
                point.y == -4 || point.y == 4 ||
                point.z == -4 || point.z == 4;
    }
    public static boolean isCornerCell(BlackBoxBoard.Point3D cell){
        return (cell.x == 0 && cell.y == -4 && cell.z == 4) ||
                (cell.x == 4 && cell.y == -4 && cell.z == 0) ||
                (cell.x == 4 && cell.y == 0 && cell.z == -4) ||
                (cell.x == 0 && cell.y == 4 && cell.z == -4) ||
                (cell.x == -4 && cell.y == 4 && cell.z == 0) ||
                (cell.x == -4 && cell.y == 0 && cell.z == 4);
    }

    //returns a string rep of our HexCell

    @Override
    public String toString() {
        String atomCheck;
        if(this.atom == null){
            atomCheck= "Atom is not present. ";
        }else{
            atomCheck = "Atom is ***PRESENT***. ";
        }

        String ciCheck;
        if (this.coIP == null) {
            ciCheck = "CI Point is not present.";
        }
        else {
            ciCheck = "CI Point is +++PRESENT+++";
        }
        return "\nHexCell ID " + cellId + ": " + atomCheck + ciCheck;
    }

}