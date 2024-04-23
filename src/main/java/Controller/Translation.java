package Controller;
import Model.BlackBoxBoard;
import Model.BlackBoxBoard.Point3D;
import javafx.geometry.Point2D;
import com.example.blackbox.HexCellGenerator;

import java.util.*;

import Model.HexCell;


/**
 * This is a controller class that translates the coordinates from custom Point3D in game logic to 2D coordinates in
 * the game's UI. Essentially, this handles board connection of logic to UI. This is done by (1) connecting the respective Linked
 * Hash Maps board (logic) and hexCellsMap (UI) in a third linked HashMap called translationMap, (2)
 */

public class Translation {

    // This section of the code deals with hex cell and atom connection.

    private BlackBoxBoard blackBoxBoard;
    private Map<Point3D, Point2D> translationMap;

    public Translation(BlackBoxBoard blackBoxBoard) {
        this.blackBoxBoard = blackBoxBoard;
    }

    //Method to link the two maps
    public void linkMaps() {
        //retrieving static hex cell map
        Map<Integer, Point2D> hexCellsMap = HexCellGenerator.getHexCellsMap();

        //check for initialisation and equal size of both maps
        if (blackBoxBoard.getBoardSize() != hexCellsMap.size()) {
            System.out.println("The maps cannot be linked due to size mismatch."); //if size differs
            return;
        }

         //creating new map with key-value pair consisting of key from board and value from hexCellsMap.
        translationMap = new LinkedHashMap<>();
        //here iterators are obtained and used to match the key value individually.
        Iterator<Map.Entry<Point3D, HexCell>> boardIterator = blackBoxBoard.getBoardIterator();
        Iterator<Map.Entry<Integer, Point2D>> hexCellIterator = hexCellsMap.entrySet().iterator();

        //here, iterator simultaneously iterates over the maps.
        while (boardIterator.hasNext() && hexCellIterator.hasNext()) {
            Map.Entry<Point3D, HexCell> boardEntry = boardIterator.next();//getting next entries in while loop.
            Map.Entry<Integer, Point2D> hexCellEntry = hexCellIterator.next();

            //linking Point3D [key of board map], to the value of the second hexCellsMap map, which is the Point2D object containing x, y coords.
            //the key of the new map is then Point3D and the value is Point2D, successfully connecting the logic and UI.
            translationMap.put(boardEntry.getKey(), hexCellEntry.getValue());
        }

        //printing the linked map for testing purposes
        translationMap.forEach((key, value) -> System.out.println(key + " ----------> " + value));
    }

    public Map<Point3D, Point2D> getTranslationMap() {
        return translationMap;
    }

    public List<Point3D> get3DAtomMatch(List<Point2D> atomPositions) { /*this method returns the 3D key value in the translationMap that
    corresponds to each entry of the 2-D coords in the positions Array List.*/
        List<Point3D> point3DSetterAtoms = new ArrayList<>();
        //Here we're iterating over the entrySet of translationMap to check each Point2D value against atomPositions
        for (Map.Entry<Point3D, Point2D> entry : translationMap.entrySet()) {
            if (atomPositions.contains(entry.getValue())) {
                //upon matching, the relevant 3D is added to the matched list.
                point3DSetterAtoms.add(entry.getKey());
                System.out.println("Match found: " + entry.getKey() + "====>" + entry.getValue() + "/n/n" + entry.getKey());
            }
        }
        return point3DSetterAtoms;
    }


    //this section of the code deals with ray translation. ie connecting the UI ray nodes to the ray class in the Model.






}






