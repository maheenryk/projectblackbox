package Model;

import java.util.HashMap;
import java.util.Map;

public class BlackBoxBoard {
    public static class Point3D {
        public final int x;
        public final int y;
        public final int z;

        public Point3D(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
}

    private Map<Point3D, HexCell> board;

    public BlackBoxBoard() {
        this.board = new HashMap<>();
        initializeBoard();
    }
    private void initializeBoard() {

    }
