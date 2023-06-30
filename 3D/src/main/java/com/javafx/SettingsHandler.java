package com.javafx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class SettingsHandler {
    private static Properties properties = new Properties();
    private static final String DB_URL = "jdbc:sqlite:3D/target/user_data.db";
    private static final String TABLE_NAME = "user_settings";

    public static void readSettings() {
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

            statement.executeUpdate(sqlCreateUserSettingsTable);
            statement.executeUpdate(sqlCreateKeyBindingsTable);

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
            if (keyBindingsRowCount == 0) {
                setDefaultKeyBindings(connection);
            }

            connection.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void setDefaultKeyBindings(Connection connection) {
        try {
            String sqlInsertDefaultKeyBinding = "INSERT INTO key_bindings (setting_id, binding_key, binding_value) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sqlInsertDefaultKeyBinding);
            pstmt.setInt(1, 1);
            
            pstmt.setString(2, "Key-RotateUp");
            pstmt.setString(3, "UP");
            pstmt.executeUpdate();

            pstmt.setString(2, "Key-FlyForward");
            pstmt.setString(3, "W");
            pstmt.executeUpdate();

            pstmt.setString(2, "Key-Decelerate");
            pstmt.setString(3, "S");
            pstmt.executeUpdate();

            pstmt.setString(2, "Key-RotateDown");
            pstmt.setString(3, "DOWN");
            pstmt.executeUpdate();
            
            pstmt.setString(2, "Key-TurnLeft");
            pstmt.setString(3, "A");
            pstmt.executeUpdate();

            pstmt.setString(2, "Key-TurnRight");
            pstmt.setString(3, "D");
            pstmt.executeUpdate();

            pstmt.setString(2, "Key-SettingsMenu");
            pstmt.setString(3, "ESCAPE");
            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void createDefault() {
        // setDefaultKeyBindings();
        // setDefaultSeed();
    }

    // private static void setDefaultSeed() {
    // properties.setProperty("seed", "1");
    // saveSettings();
    // }

    public static void setDefaultKeyBindings() {
        properties.setProperty("Key-RotateUp", "UP");
        properties.setProperty("Key-FlyForward", "W");
        properties.setProperty("Key-Decelerate", "S");
        properties.setProperty("Key-RotateDown", "DOWN");
        properties.setProperty("Key-TurnLeft", "A");
        properties.setProperty("Key-TurnRight", "D");
        properties.setProperty("Key-SettingsMenu", "ESCAPE");
        saveSettings();
    }

    // private static void saveSettings() {
    // FileWriter writer;
    // try {
    // writer = new FileWriter(SETTINGS_FILE);
    // properties.store(writer, "User Settings");
    // writer.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

    private static void saveSettings() {
        try (Connection connection = DriverManager.getConnection(DB_URL);
                PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM " + TABLE_NAME);
                PreparedStatement insertStatement = connection.prepareStatement(
                        "INSERT INTO " + TABLE_NAME + " (setting_key, setting_value) VALUES (?, ?)")) {

            deleteStatement.executeUpdate();

            for (String key : properties.stringPropertyNames()) {
                insertStatement.setString(1, key);
                insertStatement.setString(2, properties.getProperty(key));
                insertStatement.addBatch();
            }

            insertStatement.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void put(String key, String value) {
        properties.setProperty(key, value);
        saveSettings();
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
