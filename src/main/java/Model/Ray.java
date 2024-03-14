package Model;

import java.util.ArrayList;
import java.util.List;

/* Ray class will contain :
1.stores entry point of a ray
2.calculates and stores path if the ray
3.determines if ray is absorbed
4.checks for exit point for if not absorbed, at entry point */
public class Ray {
    //reference to the game board for path and check for atoms
    private BlackBoxBoard board;
    //entry point of ray on the game board
    private BlackBoxBoard.Point3D entryPoint;
    //we will store the path ray takes using a list
    private List<BlackBoxBoard.Point3D> path;

    //to check whether ray is absorbed by an atom
    private boolean isAbsorbed;

    public Ray(BlackBoxBoard board, BlackBoxBoard.Point3D entryPoint){
        this.board = board; //board ref
        this.entryPoint = entryPoint; //starting point
        this.path = new ArrayList<>();
        this.isAbsorbed = false; //at the start ray is not absorbed
        //call method to make sure every time a ray object is created its path is calculated immediately
        calculatePath();


    }
    public boolean isAbsorbed() {

        return this.isAbsorbed;
    }


    //check for atom
    private boolean checkForAtom(BlackBoxBoard.Point3D point){
        //use getHexCell method to check if a ray's current position has encountereed an atom
        HexCell cell = board.getCell(point);// get HexCell at given point.
        return cell != null && cell.hasAtom();// check if the cell has an atom

    }



    //calculate path  of ray from entry point (validate whether its edge cell when making a ray object)
    private  void calculatePath(){
        //start path at entry point
        this.path.add(this.entryPoint);

        //check if at the entry point ray encounters an atom/ direct hit at entry
        if(checkForAtom(this.entryPoint)){
            this.isAbsorbed = true;  //if checkForAtom true then it's a direct hit, and we mark ray as absorbed

        } else {
            //assume ray exits directly opposite entry point if no atom is encountered

            BlackBoxBoard.Point3D exitPoint = calculateExitPoint(entryPoint);
            this.path.add(exitPoint);

        }

    }
    private BlackBoxBoard.Point3D calculateExitPoint(BlackBoxBoard.Point3D entryPoint) {
        // Directly invert the entry point's coordinates to find the exit point
        int exitX = -entryPoint.x;
        int exitY = -entryPoint.y;
        int exitZ = -entryPoint.z;

        // Since all valid points on  board satisfy x + y + z = 0,
        // the inverted coordinates will also satisfy this condition and represent
        // a valid point on the opposite side of the board.
        // the calculated exit point would also be on the opposite edge.
        return new BlackBoxBoard.Point3D(exitX, exitY, exitZ);
    }

      // string representation of rays path
    public String getPath() {
        StringBuilder sb = new StringBuilder();
        //iterate over points in path list
        for (BlackBoxBoard.Point3D point : this.path) {
            sb.append(point.toString()).append(" -> ");
        }
        return sb.toString();
    }


}
