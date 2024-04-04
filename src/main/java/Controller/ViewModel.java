package Controller;
import javafx.geometry.Point2D;
import Model.BlackBoxBoard.Point3D; //importing custom Point3D class

public class ViewModel {

    //coordinate map conversion methods.

    //converts from game logic Point3D cubic coordinates to UI Point2D Cartesian coordinates.
    public static Point2D cubicToCartesian(Point3D cubicCoordinates, double size) {
        double x = (Math.sqrt(3) * cubicCoordinates.x + Math.sqrt(3) / 2 * cubicCoordinates.z) * size;
        double y = (3 / 2.0) * cubicCoordinates.z * size;
        return new Point2D(x, y);
    }



}
