package com.hybrid.config;

import com.hybrid.constants.FrameworkConstants;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties PROPERTIES;

    static {
        try (FileInputStream fis = new FileInputStream(FrameworkConstants.CONFIG_FILE_PATH)) {
            PROPERTIES = new Properties();
            PROPERTIES.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load config.properties: " + e.getMessage(), e);
        }
    }

    private ConfigReader() {}

    public static String get(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Key '" + key + "' not found in config.properties");
        }
        return value.trim();
    }
}
