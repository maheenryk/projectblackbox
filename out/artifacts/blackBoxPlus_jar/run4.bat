@echo off
cd /d "%~dp0"
java --module-path ".\javafx-sdk-21.0.2\lib" --add-modules javafx.controls,javafx.fxml -jar blackBoxPlus-4-atoms-exp.jar