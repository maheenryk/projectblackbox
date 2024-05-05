package Model;
import javafx.geometry.Point2D;

import java.util.*;

/**
 * Represents the black box game board for the game. This class manages the
 * hex cells, atom placements, and game board.
 */

public class BlackBoxBoard {

    /**
     * Static list to track randomly placed atoms on the board.
     */
    public static List<Point3D> randomAtoms = new ArrayList<>();

    /**
     * Represents a 3D coordinate  on the hexagonal grid.
     * Ensures that the sum of coordinates (x, y, z) equals zero to validate  placement on the grid.
     */
    public static class Point3D {
        public final int x;
        public final int y;
        public final int z;

        /**
         * Constructs a new Point3D with specified coordinates.
         *
         * @param x The x coordinate.
         * @param y The y coordinate.
         * @param z The z coordinate.
         * @throws IllegalArgumentException if the sum of x, y, and z does not equal zero.
         */

        public Point3D(int x, int y, int z) {
            if (x + y + z != 0) {
                throw new IllegalArgumentException("Invalid Co-Ordinates");
            }
            this.x = x;
            this.y = y;
            this.z = z;
        }

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


        @Override
        public String toString() {
            return "Point3D(" + "x=" + x + ", y=" + y + ", z=" + z + ')';
        }
    }


    //defining board as Map structure with co-ords as key and hex cell as value.
    private final Map<Point3D, HexCell> board;

    /**
     * Initialises the BlackBoxBoard with a set number of random atoms.
     */
    public BlackBoxBoard() {
        this.board = new LinkedHashMap<>();
        initializeBoard();
        placeRandomAtoms(6);
    }


    // list of edge cells ordered in clockwise order to be used for ray node generation
    // could be turned into a generation loop later on
    public static List<Point3D> edgeCells = Arrays.asList(
            new Point3D(0, -4, 4),  new Point3D(1, -4, 3),  new Point3D(2, -4, 2),
            new Point3D(3, -4, 1),  new Point3D(4, -4, 0),  new Point3D(4, -3, -1),
            new Point3D(4, -2, -2), new Point3D(4, -1, -3), new Point3D(4, 0, -4),
            new Point3D(3, 1, -4), new Point3D(2, 2, -4), new Point3D(1, 3, -4),
            new Point3D(0, 4, -4),  new Point3D(-1, 4, -3),  new Point3D(-2, 4, -2),
            new Point3D(-3, 4, -1),  new Point3D(-4, 4, 0),  new Point3D(-4, 3, 1),
            new Point3D(-4, 2, 2),  new Point3D(-4, 1, 3),  new Point3D(-4, 0, 4),
            new Point3D(-3, -1, 4), new Point3D(-2, -2, 4), new Point3D(-1, -3, 4));

    /**
     * Initialises the hex cells within the board.
     */
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

    /**
     * Outputs the current state of the board including all hex cells.
     */
    public void printBoard() {
        System.out.println("HashMap Contents:");
        for (Map.Entry<Point3D, HexCell> entry : board.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue() + "\n");
        }
    }

    /**
     * Returns the size of the board in terms of number of hex cells.
     *
     * @return The number of hex cells on the board.
     */

    public int getBoardSize() {
        return this.board.size();
    }

//    public Set<Map.Entry<Point3D, HexCell>> getBoardEntries() { //method to access the board safely without modifying.
//        return Collections.unmodifiableSet(board.entrySet());//attempts to add/remove entries will result in UnsupportedOperationException.
//    }

    public Iterator<Map.Entry<Point3D, HexCell>> getBoardIterator() {
        return this.board.entrySet().iterator();
    }

    /**
     * Validates if the provided coordinates are within the valid range for the board.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param z The z coordinate.
     * @return true if the coordinates are within the board's limits, false otherwise.
     */
    public boolean isValidCoordinate(int x, int y, int z) {
        return (x >= -4 && x <= 4) && (y >= -4 && y <= 4) && (z >= -4 && z <= 4);
    }



    public static int rayCount = 0;

    public static int rayMarkers = 0;

    /**
     * Gets the count of rays that have been fired.
     *
     * @return the ray count
     */
    public int getRayCount() {
        return rayCount;
    }

    /**
     * Gets the number of ray markers on the board.
     * @return the ray markers count
     */
    public int getRayMarkers() {
        return rayMarkers;
    }

    /**
     * Places atoms specified by the setter on the board.
     *  These atoms are placed only if the cell at the coordinate does not already have an atom.
     * @param setterAtomList a list of Point3D with the coordinates for placing an atom.
     */

    public void placeSetterAtoms(List<Point3D> setterAtomList) { //updating the board with the setter's Atoms.
        for (Point3D point3d : setterAtomList) {
            HexCell hexCell = board.get(point3d);
            if (hexCell != null && !hexCell.hasAtom()) {
                placeAtom(point3d);
            }
        }
    }

    /**
     * Places an atom at a specific point on the board. It also assigns CIPoints.
     * around the placed atom if they fall within valid coordinates.
     * @param point The point on the board where the atom is to be placed.
     */
    public void placeAtom(Point3D point) {
        int CIx = point.x;
        int CIy = point.y;
        int CIz = point.z;


        HexCell hexCell = board.get(point);
        if (!hexCell.hasAtom()) {
            hexCell.setAtom(new Atom());
            //System.out.println("\nPlaced an atom at: " + point);

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
                        //System.out.println("Placed CI Point at: " + CIPoint);
                    } else {
                        System.out.println("No HexCell found at: " + CIPoint + ", unable to place CI Point.");
                    }
                }
            }

        }
    }

    /**
     * Places a specific number of atoms randomly on the board.
     *
     * @param numberOfAtoms The number of atoms to place.
     */

    public void placeRandomAtoms(int numberOfAtoms) {
        // create a list from the keys of the board HashMap, which are the valid positions
        List<Point3D> validPoints = new ArrayList<>(board.keySet());


        // shuffle the list to randomise the order of points
        Collections.shuffle(validPoints);

        //we can loop through the shuffled list and take the first numberOfAtoms points to place atoms
        for (int i = 0; i < numberOfAtoms; i++) {
            Point3D point = validPoints.get(i);
            //placeAtom(point);
            randomAtoms.add(point);
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

    /**
     * Gets a HexCell from the board based on a specific point.
     *
     * @param point The point where the HexCell is located.
     * @return The HexCell at the specified point.
     */
    public HexCell getCell(BlackBoxBoard.Point3D point) {
        return this.board.get(point);
    }
}






