package com.mydemo.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * config.properties faylını oxuyur.
 * İstənilən dəyəri komanda sətrindən əvəz etmək olar:  mvn test -Dudid=emulator-5554
 */
public final class Config {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new IllegalStateException("config.properties tapılmadı");
            }
            PROPS.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("config.properties oxunmadı", e);
        }
    }

    private Config() {
    }

    public static String get(String key) {
        String value = System.getProperty(key, PROPS.getProperty(key, ""));
        return value == null ? "" : value.trim();
    }

    public static boolean has(String key) {
        return !get(key).isEmpty();
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBool(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public static String appPackage() {
        return get("appPackage");
    }
}
