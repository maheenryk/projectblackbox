package Model;
/* Atom Class:
1.This will for now contain info about atoms position.
 */

public class Atom {

    private final int x;
    private final int y;
    private final int z;


    public Atom(int x, int y, int z) {
        if(x + y + z != 0){
            throw  new IllegalArgumentException("Invalid co-ordinates");
        }
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "Atom{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    //will update as game logic progresses.

}

