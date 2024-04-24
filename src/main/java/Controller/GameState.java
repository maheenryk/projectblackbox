package Controller;
import Model.BlackBoxBoard;
import com.example.blackbox.Main;
import javafx.geometry.Point3D;
import utils.ReadyButtonClickedListener;
import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.*;


public class GameState implements ReadyButtonClickedListener {
    private final List<Point2D> setterAtoms = new ArrayList<>(); //list to store the setter atom positions
    private final List<Point2D> experimenterAtoms = new ArrayList<>();



    public void startNewGame() {
         //other new game code here (future sprint)
        System.out.println("New Game!");
    }
    private boolean isReadyClicked = false; //set ready button click flag to false.

    @Override
    public void onReadyClicked() {
        if (!isReadyClicked) { //action on first click
            isReadyClicked = true;
            System.out.println("READY button clicked");
            //call method for beginning experimenter turn here
            //maybe reset flag here.
        }
    }

    public void setSetterAtomPositions(List<Point2D> positions) {
        this.setterAtoms.clear();
        this.setterAtoms.addAll(positions);
    }
    public List<Point2D> getSetterAtomPositions() {
        return this.setterAtoms;
    }


    public static int calcScore( ) {
        List<Point2D> setterAtoms = Main.atomPositions;
        List<Point2D> experimenterAtoms = Main.expPositions;
        int correctAtoms = setterAtoms.size();
        experimenterAtoms.retainAll(setterAtoms);
        System.out.println("Score: ");
        int incorrectAtoms = (correctAtoms - experimenterAtoms.size());
        int score = incorrectAtoms*5;
        if (incorrectAtoms > 1) {
            System.out.println(incorrectAtoms + " atom was incorrectly placed.");
        }
        else {
            System.out.println(incorrectAtoms + " atoms were incorrectly placed.");
        }
        System.out.println("Total rays fired: " + BlackBoxBoard.rayCount);
        System.out.println("Total ray markers: " + BlackBoxBoard.rayMarkers);
        score += BlackBoxBoard.rayMarkers;
        return score;
    }

    //method for beginning experimenter turn.
    public void resetReadyClicked() {//resetting flag from outside
        isReadyClicked = false;
    }
}


