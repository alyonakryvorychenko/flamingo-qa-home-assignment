package com.flamingo.qa.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

public final class ConfigProvider {

    private static final Properties PROPERTIES = load();

    private ConfigProvider() {
    }

    public static String get(String key) {
        return get(key, null);
    }

    public static String get(String key, String defaultValue) {
        String systemProperty = System.getProperty(key);
        if (systemProperty != null && !systemProperty.isBlank()) {
            return systemProperty;
        }

        String envVariable = System.getenv(key);
        if (envVariable != null && !envVariable.isBlank()) {
            return envVariable;
        }

        return PROPERTIES.getProperty(key, defaultValue);
    }

    private static Properties load() {
        Properties properties = new Properties();
        try (InputStream input = ConfigProvider.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to load config.properties", e);
        }
        return properties;
    }
}
