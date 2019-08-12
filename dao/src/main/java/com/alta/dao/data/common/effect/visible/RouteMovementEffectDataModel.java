package com.alta.dao.data.common.effect.visible;

import com.alta.dao.data.common.effect.EffectDataModel;
import lombok.Builder;
import lombok.Getter;

/**
 * Provides the model that describes the route movement.
 */
@Getter
public class RouteMovementEffectDataModel extends EffectDataModel {

    private final String targetUuid;
    private final String finalDirection;
    private final String movementSpeed;
    private final int x;
    private final int y;

    /**
     * Initialize new instance of {@link RouteMovementEffectDataModel}.
     *
     * @param targetUuid        - the uuid of target.
     * @param finalDirection    - the final direction of participant.
     * @param movementSpeed     - the speed of movement.
     * @param x                 - the target coordinate of X axis.
     * @param y                 - the target coordinate of Y axis.
     */
    @Builder
    public RouteMovementEffectDataModel(String targetUuid, String finalDirection, String movementSpeed, int x, int y) {
        super(EffectType.ROUTE_MOVEMENT);
        this.targetUuid = targetUuid;
        this.finalDirection = finalDirection;
        this.movementSpeed = movementSpeed;
        this.x = x;
        this.y = y;
    }
}
