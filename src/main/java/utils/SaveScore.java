package utils;
/*
* use properties class and hash map to store the players and their scores
properties class allows for easy loading and saving  of a file load() and store()
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class SaveScore {

    //file where score will be saved
    private final String scoreFile;

    public SaveScore(String scoreFile) {
        this.scoreFile = scoreFile;
    }

    //save players score under their name will be the key used to get their score
    public void saveScore(String playerName, int score) {
        //use properties util class to save and load score uses a key-value structure using
        Properties p = new Properties();
        //try to open file if a score has already been saved
        try (FileInputStream in = new FileInputStream(scoreFile)) {
            p.load(in);//load scorefile
        } catch (IOException e) {
            System.out.println("Failed to load scores file: " + e.getMessage());
        }

        // Check current score and save only if the new score is better  (or if no score exists for this player)
        String currentScore = p.getProperty(playerName);
        if (currentScore == null || Integer.parseInt(currentScore) > score) {
            p.setProperty(playerName, String.valueOf(score));
        }

        try (FileOutputStream out = new FileOutputStream(scoreFile)) {
            p.store(out, "Player Scores"); // Save the properties back to the file
        } catch (IOException e) {
            System.out.println("Failed to save scores file: " + e.getMessage());
        }


    }
    //method to load the scoresfile into properties then transfer into hashmap to be able to use it easier
    public Map<String, Integer> loadScore(){
        //stores keyvalue pairs from the score file
        Properties p = new Properties();
        //open scorefile
        try (FileInputStream in = new FileInputStream(scoreFile)){
            p.load(in);//reads our key value pairs
        } catch (IOException e) {
            System.out.println("Failed to load scores file: " + e.getMessage());
            throw new RuntimeException(e);

        }

        Map<String, Integer> scores = new HashMap<>();// new hashmap is created to store scores in a more easily
        for (String key : p.stringPropertyNames()) {
            //storing scores gets score for each player from properties converts it to an into and stores in scores map
            scores.put(key, Integer.parseInt(p.getProperty(key)));
        }
        //return map containing all loaded scores
        return scores;
    }

}



