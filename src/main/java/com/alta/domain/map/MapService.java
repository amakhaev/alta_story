package com.alta.domain.map;

import java.io.File;

/**
 * Provides the service for working with maps.
 */
public class MapService {

    private static final String MAP_FOLDER = "maps";

    /**
     * Gets the path to map be name.
     * @param name - the name of map.
     * @return the path to map;
     */
    public String getPath(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }

        String path = MAP_FOLDER + "/" + name + "/map.tmx";
        return new File(path).exists() ? path : null;
    }

}
