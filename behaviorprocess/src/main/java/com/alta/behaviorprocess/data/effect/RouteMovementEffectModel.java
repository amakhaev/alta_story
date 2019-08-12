package com.alta.behaviorprocess.data.effect;

import lombok.Builder;
import lombok.Getter;

/**
 * Provides the model for route movement.
 */
@Getter
public class RouteMovementEffectModel extends EffectModel {

    private final String targetUuid;
    private final String finalDirection;
    private final int movementSpeed;
    private final int x;
    private final int y;

    /**
     * Initialize new instance of {@link RouteMovementEffectModel}.
     *
     * @param targetUuid        - the uuid of target.
     * @param finalDirection    - the final direction of participant.
     * @param movementSpeed     - the speed of movement.
     * @param x                 - the target coordinate of X axis.
     * @param y                 - the target coordinate of Y axis.
     */
    @Builder
    public RouteMovementEffectModel(String targetUuid, String finalDirection, int movementSpeed, int x, int y) {
        super(EffectType.ROUTE_MOVEMENT);
        this.targetUuid = targetUuid;
        this.finalDirection = finalDirection;
        this.movementSpeed = movementSpeed;
        this.x = x;
        this.y = y;
    }
}
