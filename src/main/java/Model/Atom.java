package Model;
/* Atom Class:
1.This will for now contain info about atoms position.
2.state management for later on ray hit tracking
3.equals method 
 */

public class Atom {

    private final int x;
    private final int y;
    private final int z;


    public Atom(int x, int y, int z) {
        //more validation will be added as game progress
        //only one atom per cell so if one cell is accidentally set with two atoms not allowed implement this in gameboard
        //an atom can only be placed in a valid location where co-ord must sum to 0
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


}




    /*will update as game logic progresses.
       1.such as how ray interacts with atom
       2.state whether hit by ray
       3.atom as has been guessed by the experimenter

     */


    /* storing of the atoms
     1.using a hast set
     2. good it has methods contains and add to prevent atom duplicates(prevents atoms beig added to cells that already contain atom) and to add atoms
     3. we can check ray interactions  that happen to our atom super easily especially if we are using hashmap for our  board
       */






