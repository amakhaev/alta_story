package com.alta.engine.processing.listener.engineEvent;

import java.awt.*;

/**
 * Provides the listener of any events that int the engine
 */
public interface EngineListener {

    /**
     * Handles the jumping between maps.
     *
     * @param mapName -             the name of map to jumping
     * @param mapStartCoordinates - the map (tile) coordinates of acting character on target map
     */
    void onJumping(String mapName, Point mapStartCoordinates);

}
