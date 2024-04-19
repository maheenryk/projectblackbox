package Model;

//import Controller.ViewModel;
//import javafx.geometry.Point2D;
//import Model.BlackBoxBoard.Point3D;


public class ModelMain {
    public static void main(String[] args) {
//
//        BlackBoxBoard.Point3D cubicCoordinates = new Point3D(-3, 3, 0);
//
//        // Call cubicToCartesian method and get the result
//        Point2D result = ViewModel.cubicToCartesian(cubicCoordinates);
//        // Print the result
//        System.out.println("Result: " + result);


        BlackBoxBoard blackBoxBoard = new BlackBoxBoard();
        System.out.println("Before placing atoms:");
        // blackBoxBoard.printBoard();

        //  Place atoms randomly
        // blackBoxBoard.placeRandomAtoms(6);
        System.out.println("After placing atoms:");
        // blackBoxBoard.printBoard();

        // blackBoxBoard.printCIPoints();


        BlackBoxBoard.Point3D atom1 = new BlackBoxBoard.Point3D(3,-1,-2);
        blackBoxBoard.placeAtom(atom1);
        BlackBoxBoard.Point3D atom2 = new BlackBoxBoard.Point3D(2,1,-3);
        blackBoxBoard.placeAtom(atom2);
        BlackBoxBoard.Point3D atom3 = new BlackBoxBoard.Point3D(0,0,0);
        // blackBoxBoard.placeAtom(atom3);
        BlackBoxBoard.Point3D atom4 = new BlackBoxBoard.Point3D(0,0,0);
        // blackBoxBoard.placeAtom(atom4);
        BlackBoxBoard.Point3D atom5 = new BlackBoxBoard.Point3D(0,0,0);
        // blackBoxBoard.placeAtom(atom5);
        BlackBoxBoard.Point3D atom6 = new BlackBoxBoard.Point3D(0,0,0);
        // blackBoxBoard.placeAtom(atom6);

        // Define an entry point for the ray. Choose an edge coordinate.
        BlackBoxBoard.Point3D entryPoint = new BlackBoxBoard.Point3D(4, 0, -4); // Example edge point
        Direction dir = Direction.YL;

        // Create a ray with the entry point
        Ray ray = new Ray(blackBoxBoard, entryPoint, dir);

        // Print the ray's path
        System.out.println("Ray's Path: " + ray.getPath());
        // Check if the ray is absorbed and print the result
        if (ray.isAbsorbed()) {
            System.out.println("Ray absorbed.");
        }
        if (!ray.isAbsorbed()) {
            System.out.println("Ray exited at: " + ray.getExitPoint());
        }
    }
}
