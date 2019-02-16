package com.alta.dao;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ResourcesLocation {

    private final String SCENE_DATA_FOLDER = "scene_data";
    private final String MAPS_DATA_FOLDER = SCENE_DATA_FOLDER + "/maps";

    public final String MAPS_DESCRIPTOR_FILE = MAPS_DATA_FOLDER + "/maps_descriptor.json";

    public final String DATABASE_NAME = "game.db3";
}
