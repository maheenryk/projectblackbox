package Model;

import java.util.ArrayList;
import java.util.List;
//import java.util.Objects;
import static Model.Direction.XU;
import static Model.Direction.XD;
import static Model.Direction.YR;
import static Model.Direction.YL;
import static Model.Direction.ZU;
import static Model.Direction.ZD;

public class Ray {
    //reference to the game board for path and check for atoms
    private final BlackBoxBoard board;
    //entry point of ray on the game board
    private final BlackBoxBoard.Point3D entryPoint;
    //we will store the path ray takes using a list
    private final BlackBoxBoard.Point3D exitPoint;
    private final List<BlackBoxBoard.Point3D> path;

    //to check whether ray is absorbed by an atom
    private boolean isAbsorbed;

    private boolean deflected60;

    private boolean deflected120;

    private boolean rayReversed;

    Direction newDir = Direction.Error;
    Direction entryDir;
    Direction exitDir = Direction.Error;
    Direction Abs = Direction.Absorbed;

    public Ray(BlackBoxBoard board, BlackBoxBoard.Point3D entryPoint, Direction dir){
        this.board = board; //board ref
        this.entryPoint = entryPoint; //starting point
        this.path = new ArrayList<>();
        this.isAbsorbed = false; //at the start ray is not absorbed
        this.deflected60 = false;
        this.deflected120 = false;
        this.rayReversed = false;
        //call method to make sure every time a ray object is created its path is calculated immediately

        this.exitPoint = calculatePath(dir);

    }
    public boolean isAbsorbed() {
        return this.isAbsorbed;
    }

    public boolean isDeflected60() {
        return this.deflected60;
    }

    public boolean isDeflected120() { return this.deflected120; }

    public boolean isRayReversed() { return this.rayReversed; }

    public BlackBoxBoard.Point3D getExitPoint() {
        return exitPoint;
    }

    public BlackBoxBoard.Point3D getEntryPoint() {return entryPoint;}

    public Direction getDirection() {
        return exitDir;
    }

    public Direction getEntryDir() { return  entryDir; }

    //check for atom
    private boolean checkForAtom(BlackBoxBoard.Point3D point){
        //use getHexCell method to check if a ray's current position has encountered an atom
        HexCell cell = board.getCell(point);// get HexCell at given point.
        return cell != null && cell.hasAtom();// check if the cell has an atom

    }

    //calculate path  of ray from entry point (validate whether its edge cell when making a ray object)
    private BlackBoxBoard.Point3D calculatePath(Direction dir){
        entryDir = dir;

        BlackBoxBoard.Point3D exitPoint = null;

        // calculation if ray is immediately reflected
        if(HexCell.isEdgeCell(entryPoint)){
            HexCell cell = board.getCell(entryPoint);
            if(cell != null && cell.hasCIPoint()){
                //if it has a CI we will find the cells on the edge its next to and find out if these cells have atoms o n edge
                if(isRayReflectedAtEdge(entryPoint)){
                    rayReversed = true;
                    path.add(entryPoint);
                    BlackBoxBoard.rayCount += 1;
                    BlackBoxBoard.rayMarkers += 1;
                    return entryPoint; //end method early since the ray is reflected
                }

            }

            else if (cell != null && cell.hasAtom()) {
                isAbsorbed = true;
                path.add(entryPoint);
                BlackBoxBoard.rayCount += 1;
                BlackBoxBoard.rayMarkers += 1;
                return entryPoint;
            }

        }


        //if ray is not immediately reflected
        //start path at entry point
        this.path.add(this.entryPoint);

        // initialize list to store cells visited by the ray
        //List<BlackBoxBoard.Point3D> visitedCells = new ArrayList<>();
        //visitedCells.add(this.entryPoint);

        // current position of the ray
        BlackBoxBoard.Point3D currentPosition = this.entryPoint;

        // iterate until the ray is absorbed or reaches edge of board
        // edge of board argument included in the end so the loop
        // doesn't break due to the entry point being on edge of board
        while (!isAbsorbed) {
            // Calculate the next position based on the current position and direction
            BlackBoxBoard.Point3D nextPosition;

            HexCell cell = board.getCell(currentPosition);

            if (cell !=null && cell.hasCIPoint()) {

                Direction result = newPath(currentPosition, dir);

                if (result == Direction.Absorbed) {
                    isAbsorbed = true;
                    nextPosition = calculateNextPosition(currentPosition, dir);
                    this.path.add(nextPosition);
                    BlackBoxBoard.rayMarkers += 1;
                    break;
                }

                else if (result == Direction.Error) {
                    System.out.println("Error");
                    break;
                }

                else {
                    if (rayReversed) {
                        BlackBoxBoard.rayMarkers += 1;
                    }
                    dir = result;
                }
            }

            nextPosition = calculateNextPosition(currentPosition, dir);

            // Add the next position to the path
            this.path.add(nextPosition);

            // Update current position
            currentPosition = nextPosition;

            // Add the current position to the visited cells list
            //visitedCells.add(currentPosition);

            // check if ray is on edge of board and break loop if true
            if (hasReachedBoardEdge(currentPosition)) {
                exitPoint = currentPosition;
                BlackBoxBoard.rayMarkers += 2;
                break;
            }
        }

        exitDir = dir;
        BlackBoxBoard.rayCount += 1;
        return exitPoint;
    }

