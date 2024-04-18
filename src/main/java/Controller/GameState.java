package Controller;
import com.example.blackbox.Main;
import utils.ReadyButtonClickedListener;

public class GameState implements ReadyButtonClickedListener {

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



    //method for beginning experimenter turn.

    public void resetReadyClicked() {//resetting flag from outside
        isReadyClicked = false;
    }
}


