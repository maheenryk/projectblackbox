package com.example.blackbox;

import javafx.application.Application;
import javafx.stage.Stage;

public class launchExp extends Application {

 @Override
    public void start(Stage primaryStage) {
            Main main = new Main();
            main.showExperimenterScreen(primaryStage);
        }

        public static void main(String[] args) {
            launch(args);
        }
}