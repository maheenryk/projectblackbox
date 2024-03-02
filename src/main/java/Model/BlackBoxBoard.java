package Model;

import java.util.LinkedHashMap;
import java.util.Map;

public class BlackBoxBoard {

    /* creating custom Point3D class to make connecting view (JavaFX) and model efficient.
    co-ord mapping shouldn't be done only in JavaFX using Point3D as goes against layer abstraction.
     -> thfore, custom Point3D class holding 3-d co-ords to suit cubic hexagonal mapping system.*/
    public static class Point3D {
        public final int x;
        public final int y;
        public final int z;

        //Point3D constructor
        public Point3D(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        /*
        -- [[not sure if required anymore]]. --
        must override to ensure distinct hashcode for unique combinations of x, y, z.
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
        ------
        */

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

    //nested for loops iterate within [-3, 3] range and fill hashmap with key-value pairs.
    private void initializeBoard() {
        for (int z = 3; z >= -3; z--)
          for (int x = -3; x <= 3; x++) {
            for (int y = -3; y <= 3; y++) {

                if(x + y + z == 0) { // valid co-ords must sum to 0
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
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
    }

    //method to get the size of the board (no. of hex cells). [needed for testing]
    public int getBoardSize() {
        return this.board.size();
    }
 }




