package Controller;
import Model.BlackBoxBoard;
import Model.Player;
import javafx.geometry.Point2D;
import utils.ReadyButtonClickedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.SaveScore;


public class GameState implements ReadyButtonClickedListener {
    private final List<Point2D> setterAtoms = new ArrayList<>(); //list to store the setter atom positions
    private final List<Point2D> experimenterAtoms = new ArrayList<>(); //list to store experimenter atoms.

    private String player1Name;  // Name of the setter
    private String player2Name;  // Name of the experimenter

    private Map<String, Integer> scores = new HashMap<>();  // Store player scores mapped to  player names.
    private SaveScore scoreSave = new SaveScore("SavedScores.properties");  // Instance of SaveScore
    private int roundsPlayed = 0;  // To keep track of how many rounds have been played
    private static final int TOTAL_ROUNDS = 2;  // Total rounds to be played

    public GameState() {
        // Initialise with empty names
        player1Name = "";
        player2Name = "";
    }

    //player 1 is always the setter and player 2 always exprimenter
    //use this to add players names at start of game

    public void setPlayerNames(String setter, String experimenter) {
        this.player1Name = setter;
        this.player2Name = experimenter;
    }


    public void addScore(String playerName, int score) {
        // Adds or updates the score for a player in the map and immediately saves it to the properties file.
        scores.put(playerName, score);
        scoreSave.saveScore(playerName, score);  // Save immediately
    }

    public int getScore(String playerName) {
        // gets the score for a player from the map. If no score is found, returns 0 as default.
        return scores.getOrDefault(playerName, 0);
    }


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
    //method to end game to save score
    public void endTurn(String playerName, List<Point2D> setterAtoms, List<Point2D> experimenterAtoms, BlackBoxBoard board) {
        Map<String, Integer> results = calcScore(setterAtoms, experimenterAtoms, board);
        addScore(playerName, results.get("score"));
        roundsPlayed++;

        if (roundsPlayed >= TOTAL_ROUNDS) {
            compareScoresAndDeclareWinner();
        } else {
            resetGame();  // Prepare the game for the next round
        }
    }

    private void compareScoresAndDeclareWinner() {
        if (scores.size() < 2) return; // Ensure there are two scores to compare

        List<Map.Entry<String, Integer>> sortedScores = new ArrayList<>(scores.entrySet());
        sortedScores.sort(Map.Entry.comparingByValue()); // Sort scores in ascending order

        String winner = sortedScores.get(0).getKey(); // Winner has the lowest score
        System.out.println("Winner is: " + winner + " with score: " + sortedScores.get(0).getValue());

    }

   public void resetGame() {
        // Clears all game data to start a new game.
        setterAtoms.clear();
        experimenterAtoms.clear();
        //scores.clear();  // Optionally clear scores if needed
    }


    //method for beginning experimenter turn.
    public void resetReadyClicked () {//resetting flag from outside
        isReadyClicked = false;
    }


}






