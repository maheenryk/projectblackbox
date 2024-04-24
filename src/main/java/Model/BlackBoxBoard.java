package Model;
import java.util.*;


public class BlackBoxBoard {

    /* creating custom Point3D class to make connecting view (JavaFX) and model efficient.
    co-ord mapping shouldn't be done only in JavaFX using Point3D as goes against layer abstraction.
     -> therefore, custom Point3D class holding 3-d co-ords to suit cubic hexagonal mapping system.*/
    public static class Point3D {
        public final int x;
        public final int y;
        public final int z;

        //Point3D constructor
        public Point3D(int x, int y, int z) {
            if (x + y + z != 0) {
                throw new IllegalArgumentException("Invalid Co-Ordinates");
            }
            this.x = x;
            this.y = y;
            this.z = z;
        }

        /*
        -- [[not sure if required anymore]]. --
        must override to ensure distinct hashcode for unique combinations of x, y, z. */
       @Override
        public int hashCode() {
            return java.util.Objects.hash(x, y, z);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;//if same instance
            if (o == null || getClass() != o.getClass()) return false; //if null or not instance of same class
            Point3D point3D = (Point3D) o;
            return x == point3D.x && y == point3D.y && z == point3D.z;//checking fields in objects for equivalence
        }

        //hashcode is printed without overriding, so toString method ust be overridden for Point3D object.
        @Override
        public String toString() {
            return "Point3D(" + "x=" + x + ", y=" + y + ", z=" + z + ')';
        }
    }


    //defining board as Map structure with co-ords as key and hex cell as value.
    private final Map<Point3D, HexCell> board;

    //BB-board constructor
    public BlackBoxBoard() {
        this.board = new LinkedHashMap<>();
        initializeBoard();
    }

    //nested for loops iterate within [-4, 4] range and fill hashmap with key-value pairs.
    private void initializeBoard() {
        for (int y = -4; y <= 4; y++) /* y prints as the directional invariable so that the cells
        are printed top-down, row by row*/
            for (int x = -4; x <= 4; x++) {
                for (int z = -4; z <= 4; z++) { //the printing of x and z determines the flow of hexcell generation from left to right.

                    if (x + y + z == 0) { // valid co-ords must sum to 0
                        Point3D point = new Point3D(x, y, z);
                        HexCell cell = new HexCell();
                        board.put(point, cell);
                    }
                }
            }
    }

    //method to print board entries (all key + value pairs)
    public void printBoard() {
        System.out.println("HashMap Contents:");
        for (Map.Entry<Point3D, HexCell> entry : board.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue() + "\n");
        }
    }

    //method to get the size of the board (no. of hex cells). [needed for testing]
    public int getBoardSize() {
        return this.board.size();
    }

//    public Set<Map.Entry<Point3D, HexCell>> getBoardEntries() { //method to access the board safely without modifying.
//        return Collections.unmodifiableSet(board.entrySet());//attempts to add/remove entries will result in UnsupportedOperationException.
//    }

    public Iterator<Map.Entry<Point3D, HexCell>> getBoardIterator() {
        return this.board.entrySet().iterator();
    }


    public boolean isValidCoordinate(int x, int y, int z) {
        return (x >= -4 && x <= 4) && (y >= -4 && y <= 4) && (z >= -4 && z <= 4);
    }

    public static int rayCount = 0;

    public static int rayMarkers = 0;

    public int getRayCount() {
        return rayCount;
    }

    public int getRayMarkers() {
        return rayMarkers;
    }

    public void placeSetterAtoms(List<Point3D> setterAtomList) { //updating the board with the setter's Atoms.
        for (Point3D point3d : setterAtomList) {
            HexCell hexCell = board.get(point3d);
            if (hexCell != null && !hexCell.hasAtom()) {
                placeAtom(point3d);
            }
        }
    }

    public void placeAtom(Point3D point) {
        int CIx = point.x;
        int CIy = point.y;
        int CIz = point.z;

        // Place an atom at this point if it doesn't already have one
        HexCell hexCell = board.get(point);
        if (!hexCell.hasAtom()) {
            hexCell.setAtom(new Atom());
            System.out.println("\nPlaced an atom at: " + point);

            // create CIPoints, validate them, and set them
            // Define offsets for each CI Point
            int[][] offsets = {
                    {1, -1, 0}, // Point 1
                    {1, 0, -1}, // Point 2
                    {0, 1, -1}, // Point 3
                    {-1, 1, 0}, // Point 4
                    {-1, 0, 1}, // Point 5
                    {0, -1, 1}  // Point 6
            };

            // Iterate through the offsets and place CI Points
            for (int[] offset : offsets) {
                int newCIx = CIx + offset[0];
                int newCIy = CIy + offset[1];
                int newCIz = CIz + offset[2];
                // System.out.println("Attempting CI Point at: " + newCIx + ", " + newCIy + ", " + newCIz);
                if (isValidCoordinate(newCIx, newCIy, newCIz)) {
                    Point3D CIPoint = new Point3D(newCIx, newCIy, newCIz);
                    HexCell ciCell = getCell(CIPoint);
                    //check if the hexcell is null before attempting to set coIP
                    if (ciCell != null) {  // Check if the HexCell is not null
                        ciCell.setCoIP(new CoIP());
                        ciCell.setCIPoints(CIPoint);
                        System.out.println("Placed CI Point at: " + CIPoint);
                    } else {
                        System.out.println("No HexCell found at: " + CIPoint + ", unable to place CI Point.");
                    }
                }
            }

        }
    }

    //method to place our atoms in cells randomly while staying within boards range  at the start of game
    public void placeRandomAtoms(int numberOfAtoms) {
        // create a list from the keys of the board HashMap, which are the valid positions
        List<Point3D> validPoints = new ArrayList<>(board.keySet());

        // shuffle the list to randomise the order of points
        Collections.shuffle(validPoints);

        //we can loop through the shuffled list and take the first numberOfAtoms points to place atoms
        for (int i = 0; i < numberOfAtoms; i++) {
            Point3D point = validPoints.get(i);
            placeAtom(point);
        }
    }

    public void printCIPoints() {
        System.out.println("\nCI Points:");
        for (Map.Entry<Point3D, HexCell> entry : board.entrySet()) {
            Point3D point = entry.getKey();
            HexCell hexCell = entry.getValue();
            List<Point3D> CIPoints = hexCell.getCIPoints();
            for (Point3D CIPoint : CIPoints) {
                System.out.println("Atom at " + point + " has CI Point: " + CIPoint);
            }
        }
    }


    // method to retrieve HexCell to use in ray class when interacts with board
    public HexCell getCell(BlackBoxBoard.Point3D point) {
        return this.board.get(point);
    }
}






