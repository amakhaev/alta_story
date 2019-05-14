package com.alta.computator.service.movement;

import com.alta.computator.service.movement.directionCalculation.AvoidObstructionDirectionCalculator;
import com.alta.computator.service.movement.directionCalculation.DirectionCalculator;
import com.alta.computator.service.movement.directionCalculation.StandSpotDirectionCalculator;
import lombok.experimental.UtilityClass;

/**
 * Provides the factory to get {@link MovementComputator} specific instance
 */
@UtilityClass
public class MovementFactory {

    /**
     * Gets the movement strategy.
     *
     * @return the {@link MovementComputator} instance.
     */
    public MovementComputator createComputator() {
        return new MovementComputatorImpl();
    }

    /**
     * Gets the direction calculator by given movement type.
     *
     * @param movementType - the type of movement.
     * @return the {@link DirectionCalculator} instance.
     */
    public DirectionCalculator createDirectionCalculator(MovementType movementType) {
        switch (movementType) {
            case AVOID_OBSTRUCTION:
                return new AvoidObstructionDirectionCalculator();
            case STAND_SPOT:
                return new StandSpotDirectionCalculator();
            default:
                return null;
        }
    }

}
