package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* Ray class :
*/
public class Ray {
    //reference to the game board for path and check for atoms
    private BlackBoxBoard board;
    //entry point of ray on the game board
    private BlackBoxBoard.Point3D entryPoint;
    //we will store the path ray takes using a list
    private List<BlackBoxBoard.Point3D> path;

    //to check whether ray is absorbed by an atom
    private boolean isAbsorbed;

    private boolean deflected60;

    private boolean deflected120;

    private boolean rayReversed;

    public Ray(BlackBoxBoard board, BlackBoxBoard.Point3D entryPoint, String dir){
        this.board = board; //board ref
        this.entryPoint = entryPoint; //starting point
        this.path = new ArrayList<>();
        this.isAbsorbed = false; //at the start ray is not absorbed
        this.deflected60 = false;
        this.deflected120 = false;
        this.rayReversed = false;
        //call method to make sure every time a ray object is created its path is calculated immediately
        calculatePath(dir);


    }
    public boolean isAbsorbed() {
        return this.isAbsorbed;
    }

    public boolean isDeflected60() {
        return this.deflected60;
    }

    public boolean isDeflected120() { return this.deflected120; }

    public boolean isRayReversed() { return this.rayReversed; }


    //check for atom
    private boolean checkForAtom(BlackBoxBoard.Point3D point){
        //use getHexCell method to check if a ray's current position has encountered an atom
        HexCell cell = board.getCell(point);// get HexCell at given point.
        return cell != null && cell.hasAtom();// check if the cell has an atom

    }

    //check if a cell is an edge cell
    private boolean isEdgeCell(BlackBoxBoard.Point3D point) {
        // an edge cell will be where  one coordinate is at its maximum or minimum value
        return  point.x == -4 || point.x == 4 ||
                point.y == -4 || point.y == 4 ||
                point.z == -4 || point.z == 4;
    }




    //calculate path  of ray from entry point (validate whether its edge cell when making a ray object)
    private  void calculatePath(String dir){
        // calculation if ray is immediately reflected
        if(isEdgeCell(entryPoint)){
            HexCell cell = board.getCell(entryPoint);
            if(cell != null && cell.hasCIPoint()){
                //if it has a CI we will find the cells on the edge its next to and find out if these cells have atoms o n edge
                if(isRayReflectedAtEdge(entryPoint)){
                    rayReversed = true;
                    path.add(entryPoint);
                    return; //end method early since the ray is reflected
                }

            }
        }
        //if ray is not immediately reflected
        //start path at entry point
        this.path.add(this.entryPoint);

        // initialize list to store cells visited by the ray
        List<BlackBoxBoard.Point3D> visitedCells = new ArrayList<>();
        visitedCells.add(this.entryPoint);

        // current position of the ray
        BlackBoxBoard.Point3D currentPosition = this.entryPoint;

        // iterate until the ray is absorbed or reaches edge of board
        // edge of board argument included in the end so the loop
        // doesn't break due to the entry point being on edge of board
        while (!isAbsorbed) {
            // Calculate the next position based on the current position and direction
            BlackBoxBoard.Point3D nextPosition = calculateNextPosition(currentPosition, dir);

            // Add the next position to the path
            this.path.add(nextPosition);

            // Check if the next position is a CI point
            HexCell cell = board.getCell(nextPosition);

            if (cell != null && cell.hasCIPoint()) {

                String result = newPath(currentPosition, dir);

                if (Objects.equals(result, "Absorbed")) {
                    isAbsorbed = true;
                    break;
                }

                else if (Objects.equals(result, "Error")) {
                    System.out.println(result);
                    break;
                }

                else {
                    dir = result;
                }
            }

            // Update current position
            currentPosition = nextPosition;

            // Add the current position to the visited cells list
            visitedCells.add(currentPosition);

            // check if ray is on edge of board and break loop if true
            if (hasReachedBoardEdge(currentPosition)) {
                break;
            }
        }


    }

