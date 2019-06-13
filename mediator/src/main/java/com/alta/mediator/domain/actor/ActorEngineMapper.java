package com.alta.mediator.domain.actor;

import com.alta.computator.service.movement.directionCalculation.MovementDirection;
import com.alta.dao.data.actor.ActorDirectionModel;
import com.alta.dao.data.actor.ActorModel;
import com.alta.engine.data.frameStage.ActingCharacterEngineModel;
import com.alta.engine.data.frameStage.NpcEngineModel;
import com.alta.scene.component.actorAnimation.ActorAnimationDescriptor;
import com.google.common.base.Strings;
import com.google.inject.Singleton;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Provides the data mapper for actors
 */
@Singleton
class ActorEngineMapper {

    /**
     * Maps the DAO actors data to engine data
     *
     * @param actorModel - the source actor data
     * @return the {@link ActingCharacterEngineModel} instance.
     */
    ActingCharacterEngineModel doMappingForActingCharacter(ActorModel actorModel, String skinName) {
        if (actorModel == null) {
            return null;
        }

        List<ActorAnimationDescriptor<MovementDirection>> actorAnimationDescriptors = Arrays.asList(
                this.doMappingForAnimationDescriptor(actorModel, actorModel.getTileSetDescriptor().getDirectionDown(), MovementDirection.DOWN),
                this.doMappingForAnimationDescriptor(actorModel, actorModel.getTileSetDescriptor().getDirectionLeft(), MovementDirection.LEFT),
                this.doMappingForAnimationDescriptor(actorModel, actorModel.getTileSetDescriptor().getDirectionRight(), MovementDirection.RIGHT),
                this.doMappingForAnimationDescriptor(actorModel, actorModel.getTileSetDescriptor().getDirectionUp(), MovementDirection.UP)
        );

        return ActingCharacterEngineModel.builder()
                .uuid(actorModel.getUuid())
                .zIndex(actorModel.getZIndex())
                .startMapCoordinates(actorModel.getStartMapCoordinates())
                .animationDescriptors(actorAnimationDescriptors)
                .skinName(skinName)
                .build();
    }

    /**
     * Maps the DAO actors data to engine data
     *
     * @param actorModel - the source actor data
     * @return the {@link NpcEngineModel} instance.
     */
    NpcEngineModel doMappingForSimpleNpc(ActorModel actorModel) {
        if (actorModel == null) {
            return null;
        }

        List<ActorAnimationDescriptor<MovementDirection>> actorAnimationDescriptors = Arrays.asList(
                this.doMappingForAnimationDescriptor(actorModel, actorModel.getTileSetDescriptor().getDirectionDown(), MovementDirection.DOWN),
                this.doMappingForAnimationDescriptor(actorModel, actorModel.getTileSetDescriptor().getDirectionLeft(), MovementDirection.LEFT),
                this.doMappingForAnimationDescriptor(actorModel, actorModel.getTileSetDescriptor().getDirectionRight(), MovementDirection.RIGHT),
                this.doMappingForAnimationDescriptor(actorModel, actorModel.getTileSetDescriptor().getDirectionUp(), MovementDirection.UP)
        );

        return NpcEngineModel.builder()
                .uuid(actorModel.getUuid())
                .zIndex(actorModel.getZIndex())
                .startMapCoordinates(actorModel.getStartMapCoordinates())
                .animationDescriptors(actorAnimationDescriptors)
                .repeatingMovementDurationTime(actorModel.getRepeatingMovementDurationTime())
                .faceSetDescriptor(this.doMappingForFaceSetDescriptor(actorModel))
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
                .pathToSpriteSheet(actorModel.getPathToTileSetImage())
                .tileWidth(actorModel.getTileSetDescriptor().getTileWidth())
                .tileHeight(actorModel.getTileSetDescriptor().getTileHeight())
                .identifier(direction)
                .duration(actorModel.getDurationTime())
                .stopFrameIndex(stopFrameIndex)
                .animatedTileCoordinates(positions)
                .build();
    }

    private NpcEngineModel.FaceSetDescription doMappingForFaceSetDescriptor(ActorModel actorModel) {
        if (Strings.isNullOrEmpty(actorModel.getPathToFaceSetImage()) || actorModel.getFaceSetDescriptor() == null) {
            return null;
        }

        return new NpcEngineModel.FaceSetDescription(
                actorModel.getFaceSetDescriptor().getTileWidth(),
                actorModel.getFaceSetDescriptor().getTileHeight(),
                actorModel.getFaceSetDescriptor().getEmotions(),
                actorModel.getPathToFaceSetImage()
        );
    }

}
