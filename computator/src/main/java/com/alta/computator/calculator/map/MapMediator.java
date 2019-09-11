package com.alta.computator.calculator.map;

import java.awt.*;

/**
 * Provides the mediator to control map.
 */
public interface MapMediator {

    /**
     * Gets the global coordinates of map.
     *
     * @return the {@link Point} instance.
     */
    Point getMapGlobalCoordinates();

}
