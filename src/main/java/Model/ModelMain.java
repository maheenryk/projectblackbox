package Model;

//import Controller.ViewModel;
//import javafx.geometry.Point2D;
//import Model.BlackBoxBoard.Point3D;
//import com.example.blackbox.Main;

public class ModelMain {
    public static void main(String[] args) {
//
//        BlackBoxBoard.Point3D cubicCoordinates = new Point3D(-3, 3, 0);
//
//        //Call cubicToCartesian method and get the result
//        Point2D result = ViewModel.cubicToCartesian(cubicCoordinates);
//        // Print the result
//        System.out.println("Result: " + result);


        BlackBoxBoard blackBoxBoard = new BlackBoxBoard();
        System.out.println("Before placing atoms:");
        // blackBoxBoard.printBoard();

        // Place atoms randomly
        // blackBoxBoard.placeRandomAtoms(6);
        System.out.println("After placing atoms:");

        blackBoxBoard.printCIPoints();


        // testing ray paths with specific atom placements

        /*
        BlackBoxBoard.Point3D atom1 = new BlackBoxBoard.Point3D(0,-4,4);
        blackBoxBoard.placeAtom(atom1);
        BlackBoxBoard.Point3D atom2 = new BlackBoxBoard.Point3D(-3,4,-1);
        blackBoxBoard.placeAtom(atom2);
        BlackBoxBoard.Point3D atom3 = new BlackBoxBoard.Point3D(0,1,-1);
        blackBoxBoard.placeAtom(atom3);
        BlackBoxBoard.Point3D atom4 = new BlackBoxBoard.Point3D(2,-1,-1);
        blackBoxBoard.placeAtom(atom4);
        BlackBoxBoard.Point3D atom5 = new BlackBoxBoard.Point3D(-1,0,1);
        blackBoxBoard.placeAtom(atom5);
        BlackBoxBoard.Point3D atom6 = new BlackBoxBoard.Point3D(3,-1,-2);
        blackBoxBoard.placeAtom(atom6);
         */

        blackBoxBoard.printBoard();

        RayNode.initializeNodes();

        // Define an entry point for the ray. Choose an edge coordinate.
        BlackBoxBoard.Point3D entryPoint1 = new BlackBoxBoard.Point3D(0, 4, -4); // Example edge point
        Direction dir = Direction.XU;

        // Create a ray with the entry point
        Ray ray1 = new Ray(blackBoxBoard, entryPoint1, dir);
        printRayInfo(ray1);

        BlackBoxBoard.Point3D entryPoint2 = new BlackBoxBoard.Point3D(4, 0, -4); // Example edge point
        Direction dir2 = Direction.YL;

        // Create a ray with the entry point
        Ray ray2 = new Ray(blackBoxBoard, entryPoint2, dir2);
        printRayInfo(ray2);

        System.out.println("Ray markers: " + blackBoxBoard.getRayMarkers());
        System.out.println("Total rays fired: " + blackBoxBoard.getRayCount());
        //System.out.println("The score is: " + GameState.calcScore());
        //System.out.println("Edge cells: " + BlackBoxBoard.edgeCells);

    }

    public static void printRayInfo(Ray ray) {
        // Print the ray's path
        System.out.println("Ray entered at: " + ray.getEntryPoint());
        // Check if the ray is absorbed and print the result
        if (ray.isAbsorbed()) {
            System.out.println("Ray absorbed.");
        }
        else if (ray.isRayReversed()) {
            System.out.println("Ray reversed.");
        }
        else {
            System.out.println("Ray exited at: " + ray.getExitPoint());
            System.out.println("Ray's exit direction is: " + ray.getDirection());
        }

        System.out.println("Ray's Path: " + ray.getPath() + "\n");
    }

}
