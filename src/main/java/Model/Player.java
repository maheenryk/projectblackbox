package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * players keep track of their role themselves
 * they just hand over the laptop to the other player when their round is done
 * ui displays you are setter you are experimenter
 *
 */

public class Player {

    //players name
    private String name;
    //score
  //  private int score;
    public Player(String name){
        this.name = name;

    }


    //get players name
    public String getName() {
        return this.name;
    }







}
