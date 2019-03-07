package com.alta.mediator.domain.actor;

import com.alta.computator.service.movement.strategy.MovementDirection;
import com.alta.dao.data.actor.ActorDirectionModel;
import com.alta.dao.data.actor.ActorModel;
import com.alta.engine.data.ActingCharacterModel;
import com.alta.scene.component.actorAnimation.ActorAnimationDescriptor;
import com.google.inject.Singleton;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Provides the data mapper for actors
 */
@Singleton
public class ActorEngineMapper {

    /**
     * Maps the DAO actors model to engine model
     *
     * @param actorModel - the sore actor model
     * @return the {@link ActingCharacterModel} instance.
     */
    public ActingCharacterModel doMappingForActingCharacter(ActorModel actorModel) {
        if (actorModel == null) {
            return null;
        }

        List<ActorAnimationDescriptor<MovementDirection>> actorAnimationDescriptors = Arrays.asList(
                this.doMappingForAnimationDescriptor(actorModel, actorModel.getDescriptor().getDirectionDown(), MovementDirection.DOWN),
                this.doMappingForAnimationDescriptor(actorModel, actorModel.getDescriptor().getDirectionLeft(), MovementDirection.LEFT),
                this.doMappingForAnimationDescriptor(actorModel, actorModel.getDescriptor().getDirectionRight(), MovementDirection.RIGHT),
                this.doMappingForAnimationDescriptor(actorModel, actorModel.getDescriptor().getDirectionUp(), MovementDirection.UP)
        );

        return ActingCharacterModel.builder()
                .uuid(actorModel.getUuid())
                .zIndex(actorModel.getZIndex())
                .startMapCoordinates(actorModel.getStartMapCoordinates())
                .animationDescriptors(actorAnimationDescriptors)
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
                .duration(200)
                .stopFrameIndex(stopFrameIndex)
                .animatedTileCoordinates(positions)
                .build();
    }

}
