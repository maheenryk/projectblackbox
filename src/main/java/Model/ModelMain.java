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
        blackBoxBoard.placeRandomAtoms(6);
        System.out.println("After placing atoms:");
        blackBoxBoard.printBoard();

        blackBoxBoard.printCIPoints();

        // Define an entry point for the ray. Choose an edge coordinate.
        BlackBoxBoard.Point3D entryPoint = new BlackBoxBoard.Point3D(4, 0, -4); // Example edge point
        String dir = "YL";

        // Create a ray with the entry point
        Ray ray = new Ray(blackBoxBoard, entryPoint, dir);

        // Print the ray's path
        System.out.println("Ray's Path: " + ray.getPath());
        // Check if the ray is absorbed and print the result
        System.out.println("Ray absorbed: " + ray.isAbsorbed());


    }
}
