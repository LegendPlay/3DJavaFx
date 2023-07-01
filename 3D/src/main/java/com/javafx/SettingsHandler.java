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

            statement.executeUpdate(sqlCreateUserSettingsTable);
            statement.executeUpdate(sqlCreateKeyBindingsTable);

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

    private static void insertDefaultKeyBindings(Connection connection) {
        try {
            String sqlInsertDefaultKeyBinding = "INSERT INTO key_bindings (setting_id, binding_key, binding_value) VALUES (?, ?, ?);";
            PreparedStatement pstmt = connection.prepareStatement(sqlInsertDefaultKeyBinding);
            pstmt.setInt(1, 1);

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
                pstmt.setString(2, binding[0]);
                pstmt.setString(3, binding[1]);

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
