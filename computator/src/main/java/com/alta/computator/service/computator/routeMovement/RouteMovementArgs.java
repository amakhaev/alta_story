package com.alta.computator.service.computator.routeMovement;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.service.computator.ComputatorArgs;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

/**
 * Provides the arguments to be used for computation for route movement.
 */
@Getter
@Setter
public class RouteMovementArgs implements ComputatorArgs {

    private AltitudeMap altitudeMap;
    private Point focusPointGlobalCoordinates;
    private int delta;
    private boolean runImmediately;

}