    public boolean hasCIPoint(BlackBoxBoard.Point3D point) {
        HexCell cell = board.getCell(point);
        return cell != null && cell.hasCIPoint();
    }
    // Checking for the presence of atoms at specific positions compared to the rays current position.
    // Depending on where atoms are present around the ray, the rays direction can  be deflected,absorbed or reversed
    private Direction newPath (BlackBoxBoard.Point3D position, Direction dir) {

        rayReversed = false;
        deflected60 = false;
        deflected120 = false;

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

        BlackBoxBoard.Point3D greenAtom;
        BlackBoxBoard.Point3D orangeAtom;
        BlackBoxBoard.Point3D pinkAtom;

        boolean atomGreen = false;
        boolean atomPink = false;
        boolean atomOrange = false;

        switch (dir) {

            case YL:

                aGy = CIy - 1;
                aGz = CIz + 1;

                greenAtom = new BlackBoxBoard.Point3D(aGx, aGy, aGz);
                if (checkForAtom(greenAtom)) {
                    atomGreen = true;
                }

                aOx = CIx - 1;
                aOy = CIy + 1;

                orangeAtom = new BlackBoxBoard.Point3D(aOx, aOy, aOz);
                if (checkForAtom(orangeAtom)) {
                    atomOrange = true;
                }

                aPx = CIx - 1;
                aPz = CIz + 1;

                pinkAtom = new BlackBoxBoard.Point3D(aPx, aPy, aPz);
                if (checkForAtom(pinkAtom)) {
                    atomPink = true;
                }

                if (atomGreen && atomOrange) {
                    // reverse ray
                    newDir = YR;
                    rayReversed = true;
                }

                else if (atomGreen && atomPink) {
                    newDir = XD;
                    deflected120 = true;
                }

                else if (atomOrange && atomPink) {
                    newDir = ZU;
                    deflected120 = true;
                }

                else if (atomGreen) {
                    newDir = ZD;
                    deflected60 = true;
                }

                else if (atomOrange) {
                    newDir = XU;
                    deflected60 = true;
                }

                else if (atomPink) {
                    newDir = Abs;
                }

                break;

            case YR:

                aGx = CIx + 1;
                aGy = CIy - 1;

                greenAtom = new BlackBoxBoard.Point3D(aGx, aGy, aGz);
                if (checkForAtom(greenAtom)) {
                    atomGreen = true;
                }

                aOy = CIy + 1;
                aOz = CIz - 1;

                orangeAtom = new BlackBoxBoard.Point3D(aOx, aOy, aOz);
                if (checkForAtom(orangeAtom)) {
                    atomOrange = true;
                }

                aPx = CIx + 1;
                aPz = CIz - 1;

                pinkAtom = new BlackBoxBoard.Point3D(aPx, aPy, aPz);
                if (checkForAtom(pinkAtom)) {
                    atomPink = true;
                }

                if (atomGreen && atomOrange) {
                    // reverse ray
                    newDir = YL;
                    rayReversed = true;
                }

                else if (atomGreen && atomPink) {
                    newDir = ZD;
                    deflected120 = true;
                }

                else if (atomOrange && atomPink) {
                    newDir = XU;
                    deflected120 = true;
                }

                else if (atomGreen) {
                    newDir = XD;
                    deflected60 = true;
                }

                else if (atomOrange) {
                    newDir = ZU;
                    deflected60 = true;
                }

                else if (atomPink) {
                    newDir = Abs;
                }

                break;

            case XU:

                aGx = CIx + 1;
                aGy = CIy - 1;

                greenAtom = new BlackBoxBoard.Point3D(aGx, aGy, aGz);
                if (checkForAtom(greenAtom)) {
                    atomGreen = true;
                }

                aOx = CIx - 1;
                aOz = CIz + 1;

                orangeAtom = new BlackBoxBoard.Point3D(aOx, aOy, aOz);
                if (checkForAtom(orangeAtom)) {
                    atomOrange = true;
                }

                aPy = CIy - 1;
                aPz = CIz + 1;

                pinkAtom = new BlackBoxBoard.Point3D(aPx, aPy, aPz);
                if (checkForAtom(pinkAtom)) {
                    atomPink = true;
                }

                if (atomGreen && atomOrange) {
                    // reverse ray
                    newDir = XD;
                    rayReversed = true;
                }

                else if (atomGreen && atomPink) {
                    newDir = ZD;
                    deflected120 = true;
                }

                else if (atomOrange && atomPink) {
                    newDir = YR;
                    deflected120 = true;
                }

                else if (atomGreen) {
                    newDir = YL;
                    deflected60 = true;
                }

                else if (atomOrange) {
                    newDir = ZU;
                    deflected60 = true;
                }

                else if (atomPink) {
                    newDir = Abs;
                }

                break;

            case XD:

                aGx = CIx + 1;
                aGz = CIz - 1;

                greenAtom = new BlackBoxBoard.Point3D(aGx, aGy, aGz);
                if (checkForAtom(greenAtom)) {
                    atomGreen = true;
                }

                aOx = CIx - 1;
                aOy = CIy + 1;

                orangeAtom = new BlackBoxBoard.Point3D(aOx, aOy, aOz);
                if (checkForAtom(orangeAtom)) {
                    atomOrange = true;
                }

                aPy = CIy + 1;
                aPz = CIz - 1;

                pinkAtom = new BlackBoxBoard.Point3D(aPx, aPy, aPz);
                if (checkForAtom(pinkAtom)) {
                    atomPink = true;
                }

                if (atomGreen && atomOrange) {
                    // reverse ray
                    newDir = XU;
                    rayReversed = true;
                }

                else if (atomGreen && atomPink) {
                    newDir = YL;
                    deflected120 = true;
                }

                else if (atomOrange && atomPink) {
                    newDir = ZU;
                    deflected120 = true;
                }

                else if (atomGreen) {
                    newDir = ZD;
                    deflected60 = true;
                }

                else if (atomOrange) {
                    newDir = YR;
                    deflected60 = true;
                }

                else if (atomPink) {
                    newDir = Abs;
                }

                break;

            case ZU:

                aGy = CIy - 1;
                aGz = CIz + 1;

                greenAtom = new BlackBoxBoard.Point3D(aGx, aGy, aGz);
                if (checkForAtom(greenAtom)) {
                    atomGreen = true;
                }

                aOx = CIx + 1;
                aOz = CIz - 1;

                orangeAtom = new BlackBoxBoard.Point3D(aOx, aOy, aOz);
                if (checkForAtom(orangeAtom)) {
                    atomOrange = true;
                }

                aPx = CIx + 1;
                aPy = CIy - 1;

                pinkAtom = new BlackBoxBoard.Point3D(aPx, aPy, aPz);
                if (checkForAtom(pinkAtom)) {
                    atomPink = true;
                }

                if (atomGreen && atomOrange) {
                    // reverse ray
                    newDir = ZD;
                    rayReversed = true;
                }

                else if (atomGreen && atomPink) {
                    newDir = XD;
                    deflected120 = true;
                }

                else if (atomOrange && atomPink) {
                    newDir = YL;
                    deflected120 = true;
                }

                else if (atomGreen) {
                    newDir = YR;
                    deflected60 = true;
                }

                else if (atomOrange) {
                    newDir = XU;
                    deflected60 = true;
                }

                else if (atomPink) {
                    newDir = Abs;
                }

                break;

            case ZD:

                aGx = CIx - 1;
                aGz = CIz + 1;

                greenAtom = new BlackBoxBoard.Point3D(aGx, aGy, aGz);
                if (checkForAtom(greenAtom)) {
                    atomGreen = true;
                }

                aOy = CIy + 1;
                aOz = CIz - 1;

                orangeAtom = new BlackBoxBoard.Point3D(aOx, aOy, aOz);
                if (checkForAtom(orangeAtom)) {
                    atomOrange = true;
                }

                aPx = CIx - 1;
                aPy = CIy + 1;

                pinkAtom = new BlackBoxBoard.Point3D(aPx, aPy, aPz);
                if (checkForAtom(pinkAtom)) {
                    atomPink = true;
                }

                if (atomGreen && atomOrange) {
                    // reverse ray
                    newDir = ZU;
                    rayReversed = true;
                }

                else if (atomGreen && atomPink) {
                    newDir = YR;
                    deflected120 = true;
                }

                else if (atomOrange && atomPink) {
                    newDir = XU;
                    deflected120 = true;
                }

                else if (atomGreen) {
                    newDir = XD;
                    deflected60 = true;
                }

                else if (atomOrange) {
                    newDir = YL;
                    deflected60 = true;
                }

                else if (atomPink) {
                    newDir = Abs;
                }

                break;
        }

        if (rayReversed) {
            System.out.println("Ray reversed.");
        }

        else if (deflected120) {
            System.out.println("Ray deflected 120 degrees.");
        }

        else if (deflected60) {
            System.out.println("Ray deflected 60 degrees.");
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
    private BlackBoxBoard.Point3D calculateNextPosition(BlackBoxBoard.Point3D currentPosition, Direction dir) {

        int x = currentPosition.x;
        int y = currentPosition.y;
        int z = currentPosition.z;


        switch (dir) {
            case YL: // direction is on axis y going left
                x --;
                z ++;
                break;

            case YR: // direction is on axis y going right
                x ++;
                z --;
                break;

            case XU: // direction is on axis x going up
                y --;
                z ++;
                break;

            case XD: // direction is on axis x going down
                y ++;
                z --;
                break;

            case ZU: // direction is on axis z going up
                x ++;
                y --;
                break;

            case ZD: // direction is on axis z going down
                x --;
                y ++;
                break;
        }

        // Return the calculated next position
        return new BlackBoxBoard.Point3D(x, y, z);
    }

    public int determineExitRayCircle(Direction exitDir, BlackBoxBoard.Point3D exitPoint) {

        int exRC = 0;
        return exRC;
    }

    public void determineRayEntry(int rayCircle) {

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