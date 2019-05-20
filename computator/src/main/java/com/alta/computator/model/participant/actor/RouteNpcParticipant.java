package com.alta.computator.model.participant.actor;

import com.alta.computator.model.participant.ParticipatType;
import com.alta.computator.service.movement.MovementType;
import com.alta.computator.service.movement.directionCalculation.MovementDirection;
import lombok.Getter;
import lombok.NonNull;

import java.awt.*;
import java.util.List;

/**
 * Provides the participant that describes the route NPC
 */
public class RouteNpcParticipant extends NpcParticipant {

    @Getter
    private final boolean isRouteLooped;

    @Getter
    private final List<Point> points;

    /**
     * Initialize new instance of {@link RouteNpcParticipant}
     */
    public RouteNpcParticipant(@NonNull String uuid,
                               @NonNull Point startMapCoordinates,
                               int zIndex,
                               int repeatingMovementDurationTime,
                               @NonNull MovementDirection initialDirection,
                               boolean isRouteLooped,
                               List<Point> points) {
        super(
                uuid,
                startMapCoordinates,
                zIndex,
                repeatingMovementDurationTime,
                MovementType.ROUTE_POINTS,
                initialDirection, ParticipatType.ROUTE_NPC
        );
        this.isRouteLooped = isRouteLooped;
        this.points = points;
    }
}
