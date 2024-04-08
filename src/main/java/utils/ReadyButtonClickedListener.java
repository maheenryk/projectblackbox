package utils;

/**
 * This is an interface that handles the communication of the controller and view when
 * dealing with the click of the ready button and the consequent change of game state from Setter's turn to
 * Experienter's turn.
 */
public interface ReadyButtonClickedListener {
    void onReadyClicked();
}
