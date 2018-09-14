package com.alta.utils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Provides the utility class to work with resource
 */
public class ResourceUtils {

    private static ResourceBundle resourceBundle;

    /**
     * Gets the key from localization file.
     *
     * @param key - the key.
     * @return the string values by key
     */
    public static String getString(String key) {
        if (resourceBundle == null) {
            resourceBundle = ResourceBundle.getBundle("localization", new Locale("en", "US"));
        }

        return resourceBundle.getString(key);
    }

}
