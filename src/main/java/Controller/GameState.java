package Controller;
import Model.BlackBoxBoard;
import javafx.geometry.Point2D;
import utils.ReadyButtonClickedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the state and the flow of the game, handling interactions such as
 * game initialisation, atom placement, and scoring.

 */



public class GameState implements ReadyButtonClickedListener {
    private final List<Point2D> setterAtoms = new ArrayList<>(); //list to store the setter atom positions
    private final List<Point2D> experimenterAtoms = new ArrayList<>(); //list to store experimenter atoms.


    /**
     * Initialises a new game, setting up the needed game elements.
     */
    public void startNewGame() {
        System.out.println("New Game!");
    }
    private boolean isReadyClicked = false; //set ready button click flag to false.

    /**
     * Handles the ready button click. Sets the flag to true on the first click.
     */


    @Override
    public void onReadyClicked() {
        if (!isReadyClicked) {
            isReadyClicked = true;
            System.out.println("READY button clicked");
        }
    }

    /**
     * Sets the positions of atoms placed by the setter.
     *
     * @param positions A list of Point2D of the positions  of the setter's atoms.
     */
    public void setSetterAtomPositions(List<Point2D> positions) {
        this.setterAtoms.clear();
        this.setterAtoms.addAll(positions);
    }

    /**
     * Gets the positions of atoms placed by the setter.
     *
     * @return A list of Point2D of  the positions of the setter's atoms.
     */
    public List<Point2D> getSetterAtomPositions() {
        return this.setterAtoms;
    }

    /**
     * Sets the positions of atoms placed by the experimenter.
     *
     * @param positions A list of Point2D of  the positions of the experimenter's atoms.
     */
    public void setExpAtomPositions(List<Point2D> positions) {
        this.experimenterAtoms.clear();
        this.experimenterAtoms.addAll(positions);
    }

    /**
     * Gets the positions of atoms placed by the experimenter.
     *
     * @return A list of Point2D of the positions of the experimenter's atoms.
     */

    public List<Point2D> getExperimenterAtoms() {
        return this.experimenterAtoms;
    }




    /**
     * Calculates the score based on the placement of atoms by the setter and experimenter.
     * Scores are calculated based on correct placements and penalties for incorrect placements.
     *
     * @param setterAtoms The list of Point2D from the setter.
     * @param experimenterAtoms The list of Point2D from the experimenter that needs to match the setter's list.
     * @param board The black box board is  used to track other game logic like rays.
     * @return A map containing scoring details including correct atom placements and total score.
     */
    public static Map<String, Integer>  calcScore(List<Point2D> setterAtoms, List<Point2D> experimenterAtoms, BlackBoxBoard board) {
        int correctAtoms = setterAtoms.size();

        // only keep in experimenter atoms list the atoms that match with setter's list
          experimenterAtoms.retainAll(setterAtoms);
        int incorrectAtoms = (correctAtoms - experimenterAtoms.size());
        int score = incorrectAtoms * 5;

        int correctlyPlacedAtoms = experimenterAtoms.size();

        if (correctlyPlacedAtoms > 1) {
            System.out.println(correctlyPlacedAtoms + " atoms were correctly placed.");
        } else {
            System.out.println(correctlyPlacedAtoms + " atom was correctly placed.");
        }


        System.out.println("Total rays fired: " + board.getRayCount());
        System.out.println("Total ray markers: " + board.getRayMarkers());
        score += BlackBoxBoard.rayMarkers;

        Map<String, Integer> results = new HashMap<>();
        results.put("score", score);
        results.put("correctAtoms", correctlyPlacedAtoms);
        results.put("rayCount", board.getRayCount());
        results.put("rayMarkers", board.getRayMarkers());

        return results;

    }

    /**
     * Resets the ready button click flag.
     */
    public void resetReadyClicked() {//resetting flag from outside
        isReadyClicked = false;
    }
}






