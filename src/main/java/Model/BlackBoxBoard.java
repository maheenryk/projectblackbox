package Model;

import java.util.HashMap;
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
        this.board = new HashMap<>();
        initializeBoard();
    }

    //nested for loops iterate within [-3, 3] range and fill hashmap with key-value pairs.
    private void initializeBoard() {
        for (int x = -3; x <= 3; x++) {
            for (int y = -3; y <= 3; y++) {
                for (int z = -3; z <= 3; z++) {

                    if(x + y + z == 0){ //x + y + z must equal 0

                    Point3D point = new Point3D(x, y, z);
                    HexCell cell = new HexCell();
                    board.put(point, cell);
                    }

                }
            }
        }
    }
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