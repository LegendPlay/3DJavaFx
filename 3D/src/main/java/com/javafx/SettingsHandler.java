package com.javafx;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class SettingsHandler {
    private static Properties properties = new Properties();
    private static final File SETTINGS_FILE = new File("./target/userSettings.properties");

    public static void readSettings() {
        try {
            if (SETTINGS_FILE.exists()) {
                FileReader reader = new FileReader(SETTINGS_FILE);
                properties.load(reader);
                reader.close();
            } else {
                // set default settings
                // e.g. properties.setProperty("sound", "true");

                saveSettings();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
