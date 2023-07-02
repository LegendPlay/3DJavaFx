package com.javafx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
                    + "FOREIGN KEY (setting_id) REFERENCES user_settings (setting_id)"
                    + ");";

            statement.executeUpdate(sqlCreateUserSettingsTable);
            statement.executeUpdate(sqlCreateKeyBindingsTable);
            statement.executeUpdate(sqlCreateUserWorldsTable);

            // check if all columns are there
            String sqlCheckUserSettings = "SELECT COUNT(*) FROM user_settings";
            ResultSet resultSet = statement.executeQuery(sqlCheckUserSettings);
            int userSettingsRowCount = 0;
            if (resultSet.next()) {
                userSettingsRowCount = resultSet.getInt(1);
            }
            resultSet.close();

            // if user_settings table empty, insert a default row
            if (userSettingsRowCount == 0) {
                String sqlSetDefaultUserSettings = "INSERT INTO user_settings DEFAULT VALUES";
                statement.executeUpdate(sqlSetDefaultUserSettings);
            }

            String sqlCheckKeyBindings = "SELECT COUNT(*) FROM key_bindings";
            resultSet = statement.executeQuery(sqlCheckKeyBindings);
            int keyBindingsRowCount = 0;
            if (resultSet.next()) {
                keyBindingsRowCount = resultSet.getInt(1);
            }
            resultSet.close();

            // if key_bindings table empty, insert default key bindings
            if (keyBindingsRowCount < 4) {
                insertDefaultKeyBindings(connection);
            }

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

    public static void createGame(int seed, String worldName) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            String sqlCreateGame = "INSERT INTO user_worlds (setting_id, world_name, creation_time, seed, position_x, position_y, position_z, rotation_x, rotation_y, rotation_z) "
                    + "VALUES (1, ?, DATETIME('now', 'localtime'), ?, 0, 20, 0, 180, 0, 0);";
            PreparedStatement pstmt = connection.prepareStatement(sqlCreateGame);

            pstmt.setString(1, worldName);
            pstmt.setInt(2, seed);
            pstmt.executeUpdate();

            pstmt.close();
            connection.close();

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static void saveGameData(int worldId, double positionX, double positionY, double positionZ,
            double rotationX, double rotationY, double rotationZ) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            String sqlSaveGameData = "UPDATE user_worlds "
                    + "SET latest_save = DATETIME('now', 'localtime'), position_x = ?, position_y = ?, position_z = ?, "
                    + "rotation_x = ?, rotation_y = ?, rotation_z = ? "
                    + "WHERE world_id = ? ;";

            PreparedStatement pstmt = connection.prepareStatement(sqlSaveGameData);
            pstmt.setDouble(1, positionX);
            pstmt.setDouble(2, positionY);
            pstmt.setDouble(3, positionZ);
            pstmt.setDouble(4, rotationX);
            pstmt.setDouble(5, rotationY);
            pstmt.setDouble(6, rotationZ);

            pstmt.setInt(7, worldId);

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

    public static GameData readGameData(int worldId) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            String sqlGetGameData = "SELECT * "
                    + "FROM user_worlds "
                    + "WHERE world_id = ? ;";

            PreparedStatement pstmt = connection.prepareStatement(sqlGetGameData);
            pstmt.setInt(1, worldId);
            ResultSet result = pstmt.executeQuery();

            GameData gameData = new GameData();
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
                gameData.setDebug(false); // TODO add in function to swith between keybindings
            }

            pstmt.close();
            connection.close();

            return gameData;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            return null;
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
                return -13;
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
                    { "Key-SettingsMenu", "ESCAPE" }
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
        try {
            Connection connection = DriverManager.getConnection(DB_URL);

            String sqlGetKeyBinding = "SELECT binding_value "
                    + "FROM key_bindings "
                    + "WHERE binding_key = ? ;";

            PreparedStatement pstmt = connection.prepareStatement(sqlGetKeyBinding);
            pstmt.setString(1, bindingKey);

            ResultSet result = pstmt.executeQuery();

            String bindingValue = "error"; // Default value in case no rows are found
            if (result.next()) {
                // Check if the result set has a row before accessing the value
                bindingValue = result.getString("binding_value");
            }

            connection.close();
            pstmt.close();

            return bindingValue;

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            return "error";
        }
    }
}
