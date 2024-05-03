package Controller;
import Model.BlackBoxBoard;

import javafx.geometry.Point2D;
import utils.ReadyButtonClickedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class GameState implements ReadyButtonClickedListener {
    private final List<Point2D> setterAtoms = new ArrayList<>(); //list to store the setter atom positions
    private final List<Point2D> experimenterAtoms = new ArrayList<>(); //list to store experimenter atoms.




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
            //reset flag here.


        }
    }

    public void setSetterAtomPositions(List<Point2D> positions) {
        this.setterAtoms.clear();
        this.setterAtoms.addAll(positions);
    }

    public List<Point2D> getSetterAtomPositions() {
        return this.setterAtoms;
    }

    public void setExpAtomPositions(List<Point2D> positions) {
        this.experimenterAtoms.clear();
        this.experimenterAtoms.addAll(positions);
    }

    public List<Point2D> getExperimenterAtoms() {
        return this.experimenterAtoms;
    }


    public static Map<String, Integer> calcScore(List<Point2D> setterAtoms, List<Point2D> experimenterAtoms, BlackBoxBoard board) {
        int correctAtoms = setterAtoms.size();

        // only keep in experimenter atoms list the atoms that match with setter's list
        experimenterAtoms.retainAll(setterAtoms);
        int incorrectAtoms = (correctAtoms - experimenterAtoms.size());
        int score = incorrectAtoms * 5;

        if (incorrectAtoms > 1) {
            System.out.println(incorrectAtoms + " atoms were incorrectly placed.");
        } else {
            System.out.println(incorrectAtoms + " atom was incorrectly placed.");
        }
        System.out.println("Total rays fired: " + board.getRayCount());
        System.out.println("Total ray markers: " + board.getRayMarkers());
        score += BlackBoxBoard.rayMarkers;

        Map<String, Integer> results = new HashMap<>(); //map for storing results
        results.put("score", score);
        //results.put("incorrectAtoms", incorrectAtoms);
        results.put("rayCount", board.getRayCount());
        results.put("rayMarkers", board.getRayMarkers());

        return results;

    }


    //method for beginning experimenter turn.
    public void resetReadyClicked () {//resetting flag from outside
        isReadyClicked = false;
    }


}






