module com.example.scenetest {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.blackbox to javafx.fxml;
    exports com.example.blackbox;
}