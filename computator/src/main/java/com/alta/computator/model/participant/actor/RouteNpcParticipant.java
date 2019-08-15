package com.alta.computator.model.participant.actor;

import com.alta.computator.model.participant.ParticipatType;
import com.alta.computator.service.computator.movement.GlobalMovementCalculatorImpl;
import com.alta.computator.service.computator.movement.MovementType;
import com.alta.computator.service.computator.movement.directionCalculation.MovementDirection;
import com.alta.computator.service.computator.movement.directionCalculation.RouteMovementDescription;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.awt.*;
import java.util.List;

/**
 * Provides the participant that describes the route NPC
 */
public class RouteNpcParticipant extends NpcParticipant {

    @Getter
    private final boolean isRouteLooped;

    @Getter
    private final List<RouteMovementDescription> routeDescription;

    @Setter
    @Getter
    private int movementSpeed;

    /**
     * Initialize new instance of {@link RouteNpcParticipant}
     */
    public RouteNpcParticipant(@NonNull String uuid,
                               @NonNull Point startMapCoordinates,
                               int zIndex,
                               int repeatingMovementDurationTime,
                               @NonNull MovementDirection initialDirection,
                               boolean isRouteLooped,
                               List<RouteMovementDescription> routeDescription) {
        super(
                uuid,
                startMapCoordinates,
                zIndex,
                repeatingMovementDurationTime,
                MovementType.ROUTE_POINTS,
                initialDirection,
                ParticipatType.ROUTE_NPC
        );
        this.isRouteLooped = isRouteLooped;
        this.routeDescription = routeDescription;
        this.movementSpeed = GlobalMovementCalculatorImpl.NORMAL_MOVE_SPEED;
    }
}
