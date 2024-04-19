package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a player
 * Players can either be a Setter or Experimenter store in an enum

 */

public class Player {
    // declare our enum to store role states

    enum PlayerRole{
        SETTER,
        EXPERIMENTER
    }

    private PlayerRole role;
    public Player(PlayerRole role){
        this.role = role;
    }

    public PlayerRole getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "Player role : " + role;
    }


    /*private PlayerRole role;
      //tracks score for player
     private int score = 0;
      // the number of ray markers placed by experimenter
     private int rayMarkersUsed = 0;
     private List<BlackBoxBoard.Point3D> atomsPlaced = new ArrayList<>();
      // reference to Blackboxboard
     private BlackBoxBoard board;
      // a ray list to store experimenters rays
     private  List<Ray> rays = new ArrayList<>();
      //constructor
   public Player(PlayerRole role, BlackBoxBoard board){
       this.role = role;
       this.board = board;
    }




    /**
     *  Methods to be implemented :
     *  CalculateScore
     *  sendRay
     *  Initialise board (set atoms -> setter)
     *
     *
     *
     *
     * */


}