    public boolean hasCIPoint(BlackBoxBoard.Point3D point) {
        HexCell cell = board.getCell(point);
        return cell != null && cell.hasCIPoint();
    }
    // Checking for the presence of atoms at specific positions compared to the rays current position.
    // Depending on where atoms are present around the ray, the rays direction can  be deflected,absorbed or reversed
    private String newPath (BlackBoxBoard.Point3D position, String dir) {

        String newDir = "Error";

        int CIx = position.x;
        int CIy = position.y;
        int CIz = position.z;

        int aGx = CIx;
        int aGy = CIy;
        int aGz = CIz;

        int aOx = CIx;
        int aOy = CIy;
        int aOz = CIz;

        int aPx = CIx;
        int aPy = CIy;
        int aPz = CIz;

        BlackBoxBoard.Point3D greenAtom = new BlackBoxBoard.Point3D(aGx, aGy, aGz);
        BlackBoxBoard.Point3D orangeAtom = new BlackBoxBoard.Point3D(aOx, aOy, aOz);
        BlackBoxBoard.Point3D pinkAtom = new BlackBoxBoard.Point3D(aPx, aPy, aPz);

        boolean atomGreen = false;
        boolean atomPink = false;
        boolean atomOrange = false;

        switch (dir) {

            case "YL":

                aGx = CIx;
                aGy = CIy - 1;
                aGz = CIz + 1;

                if (checkForAtom(greenAtom)) {
                    atomGreen = true;
                }

                aOx = CIx - 1;
                aOy = CIy + 1;
                aOz = CIz;


                if (checkForAtom(orangeAtom)) {
                    atomOrange = true;
                }

                aPx = CIx - 1;
                aPy = CIy;
                aPz = CIz + 1;

                if (checkForAtom(pinkAtom)) {
                    atomPink = true;
                }

                if (atomGreen && atomOrange) {
                    // reverse ray
                    newDir = "YR";
                }

                else if (atomGreen && atomPink) {
                    newDir = "XU";
                }

                else if (atomOrange && atomPink) {
                    newDir = "ZD";
                }

                else if (atomGreen) {
                    newDir = "ZU";
                }

                else if (atomOrange) {
                    newDir = "XD";
                }

                else if (atomPink) {
                    newDir = "Absorbed";
                }

            case "YR":

                aGx = CIx + 1;
                aGy = CIy - 1;
                aGz = CIz;

                if (checkForAtom(greenAtom)) {
                    atomGreen = true;
                }

                aOx = CIx;
                aOy = CIy + 1;
                aOz = CIz - 1;

                if (checkForAtom(orangeAtom)) {
                    atomOrange = true;
                }

                aPx = CIx + 1;
                aPy = CIy;
                aPz = CIz - 1;

                if (checkForAtom(pinkAtom)) {
                    atomPink = true;
                }

                if (atomGreen && atomOrange) {
                    // reverse ray
                    newDir = "YL";
                }

                else if (atomGreen && atomPink) {
                    newDir = "ZU";
                }

                else if (atomOrange && atomPink) {
                    newDir = "XD";
                }

                else if (atomGreen) {
                    newDir = "XU";
                }

                else if (atomOrange) {
                    newDir = "ZD";
                }

                else if (atomPink) {
                    newDir = "Absorbed";
                }

            case "XU":

                aGx = CIx + 1;
                aGy = CIy;
                aGz = CIz - 1;

                if (checkForAtom(greenAtom)) {
                    atomGreen = true;
                }

                aOx = CIx - 1;
                aOy = CIy + 1;
                aOz = CIz;

                if (checkForAtom(orangeAtom)) {
                    atomOrange = true;
                }

                aPx = CIx;
                aPy = CIy + 1;
                aPz = CIz - 1;

                if (checkForAtom(pinkAtom)) {
                    atomPink = true;
                }

                if (atomGreen && atomOrange) {
                    // reverse ray
                    newDir = "XD";
                }

                else if (atomGreen && atomPink) {
                    newDir = "YL";
                }

                else if (atomOrange && atomPink) {
                    newDir = "ZD";
                }

                else if (atomGreen) {
                    newDir = "ZU";
                }

                else if (atomOrange) {
                    newDir = "YR";
                }

                else if (atomPink) {
                    newDir = "Absorbed";
                }

            case "XD":

                aGx = CIx + 1;
                aGy = CIy - 1;
                aGz = CIz;

                if (checkForAtom(greenAtom)) {
                    atomGreen = true;
                }

                aOx = CIx - 1;
                aOy = CIy;
                aOz = CIz + 1;

                if (checkForAtom(orangeAtom)) {
                    atomOrange = true;
                }

                aPx = CIx;
                aPy = CIy - 1;
                aPz = CIz + 1;

                if (checkForAtom(pinkAtom)) {
                    atomPink = true;
                }

                if (atomGreen && atomOrange) {
                    // reverse ray
                    newDir = "XU";
                }

                else if (atomGreen && atomPink) {
                    newDir = "ZU";
                }

                else if (atomOrange && atomPink) {
                    newDir = "YR";
                }

                else if (atomGreen) {
                    newDir = "YL";
                }

                else if (atomOrange) {
                    newDir = "ZD";
                }

                else if (atomPink) {
                    newDir = "Absorbed";
                }

            case "ZU":

                aGx = CIx - 1;
                aGy = CIy;
                aGz = CIz + 1;

                if (checkForAtom(greenAtom)) {
                    atomGreen = true;
                }

                aOx = CIx;
                aOy = CIy + 1;
                aOz = CIz - 1;

                if (checkForAtom(orangeAtom)) {
                    atomOrange = true;
                }

                aPx = CIx - 1;
                aPy = CIy + 1;
                aPz = CIz;

                if (checkForAtom(pinkAtom)) {
                    atomPink = true;
                }

                if (atomGreen && atomOrange) {
                    // reverse ray
                    newDir = "ZD";
                }

                else if (atomGreen && atomPink) {
                    newDir = "YR";
                }

                else if (atomOrange && atomPink) {
                    newDir = "XU";
                }

                else if (atomGreen) {
                    newDir = "XD";
                }

                else if (atomOrange) {
                    newDir = "YL";
                }

                else if (atomPink) {
                    newDir = "Absorbed";
                }

            case "ZD":

                aGx = CIx;
                aGy = CIy - 1;
                aGz = CIz + 1;

                if (checkForAtom(greenAtom)) {
                    atomGreen = true;
                }

                aOx = CIx + 1;
                aOy = CIy;
                aOz = CIz - 1;

                if (checkForAtom(orangeAtom)) {
                    atomOrange = true;
                }

                aPx = CIx + 1;
                aPy = CIy - 1;
                aPz = CIz;

                if (checkForAtom(pinkAtom)) {
                    atomPink = true;
                }

                if (atomGreen && atomOrange) {
                    // reverse ray
                    newDir = "ZU";
                }

                else if (atomGreen && atomPink) {
                    newDir = "XD";
                }

                else if (atomOrange && atomPink) {
                    newDir = "YL";
                }

                else if (atomGreen) {
                    newDir = "YR";
                }

                else if (atomOrange) {
                    newDir = "XU";
                }

                else if (atomPink) {
                    newDir = "Absorbed";
                }
        }

        return newDir;
    }

