package com.javafx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class SettingsHandler {
    private static final String DB_URL = "jdbc:sqlite:3D/target/user_data.db";
    private static final String TABLE_NAME = "user_settings";

    public static void initializeDatabase() {
        // create connection and create tables
        try {
            Connection connection = DriverManager.getConnection(DB_URL);

            Statement statement = connection.createStatement();
            String sqlCreateUserSettingsTable = "CREATE TABLE IF NOT EXISTS user_settings ("
                    + "setting_id INTEGER PRIMARY KEY NOT NULL"
                    + ");";

            String sqlCreateKeyBindingsTable = "CREATE TABLE IF NOT EXISTS key_bindings ("
                    + "binding_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + "setting_id INTEGER NOT NULL,"
                    + "binding_key TEXT NOT NULL,"
                    + "binding_value TEXT NOT NULL,"
                    + "FOREIGN KEY (setting_id) REFERENCES user_settings (setting_id)"
                    + ");";

            String sqlCreateUserWorldsTable = "CREATE TABLE IF NOT EXISTS user_worlds ("
                    + "world_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + "setting_id INTEGER NOT NULL,"
                    + "world_name TEXT,"
                    + "latest_save TEXT,"
                    + "creation_time TEXT NOT NULL,"
                    + "seed INTEGER NOT NULL,"
                    + "position_x NUMERIC NOT NULL,"
                    + "position_y NUMERIC NOT NULL,"
                    + "position_z NUMERIC NOT NULL,"
                    + "rotation_x NUMERIC NOT NULL,"
                    + "rotation_y NUMERIC NOT NULL,"
                    + "rotation_z NUMERIC NOT NULL,"
                    + "isInFreeFlyMode INTEGER NOT NULL," // 1 = true, 0 = false
                    + "latest_screenshot BLOB,"
                    + "FOREIGN KEY (setting_id) REFERENCES user_settings (setting_id)"
                    + ");";

            statement.executeUpdate(sqlCreateUserSettingsTable);
            statement.executeUpdate(sqlCreateKeyBindingsTable);
            statement.executeUpdate(sqlCreateUserWorldsTable);


            String sqlSetDefaultUserSettings = "INSERT INTO user_settings DEFAULT VALUES";
            statement.executeUpdate(sqlSetDefaultUserSettings);

            insertDefaultKeyBindings(connection);

            connection.close();
            statement.close();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static void setDefaultKeyBindings() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);

            String sqlSetDefault = "UPDATE key_bindings SET binding_key = ? , "
                    + "binding_value = ?"
                    + "WHERE binding_id = ?;";

            String[][] propertiesList = {
                    { "Key-RotateUp", "UP" },
                    { "Key-FlyForward", "W" },
                    { "Key-Decelerate", "S" },
                    { "Key-RotateDown", "DOWN" },
                    { "Key-TurnLeft", "A" },
                    { "Key-TurnRight", "D" },
                    { "Key-SettingsMenu", "ESCAPE" }
            };

            PreparedStatement pstmt = connection.prepareStatement(sqlSetDefault);

            var i = 1;
            for (String[] binding : propertiesList) {
                pstmt.setString(1, binding[0]);
                pstmt.setString(2, binding[1]);
                pstmt.setInt(3, i);

                pstmt.executeUpdate();
                i++;
            }
            pstmt.close();
            connection.close();

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static void setDefaultKeyBindingsFree() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);

            String sqlSetDefault = "UPDATE key_bindings SET binding_key = ? , "
                    + "binding_value = ?"
                    + "WHERE binding_id = ?;";

            String[][] propertiesList = {
                    { "KeyF-Forward", "W" },
                    { "KeyF-Left", "A" },
                    { "KeyF-Right", "D" },
                    { "KeyF-Backward", "S" },
                    { "KeyF-Down", "SHIFT" },
                    { "KeyF-Up", "SPACE" },
                    { "KeyF-RotateLeft", "LEFT" },
                    { "KeyF-RotateRight", "RIGHT" },
                    { "KeyF-RotateUp", "UP" },
                    { "KeyF-RotateDown", "DOWN" },
                    { "KeyF-SettingsMenu", "ESCAPE" }
            };

            PreparedStatement pstmt = connection.prepareStatement(sqlSetDefault);

            var i = 1;
            for (String[] binding : propertiesList) {
                pstmt.setString(1, binding[0]);
                pstmt.setString(2, binding[1]);
                pstmt.setInt(3, i);

                pstmt.executeUpdate();
                i++;
            }
            pstmt.close();
            connection.close();

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static void createGame(int seed, String worldName, boolean isInFreeFlyMode) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            String sqlCreateGame = "INSERT INTO user_worlds (setting_id, world_name, creation_time, seed, position_x, position_y, position_z, rotation_x, rotation_y, rotation_z, isInFreeFlyMode, latest_screenshot) "
                    + "VALUES (1, ?, DATETIME('now', 'localtime'), ?, 0, 20, 0, 180, 0, 0, ?, -0);";
            PreparedStatement pstmt = connection.prepareStatement(sqlCreateGame);

            pstmt.setString(1, worldName);
            pstmt.setInt(2, seed);
            pstmt.setInt(3, isInFreeFlyMode ? 1 : 0); // 1 = true, 0 = false
            pstmt.executeUpdate();

            pstmt.close();
            connection.close();

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static void saveGameData(int worldId, double positionX, double positionY, double positionZ,
            double rotationX, double rotationY, double rotationZ, boolean isInFreeFlyMode) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            String sqlSaveGameData = "UPDATE user_worlds "
                    + "SET latest_save = DATETIME('now', 'localtime'), position_x = ?, position_y = ?, position_z = ?, "
                    + "rotation_x = ?, rotation_y = ?, rotation_z = ?, isInFreeFlyMode = ? "
                    + "WHERE world_id = ? ;";

            PreparedStatement pstmt = connection.prepareStatement(sqlSaveGameData);
            pstmt.setDouble(1, positionX);
            pstmt.setDouble(2, positionY);
            pstmt.setDouble(3, positionZ);
            pstmt.setDouble(4, rotationX);
            pstmt.setDouble(5, rotationY);
            pstmt.setDouble(6, rotationZ);
            pstmt.setInt(7, isInFreeFlyMode ? 1 : 0);

            pstmt.setInt(8, worldId);

            pstmt.executeUpdate();

            pstmt.close();
            connection.close();

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static void saveGameDataWorldName(int worldId, String worldName) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            String sqlSetWorldName = "UPDATE user_worlds "
                    + "SET world_name = ? "
                    + "WHERE world_id = ? ;";

            PreparedStatement pstmt = connection.prepareStatement(sqlSetWorldName);
            pstmt.setString(1, worldName);
            pstmt.setInt(2, worldId);

            pstmt.executeUpdate();

            pstmt.close();
            connection.close();

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static void setIsInFreeFlyMode(int worldId, boolean isInFreeFlyMode) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            String sqlSetWorldName = "UPDATE user_worlds "
                    + "SET isInFreeFlyMode = ? "
                    + "WHERE world_id = ? ;";

            PreparedStatement pstmt = connection.prepareStatement(sqlSetWorldName);
            pstmt.setInt(1, isInFreeFlyMode ? 1 : 0);
            pstmt.setInt(2, worldId);

            pstmt.executeUpdate();

            pstmt.close();
            connection.close();

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static GameData readGameData(int worldId) {
        GameData gameData = new GameData();

        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            String sqlGetGameData = "SELECT * "
                    + "FROM user_worlds "
                    + "WHERE world_id = ? ;";

            PreparedStatement pstmt = connection.prepareStatement(sqlGetGameData);
            pstmt.setInt(1, worldId);
            ResultSet result = pstmt.executeQuery();

            while (result.next()) {
                gameData.setWorldId(result.getInt("world_id"));
                gameData.setSettingId(result.getInt("setting_id"));
                gameData.setWorldName(result.getString("world_name"));
                gameData.setLatestSave(result.getString("latest_save"));
                gameData.setCreationTime(result.getString("creation_time"));
                gameData.setSeed(result.getInt("seed"));
                gameData.setPositionX(result.getDouble("position_x"));
                gameData.setPositionY(result.getDouble("position_y"));
                gameData.setPositionZ(result.getDouble("position_z"));
                gameData.setRotationX(result.getDouble("rotation_x"));
                gameData.setRotationY(result.getDouble("rotation_y"));
                gameData.setRotationZ(result.getDouble("rotation_z"));
                gameData.setInFreeFlyMode(result.getInt("isInFreeFlyMode") == 1 ? true : false); // 1 = true, 0 = false
                byte[] imageBytes = result.getBytes("latest_screenshot");
                if (imageBytes != null) {
                    gameData.setLatestScreenshot(imageBytes);
                }
            }

            pstmt.close();
            connection.close();

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return gameData;
    }

    public static List<GameData> readAllGameData() {
        List<GameData> gameDataList = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            String sqlGetAllGameData = "SELECT * FROM user_worlds;";

            PreparedStatement pstmt = connection.prepareStatement(sqlGetAllGameData);
            ResultSet result = pstmt.executeQuery();

            while (result.next()) {
                GameData gameData = new GameData();
                gameData.setWorldId(result.getInt("world_id"));
                gameData.setSettingId(result.getInt("setting_id"));
                gameData.setWorldName(result.getString("world_name"));
                gameData.setLatestSave(result.getString("latest_save"));
                gameData.setCreationTime(result.getString("creation_time"));
                gameData.setSeed(result.getInt("seed"));
                gameData.setPositionX(result.getDouble("position_x"));
                gameData.setPositionY(result.getDouble("position_y"));
                gameData.setPositionZ(result.getDouble("position_z"));
                gameData.setRotationX(result.getDouble("rotation_x"));
                gameData.setRotationY(result.getDouble("rotation_y"));
                gameData.setRotationZ(result.getDouble("rotation_z"));
                gameData.setInFreeFlyMode(result.getInt("isInFreeFlyMode") == 0 ? false : true); // 1 = true, 0 = false

                byte[] imageBytes = result.getBytes("latest_screenshot");
                if (imageBytes != null) {
                    gameData.setLatestScreenshot(imageBytes);
                }

                gameDataList.add(gameData);
            }

            pstmt.close();
            connection.close();

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return gameDataList;
    }

    public static void deleteWorld(int worldId) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            String sqlDeleteWorld = "DELETE FROM user_worlds WHERE world_id = ?;";

            PreparedStatement pstmt = connection.prepareStatement(sqlDeleteWorld);
            pstmt.setInt(1, worldId);

            pstmt.executeUpdate();

            pstmt.close();
            connection.close();

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static int getNewestWorldId() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);

            String sqlGetNewestWorldId = "SELECT MAX(world_id) AS max_id "
                    + "FROM user_worlds;";

            PreparedStatement pstmt = connection.prepareStatement(sqlGetNewestWorldId);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                int maxId = result.getInt("max_id");
                pstmt.close();
                connection.close();
                return maxId;
            } else {
                pstmt.close();
                connection.close();
                return -1;
            }

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            return -1;
        }
    }

    private static void insertDefaultKeyBindings(Connection connection) {
        try {
            String sqlInsertDefaultKeyBinding = "INSERT INTO key_bindings (setting_id, binding_key, binding_value) "
                    + "VALUES (1, ?, ?);";
            PreparedStatement pstmt = connection.prepareStatement(sqlInsertDefaultKeyBinding);

            String[][] propertiesList = {
                    { "Key-RotateUp", "UP" },
                    { "Key-FlyForward", "W" },
                    { "Key-Decelerate", "S" },
                    { "Key-RotateDown", "DOWN" },
                    { "Key-TurnLeft", "A" },
                    { "Key-TurnRight", "D" },
                    { "Key-SettingsMenu", "ESCAPE" },

                    { "KeyF-Forward", "W" },
                    { "KeyF-Left", "A" },
                    { "KeyF-Right", "D" },
                    { "KeyF-Backward", "S" },
                    { "KeyF-Down", "SHIFT" },
                    { "KeyF-Up", "SPACE" },
                    { "KeyF-RotateLeft", "LEFT" },
                    { "KeyF-RotateRight", "RIGHT" },
                    { "KeyF-RotateUp", "UP" },
                    { "KeyF-RotateDown", "DOWN" },
                    { "KeyF-SettingsMenu", "ESCAPE" }
            };

            for (String[] binding : propertiesList) {
                pstmt.setString(1, binding[0]);
                pstmt.setString(2, binding[1]);

                pstmt.executeUpdate();
            }

            pstmt.close();

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static void updateKeyBindingValue(String bindingKey, String bindingValue) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);

            String sqlUpdateKeyBindingValue = "UPDATE key_bindings "
                    + "SET binding_value = ? "
                    + "WHERE binding_Key = ? ;";

            PreparedStatement pstmt = connection.prepareStatement(sqlUpdateKeyBindingValue);
            pstmt.setString(1, bindingValue);
            pstmt.setString(2, bindingKey);

            pstmt.executeUpdate();

            pstmt.close();
            connection.close();

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static String getKeyBindingValue(String bindingKey) {
        String bindingValue = "error"; // default value in case no rows are found

        try {
            Connection connection = DriverManager.getConnection(DB_URL);

            String sqlGetKeyBinding = "SELECT binding_value "
                    + "FROM key_bindings "
                    + "WHERE binding_key = ? ;";

            PreparedStatement pstmt = connection.prepareStatement(sqlGetKeyBinding);
            pstmt.setString(1, bindingKey);

            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                bindingValue = result.getString("binding_value");
            }

            connection.close();
            pstmt.close();

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return bindingValue;
    }

    // public static Image screenshot(Scene scene) {
    // WritableImage image = scene.snapshot(null);

    // byte[] imageBytes = writableImageToByteArray(image);

    // return byteArrayToImage(imageBytes, (int) image.getWidth(), (int)
    // image.getHeight());
    // }

    public static void saveScreenshot(int worldId, Scene scene) {
        WritableImage image = scene.snapshot(null);
        byte[] imageBytes = writableImageToByteArray(image);

        try {
            Connection connection = DriverManager.getConnection(DB_URL);

            String sqlUpdateLatestScreenshot = "UPDATE user_worlds "
                    + "SET latest_screenshot = ? "
                    + "WHERE world_id = ? ;";

            PreparedStatement pstmt = connection.prepareStatement(sqlUpdateLatestScreenshot);
            pstmt.setBytes(1, imageBytes);
            pstmt.setInt(2, worldId);

            pstmt.executeUpdate();

            pstmt.close();
            connection.close();

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static Image byteArrayToImage(byte[] imageBytes, int width, int height) {
        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelIndex = (y * width + x) * 4;
                int red = (imageBytes[pixelIndex] & 0xFF);
                int green = (imageBytes[pixelIndex + 1] & 0xFF);
                int blue = (imageBytes[pixelIndex + 2] & 0xFF);
                int alpha = (imageBytes[pixelIndex + 3] & 0xFF);

                Color color = Color.rgb(red, green, blue, alpha / 255.0);
                pixelWriter.setColor(x, y, color);
            }
        }

        return writableImage;
    }

    public static byte[] writableImageToByteArray(WritableImage writableImage) {
        PixelReader pixelReader = writableImage.getPixelReader();
        int width = (int) writableImage.getWidth();
        int height = (int) writableImage.getHeight();
        byte[] imageBytes = new byte[width * height * 4];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelIndex = (y * width + x) * 4;
                Color color = pixelReader.getColor(x, y);
                imageBytes[pixelIndex] = (byte) (color.getRed() * 255);
                imageBytes[pixelIndex + 1] = (byte) (color.getGreen() * 255);
                imageBytes[pixelIndex + 2] = (byte) (color.getBlue() * 255);
                imageBytes[pixelIndex + 3] = (byte) (color.getOpacity() * 255);
            }
        }

        return imageBytes;
    }
}
