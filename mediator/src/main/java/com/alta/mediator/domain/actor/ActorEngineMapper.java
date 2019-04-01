package com.alta.mediator.domain.actor;

import com.alta.computator.service.movement.strategy.MovementDirection;
import com.alta.dao.data.actor.ActorDirectionModel;
import com.alta.dao.data.actor.ActorModel;
import com.alta.engine.model.ActingCharacterEngineModel;
import com.alta.engine.model.SimpleNpcEngineModel;
import com.alta.scene.component.actorAnimation.ActorAnimationDescriptor;
import com.google.inject.Singleton;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Provides the model mapper for actors
 */
@Singleton
class ActorEngineMapper {

    /**
     * Maps the DAO actors model to engine model
     *
     * @param actorModel - the source actor model
     * @return the {@link ActingCharacterEngineModel} instance.
     */
    ActingCharacterEngineModel doMappingForActingCharacter(ActorModel actorModel) {
        if (actorModel == null) {
            return null;
        }

        List<ActorAnimationDescriptor<MovementDirection>> actorAnimationDescriptors = Arrays.asList(
                this.doMappingForAnimationDescriptor(actorModel, actorModel.getDescriptor().getDirectionDown(), MovementDirection.DOWN),
                this.doMappingForAnimationDescriptor(actorModel, actorModel.getDescriptor().getDirectionLeft(), MovementDirection.LEFT),
                this.doMappingForAnimationDescriptor(actorModel, actorModel.getDescriptor().getDirectionRight(), MovementDirection.RIGHT),
                this.doMappingForAnimationDescriptor(actorModel, actorModel.getDescriptor().getDirectionUp(), MovementDirection.UP)
        );

        return ActingCharacterEngineModel.builder()
                .uuid(actorModel.getUuid())
                .zIndex(actorModel.getZIndex())
                .startMapCoordinates(actorModel.getStartMapCoordinates())
                .animationDescriptors(actorAnimationDescriptors)
                .build();
    }

    /**
     * Maps the DAO actors model to engine model
     *
     * @param actorModel - the source actor model
     * @return the {@link SimpleNpcEngineModel} instance.
     */
    SimpleNpcEngineModel doMappingForSimpleNpc(ActorModel actorModel) {
        if (actorModel == null) {
            return null;
        }

        List<ActorAnimationDescriptor<MovementDirection>> actorAnimationDescriptors = Arrays.asList(
                this.doMappingForAnimationDescriptor(actorModel, actorModel.getDescriptor().getDirectionDown(), MovementDirection.DOWN),
                this.doMappingForAnimationDescriptor(actorModel, actorModel.getDescriptor().getDirectionLeft(), MovementDirection.LEFT),
                this.doMappingForAnimationDescriptor(actorModel, actorModel.getDescriptor().getDirectionRight(), MovementDirection.RIGHT),
                this.doMappingForAnimationDescriptor(actorModel, actorModel.getDescriptor().getDirectionUp(), MovementDirection.UP)
        );

        return SimpleNpcEngineModel.builder()
                .uuid(actorModel.getUuid())
                .zIndex(actorModel.getZIndex())
                .startMapCoordinates(actorModel.getStartMapCoordinates())
                .animationDescriptors(actorAnimationDescriptors)
                .repeatingMovementDurationTime(actorModel.getRepeatingMovementDurationTime())
                .build();
    }

    private ActorAnimationDescriptor<MovementDirection> doMappingForAnimationDescriptor(ActorModel actorModel,
                                                                                        List<ActorDirectionModel> directionModels,
                                                                                        MovementDirection direction) {
        List<Point> positions = new ArrayList<>();
        int stopFrameIndex = 0;
        for (int i = 0; i < directionModels.size(); i++) {
            ActorDirectionModel directionModel = directionModels.get(i);
            positions.add(new Point(directionModel.getX(), directionModel.getY()));
            if (directionModel.isStopFrame()) {
                stopFrameIndex = i;
            }
        }

        return ActorAnimationDescriptor.<MovementDirection>builder()
                .pathToSpriteSheet(actorModel.getPathToImageSet())
                .tileWidth(actorModel.getDescriptor().getTileWidth())
                .tileHeight(actorModel.getDescriptor().getTileHeight())
                .identifier(direction)
                .duration(actorModel.getDurationTime())
                .stopFrameIndex(stopFrameIndex)
                .animatedTileCoordinates(positions)
                .build();
    }

}
