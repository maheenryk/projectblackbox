package Model;
/*Atom Class:
1.this will for now contain info about atoms position reference to Point3D
2.state management for later on ray hit tracking
3.equals method
 */

public class Atom {
    //reference to co-ordinates

    private final BlackBoxBoard.Point3D position;



    public Atom( BlackBoxBoard.Point3D position) {
        //validation is in point3D

        this.position = position;
    }

    public BlackBoxBoard.Point3D getPosition() {
        return position;
    }


    @Override
    public String toString() {
        // make sure  Point3D class has a proper toString implementation
        return "Atom{ " + "position: " + position + "}";
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



}

