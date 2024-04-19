package Controller;
import com.example.blackbox.Main;
import utils.ReadyButtonClickedListener;
import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.List;


public class GameState implements ReadyButtonClickedListener {
    private List<Point2D> setterAtoms = new ArrayList<>(); //list to store the setter atom positions

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




    //method for beginning experimenter turn.

    public void resetReadyClicked() {//resetting flag from outside
        isReadyClicked = false;
    }
}


