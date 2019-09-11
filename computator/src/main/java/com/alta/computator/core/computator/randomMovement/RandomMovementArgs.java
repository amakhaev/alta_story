package com.alta.computator.core.computator.randomMovement;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.core.computator.ComputatorArgs;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

/**
 * Provides the arguments to be used for computation for random movement.
 */
@Getter
@Setter
public class RandomMovementArgs implements ComputatorArgs {

    private AltitudeMap altitudeMap;
    private Point focusPointGlobalCoordinates;
    private int delta;

}
