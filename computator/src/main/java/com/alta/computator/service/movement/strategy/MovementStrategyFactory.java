package com.alta.computator.service.movement.strategy;

import lombok.experimental.UtilityClass;

/**
 * Provides the factory to get {@link MovementStrategy} specific instance
 */
@UtilityClass
public class MovementStrategyFactory {

    /**
     * Gets the strategy instance by given type.
     *
     * @param strategy - the strategy type
     * @return the {@link MovementStrategy} instance.
     */
    public MovementStrategy getStrategy(Strategy strategy) {
        switch (strategy) {
            case AVOID_OBSTRUCTION:
                return new AvoidObstructionStrategy();
            default:
                return null;
        }
    }

    /**
     * Provides possibles strategies
     */
    public enum Strategy {
        AVOID_OBSTRUCTION
    }

}
