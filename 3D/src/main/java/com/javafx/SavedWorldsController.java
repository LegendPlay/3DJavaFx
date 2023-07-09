package com.javafx;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class SavedWorldsController {
    private ObservableList<GameData> gameDataList;

    @FXML
    private Button playWorldButton;

    @FXML
    private Button editWorldButton;

    @FXML
    private Button deleteWorldButton;

    @FXML
    private Button test;

    @FXML
    private ListView<GameData> listOfGames;

    @FXML
    private GameData selectedGameData;

    @FXML
    private void goToSettings() throws IOException {
        StartPage.setSettingsScene("settingsMenu", "savedWorldsMenu", FlightSimulatorGame.world_id);
    }

    @FXML
    private void goBackScene() throws IOException {
        StartPage.setScene("startMenu");
    }

    public void initialize() {
        gameDataList = FXCollections.observableArrayList(SettingsHandler.readAllGameData());
        Collections.reverse(gameDataList);

        listOfGames.setItems(gameDataList);

        listOfGames.setCellFactory(param -> new ListCell<GameData>() {
            private VBox container = new VBox();

            {
                container.setAlignment(Pos.CENTER_LEFT);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setGraphic(container);
            }

            @Override
            protected void updateItem(GameData gameData, boolean empty) {
                super.updateItem(gameData, empty);

                if (empty || gameData == null) {
                    container.getChildren().clear();
                } else {
                    Label nameLabel = new Label(gameData.getWorldName());
                    nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

                    Label timeLabel = new Label("\nCreation: " + gameData.getCreationTime() + "  Latest Save: "
                            + gameData.getLatestSave());

                    String mode = gameData.getIsInFreeFlyMode() ? "Free Fly Mode" : "Realistic Flight Mode";
                    Label gameMode = new Label();
                    Text modeText = new Text(mode);
                    modeText.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                    modeText.setTextOrigin(VPos.BASELINE);
                    gameMode.setGraphic(
                            new HBox(new Text("Gamemode: "), modeText, new Text("  Seed: " + gameData.getSeed())));

                    container.getChildren().setAll(nameLabel, timeLabel, gameMode);
                    container.setPadding(new Insets(5, 5, 5, 5));
                }
            }
        });

        listOfGames.getSelectionModel().selectedItemProperty().addListener((observalbe, oldValue, newValue) -> {
            if (newValue != null) {
                enableButtons();

                selectedGameData = newValue;
            }
        });
    }

    @FXML
    private void playWorld() {
        if (selectedGameData != null) {
            FlightSimulatorGame game = new FlightSimulatorGame(selectedGameData);
            StartPage.setScene(game.startGame());
        }
    }

    @FXML
    private void deleteWorld() {
        if (selectedGameData != null) {
            SettingsHandler.deleteWorld(selectedGameData.getWorldId());
            gameDataList.remove(selectedGameData);
            listOfGames.refresh();
        }
    }

    @FXML
    private void editWorld() {

    }

    private void enableButtons() {
        playWorldButton.setDisable(false);
        editWorldButton.setDisable(false);
        deleteWorldButton.setDisable(false);
    }
}
