package com.javafx;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class SettingsController implements Initializable {
    private String cameFromMenu;
    private int world_id;
    private List<TextField> keyBindingFields;
    private String[] listOfKeys = {
            "Key-FlyForward",
            "Key-TurnLeft",
            "Key-TurnRight",
            "Key-Decelerate",
            "Key-FlyDown",
            "Key-RotateLeft",
            "Key-RotateRight",
            "Key-RotateUp",
            "Key-RotateDown",
            "Key-SettingsMenu"
    };

    @FXML
    private VBox keyBindingsVBox;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Pane homeIcon;

    @FXML
    private TextField seedTextField;

    @FXML
    private TextField worldNameTextField;

    @FXML
    private Button saveWorldName;

    @FXML
    private Button saveAndExitButton;

    @FXML
    private Button saveGameButton;

    public SettingsController(String cameFromMenu, int world_id) {
        this.cameFromMenu = cameFromMenu;
        this.world_id = world_id;
    }

    @FXML
    private void resetDefaultKeyBindings() {
        SettingsHandler.setDefaultKeyBindings();
        getKeyBindingsFromSettings();
    }

    @FXML
    private void saveAndGoToStartMenu() {

    }

    @FXML
    private void saveGame() {

    }

    @FXML
    private void onPressedAnchorPane() {
        anchorPane.requestFocus();
    }

    @FXML
    private void saveWorldName() {
        if (worldNameTextField.getText().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Name");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid name for this world");
            alert.showAndWait();
        } else {
            SettingsHandler.saveGameDataWorldName(world_id, worldNameTextField.getText());
        }
    }

    @FXML
    private void onKeyPressedKeyBinding(KeyEvent event) {
        String newKey = event.getCode().toString();
        TextField sourceTextField = (TextField) event.getSource();
        sourceTextField.setText(newKey);

        SettingsHandler.updateKeyBindingValue(sourceTextField.getId(), newKey);
    }

    @FXML
    private void onKeyReleasedKeyBinding() {
        anchorPane.requestFocus();
    }

    @FXML
    private void goToStartMenu() throws IOException {
        StartPage.setScene("startMenu");
    }

    @FXML
    private void goBackScene() throws IOException {
        switch (cameFromMenu) {
            case "startPage":
                StartPage.setScene("startMenu");
                break;
            case "flightSimulator":
                GameData gameData = SettingsHandler.readGameData(world_id);
                FlightSimulatorGame game = new FlightSimulatorGame(gameData);
                StartPage.setScene(game.startGame());
                break;
            case "savedWorldsMenu":
                StartPage.setScene("savedWorldsMenu");
                break;
            case "createWorldMenu":
                StartPage.setScene("createWorldMenu");
                break;
            default:
                break;
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        anchorPane.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            // if ESC is pressed in settings
            if (e.getCode().equals(KeyCode.ESCAPE)) {
                Node focusNode = anchorPane.getScene().getFocusOwner();
                // if ESC in keybindings textfield
                if (!(focusNode instanceof TextField)) {
                    try {
                        goBackScene();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        switch (cameFromMenu) {
            case "startPage":
                homeIcon.setVisible(false);
                worldNameTextField.setDisable(true);
                saveWorldName.setDisable(true);
                saveAndExitButton.setDisable(true);
                saveGameButton.setDisable(true);
                break;
            case "flightSimulator":
                homeIcon.setVisible(true);
                break;
            case "savedWorldsMenu":
                homeIcon.setVisible(true);
                worldNameTextField.setDisable(true);
                saveWorldName.setDisable(true);
                saveAndExitButton.setDisable(true);
                saveGameButton.setDisable(true);
                break;
            case "createWorldMenu":
                homeIcon.setVisible(true);
                worldNameTextField.setDisable(true);
                saveWorldName.setDisable(true);
                saveAndExitButton.setDisable(true);
                saveGameButton.setDisable(true);
            default:
                break;
        }

        GameData data = SettingsHandler.readGameData(world_id);
        seedTextField.setText(String.valueOf(data.getSeed()));
        worldNameTextField.setText(data.getWorldName());

        keyBindingFields = new ArrayList<>();
        getKeyBindingsFromSettings();
    }

    private void getKeyBindingsFromSettings() {
        int i = 0;
        for (Node node : keyBindingsVBox.getChildren()) {
            if (node instanceof TextField) {
                // set saved values to the textfields
                TextField textField = (TextField) node;
                textField.setText(SettingsHandler.getKeyBindingValue(listOfKeys[i]));
                keyBindingFields.add(textField);
                i++;
            }
        }
    }
}