    // Method to check if a position is at the edge of the board
    private boolean hasReachedBoardEdge(BlackBoxBoard.Point3D position) {
        int x = position.x;
        int y = position.y;
        int z = position.z;

        return (x == 4 || x == -4) || (y == 4 || y == -4) || (z == 4 || z == -4);
    }


    // Method to calculate the next position of the ray based on the current position and direction of ray
    private BlackBoxBoard.Point3D calculateNextPosition(BlackBoxBoard.Point3D currentPosition, String dir) {

        int x = currentPosition.x;
        int y = currentPosition.y;
        int z = currentPosition.z;


        switch (dir) {
            case "YL": // direction is on axis y going left
                x --;
                z ++;
                break;

            case "YR": // direction is on axis y going right
                x ++;
                z --;
                break;

            case "XU": // direction is on axis x going up
                y ++;
                z --;
                break;

            case "XD": // direction is on axis x going down
                y --;
                z ++;
                break;

            case "ZU": // direction is on axis z going up
                x --;
                y ++;
                break;

            case "ZD": // direction is on axis z going down
                x ++;
                y --;
                break;
        }

        // Return the calculated next position
        return new BlackBoxBoard.Point3D(x, y, z);
    }

    // Function to calculate and store  edge cells next to entry point of a ray to determine if ray is reversed
    private List<BlackBoxBoard.Point3D> getNextTo(BlackBoxBoard.Point3D point) {
        List<BlackBoxBoard.Point3D> nextTo = new ArrayList<>();

        // Handle corner conditions
        if (point.x == -4 && point.y == 4) { // Bottom left corner
            nextTo.add(new BlackBoxBoard.Point3D(point.x, point.y - 1, point.z + 1));
            nextTo.add(new BlackBoxBoard.Point3D(point.x + 1, point.y, point.z - 1));
        }
        if (point.x == 4 && point.y == -4) { // Top right corner
            nextTo.add(new BlackBoxBoard.Point3D(point.x - 1, point.y, point.z + 1));
            nextTo.add(new BlackBoxBoard.Point3D(point.x, point.y + 1, point.z - 1));
        }
        if (point.x == -4 && point.z == 4) { // Leftmost corner
            nextTo.add(new BlackBoxBoard.Point3D(point.x + 1, point.y - 1, point.z));
            nextTo.add(new BlackBoxBoard.Point3D(point.x, point.y + 1, point.z - 1));
        }
        if (point.x == 4 && point.z == -4) { // Rightmost corner
            nextTo.add(new BlackBoxBoard.Point3D(point.x, point.y - 1, point.z + 1));
            nextTo.add(new BlackBoxBoard.Point3D(point.x - 1, point.y + 1, point.z));
        }
        if (point.y == -4 && point.z == 4) { // Top left corner
            nextTo.add(new BlackBoxBoard.Point3D(point.x + 1, point.y, point.z - 1));
            nextTo.add(new BlackBoxBoard.Point3D(point.x - 1, point.y + 1, point.z));
        }
        if (point.y == 4 && point.z == -4) { // Bottom right corner
            nextTo.add(new BlackBoxBoard.Point3D(point.x + 1, point.y - 1, point.z));
            nextTo.add(new BlackBoxBoard.Point3D(point.x - 1, point.y, point.z + 1));
        }

        // Handle non-corner edge conditions separately
        // X-axis edge conditions
        if (point.x == -4 || point.x == 4) {
            nextTo.add(new BlackBoxBoard.Point3D(point.x, point.y + 1, point.z - 1));
            nextTo.add(new BlackBoxBoard.Point3D(point.x, point.y - 1, point.z + 1));
        }
        // Y-axis edge conditions
        if (point.y == -4 || point.y == 4) {
            nextTo.add(new BlackBoxBoard.Point3D(point.x + 1, point.y, point.z - 1));
            nextTo.add(new BlackBoxBoard.Point3D(point.x - 1, point.y, point.z + 1));
        }
        // Z-axis edge conditions
        if (point.z == -4 || point.z == 4) {
            nextTo.add(new BlackBoxBoard.Point3D(point.x + 1, point.y - 1, point.z));
            nextTo.add(new BlackBoxBoard.Point3D(point.x - 1, point.y + 1, point.z));
        }

        return nextTo;
    }
    //Function to determine if ray will be reversed
     private boolean isRayReflectedAtEdge(BlackBoxBoard.Point3D point) {
        List<BlackBoxBoard.Point3D> nextTo = getNextTo(point);

        // Check if any of the adjacent cells have an atom which could be the cause of the reversal/reflection
         for (BlackBoxBoard.Point3D cellsNextTo : nextTo) {
             HexCell cell = board.getCell(cellsNextTo);
             if (cell != null && cell.hasAtom()) {
                 return true; // Ray is reflected by an atom in the adjacent cell.
             }
         }
         return false; //No reflection occurs if no atoms are in the cells next to entry point
     }



        // string representation of rays path
      public String getPath() {
          StringBuilder sb = new StringBuilder();

          // Iterate over points in the path list
          for (int i = 0; i < this.path.size(); i++) {
              BlackBoxBoard.Point3D point = this.path.get(i);

              // Append the coordinates of the point
              sb.append("(")
                      .append(point.x).append(", ")
                      .append(point.y).append(", ")
                      .append(point.z)
                      .append(")");

              // Append separator "->" if not the last point
              if (i < this.path.size() - 1) {
                  sb.append(" -> ");
              }
          }
          return sb.toString();
      }


}
