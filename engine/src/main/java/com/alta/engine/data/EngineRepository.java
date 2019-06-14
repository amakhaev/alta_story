package com.alta.engine.data;

import java.awt.*;

/**
 * Provides the repository to make CRUD with data related to engine in general.
 */
public interface EngineRepository {

    /**
     * Saves the state of game.
     *
     * @param mapName                       - the name of map where acting character stay currently.
     * @param actionCharacterSkin           - the skin name of acting character.
     * @param actionCharacterMapCoordinate  - the coordinates on map of acting character.
     */
    void saveState(String mapName, String actionCharacterSkin, Point actionCharacterMapCoordinate);

    /**
     * Makes the jump to another map.
     *
     * @param mapName               - the name of map where acting character stay currently.
     * @param mapStartCoordinate    - the coordinates on map of acting character.
     */
    void makeJumping(String mapName, Point mapStartCoordinate);
}
