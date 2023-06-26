package com.javafx;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class SettingsHandler {
    private static Properties properties = new Properties();
    private static final File SETTINGS_FILE = new File("3D/target/userSettings.properties");

    public static void readSettings() {
        try {
            if (SETTINGS_FILE.exists()) {
                FileReader reader = new FileReader(SETTINGS_FILE);
                properties.load(reader);
                reader.close();
            } else {
                // set default settings
                setDefaultKeyBindings();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    private static void saveSettings() {
        FileWriter writer;
        try {
            writer = new FileWriter(SETTINGS_FILE);
            properties.store(writer, "User Settings");
            writer.close();
        } catch (IOException e) {
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
