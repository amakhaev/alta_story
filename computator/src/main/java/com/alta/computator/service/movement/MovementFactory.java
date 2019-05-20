package com.alta.computator.service.movement;

import com.alta.computator.service.movement.directionCalculation.*;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.List;

/**
 * Provides the factory to get {@link MovementComputator} specific instance
 */
@Slf4j
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
     * Creates the movement computator with given movement speed.
     *
     * @param movementSpeed - the speed of movement.
     * @return created {@link MovementComputator} instance.
     */
    public MovementComputator createComputator(int movementSpeed) {
        return new MovementComputatorImpl(movementSpeed);
    }

    /**
     * Creates the strategy of movement for focus point.
     *
     * @return created {@link MovementDirectionStrategy} instance.
     */
    public MovementDirectionStrategy createFocusPointStrategy() {
        return new AvoidObstructionMovementStrategy();
    }

    /**
     * Creates the strategy of movement for simple NPC.
     *
     * @param type - the type of movement direction.
     * @return created {@link MovementDirectionStrategy} instance.
     */
    public MovementDirectionStrategy createSimpleNpcStrategy(MovementType type) {
        switch (type) {
            case AVOID_OBSTRUCTION:
                return new AvoidObstructionMovementStrategy();
            case STAND_SPOT:
                return new StandSpotMovementStrategy();
            default:
                log.error("Unknown type of movement: {}. Default strategy will be StandSpotMovementStrategy", type);
                return new StandSpotMovementStrategy();
        }
    }

    /**
     * Creates the strategy for NPC that has route.
     *
     * @param isRouteLooped     - indicates when route should be looped.
     * @param routeDescription  - the description of route.
     * @return created {@link MovementDirectionStrategy}.
     */
    public RouteMovementDirectionStrategy createRouteNpcStrategy(boolean isRouteLooped,
                                                                 List<RouteMovementDescription> routeDescription) {
        return new RoutePointsMovementStrategy(isRouteLooped, routeDescription);
    }
}
