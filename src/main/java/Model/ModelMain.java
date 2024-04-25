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
        //blackBoxBoard.placeRandomAtoms(6);
        System.out.println("After placing atoms:");

        blackBoxBoard.printCIPoints();


        // testing ray paths with specific atom placements

        BlackBoxBoard.Point3D atom = new BlackBoxBoard.Point3D(0,0,0);
        blackBoxBoard.placeAtom(atom);
        /*

        atom = new BlackBoxBoard.Point3D(-3,4,-1);
        blackBoxBoard.placeAtom(atom);
        atom = new BlackBoxBoard.Point3D(0,1,-1);
        blackBoxBoard.placeAtom(atom);
        atom = new BlackBoxBoard.Point3D(2,-1,-1);
        blackBoxBoard.placeAtom(atom);
        atom = new BlackBoxBoard.Point3D(-1,0,1);
        blackBoxBoard.placeAtom(atom);
        atom = new BlackBoxBoard.Point3D(3,-1,-2);
        blackBoxBoard.placeAtom(atom);
         */

        blackBoxBoard.printBoard();

        RayNode.initializeNodes();
        //RayNode.printRayNodes();

        // Define an entry point for the ray. Choose an edge coordinate.
        // Create a ray with the entry point
        Ray ray = new Ray(blackBoxBoard, 35);
        Ray.printRayInfo(ray);

        // Create a ray with the entry point
        ray = new Ray(blackBoxBoard, 46);
        Ray.printRayInfo(ray);

        System.out.println("Ray markers: " + blackBoxBoard.getRayMarkers());
        System.out.println("Total rays fired: " + blackBoxBoard.getRayCount());
        //System.out.println("The score is: " + GameState.calcScore());
        //System.out.println("Edge cells: " + BlackBoxBoard.edgeCells);

        //System.out.println("Node Coordinates: " + RayNode.getNodeCoordinates(18));
        //System.out.println("Node Direction: " + RayNode.getNodeDirection(18));
        //System.out.println("Node: " + RayNode.getNodeNumber((new BlackBoxBoard.Point3D(-4, 4, 0)), Direction.YL));

    }

}
