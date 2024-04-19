package Controller;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SwitchScene {
    private Stage stage;
    private Scene mainGameScene;

    public SwitchScene(Stage stage, Scene mainGameScene) {
        this.stage = stage;
        this.mainGameScene = mainGameScene;
    }

    public void handleStartButtonClick() {
        stage.setScene(mainGameScene);
    }
}
