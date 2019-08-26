package com.alta.behaviorprocess.data.localMap;

import java.awt.*;

/**
 * Provides the repository that can make actions on a local map.
 */
public interface LocalMapRepository {

    /**
     * Makes the jump to another map.
     *
     * @param mapName               - the name of map where acting character stay currently.
     * @param mapStartCoordinate    - the coordinates on map of acting character.
     */
    void makeJumping(String mapName, Point mapStartCoordinate);

}
