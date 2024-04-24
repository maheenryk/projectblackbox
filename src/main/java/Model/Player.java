package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * layers track of their role themselves
 * they just hand over the laptop to the other player when their round is done
 * ui displays you are setter you are experimenter
 */

public class Player {
    //private String role;
    //players name
    private String name;
    //score
    private int score;
    public Player(String name){
        this.name = name;
       // this.role = role;
        this.score =0;
    }


    // public String getRole() {
     //   return role;
    //}


    //get players name
    public String getName() {
        return this.name;
    }

    //get the players score
    public int getScore() {
        return this.score;
    }


    //method to get their score at end of game
    public void updateScore(int newScore) {
        this.score += newScore;
    }







}
