package com.example.todo.app.utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getResourceAsStream("/config.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find config.properties");
            }
            properties.load(input); // Load the properties file
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    // Retrieves the path for a given key from the properties file.
    public static String getPath(String key) {
        String path = properties.getProperty(key);
        if (path == null) {
            throw new RuntimeException("Key not found in config.properties: " + key);
        }
        return path;
    }
}