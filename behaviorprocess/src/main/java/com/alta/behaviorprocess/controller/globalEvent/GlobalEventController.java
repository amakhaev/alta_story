package com.alta.behaviorprocess.controller.globalEvent;

import java.awt.*;

/**
 * Provides the controller handle global events.
 */
public interface GlobalEventController {

    /**
     * Saves the state of game.
     *
     * @param mapName                       - the name of map where acting character stay currently.
     * @param actionCharacterSkin           - the skin name of acting character.
     * @param actionCharacterMapCoordinate  - the coordinates on map of acting character.
     */
    void saveGameState(String mapName, String actionCharacterSkin, Point actionCharacterMapCoordinate);

}
