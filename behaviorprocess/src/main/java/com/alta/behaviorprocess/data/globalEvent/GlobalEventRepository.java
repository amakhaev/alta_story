package com.alta.behaviorprocess.data.globalEvent;

import java.awt.*;

/**
 * Provides the repository tp handle global events.
 */
public interface GlobalEventRepository {

    /**
     * Saves the state of game.
     *
     * @param mapName                       - the name of map where acting character stay currently.
     * @param actionCharacterSkin           - the skin name of acting character.
     * @param actionCharacterMapCoordinate  - the coordinates on map of acting character.
     */
    void saveState(String mapName, String actionCharacterSkin, Point actionCharacterMapCoordinate);

}
