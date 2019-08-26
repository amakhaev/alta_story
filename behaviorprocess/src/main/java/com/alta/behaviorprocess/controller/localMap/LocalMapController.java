package com.alta.behaviorprocess.controller.localMap;

import java.awt.*;

/**
 * Provides the controller to manage local maps.
 */
public interface LocalMapController {

    /**
     * Jumps to another map.
     *
     * @param mapName               - the name of map where acting character stay currently.
     * @param mapStartCoordinate    - the coordinates on map of acting character.
     */
    void jumpToMap(String mapName, Point mapStartCoordinate);

}
