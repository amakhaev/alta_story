package com.alta.mediator.domain.frameStage;

import com.alta.dao.data.map.MapModel;
import com.alta.dao.data.characterPreservation.CharacterPreservationModel;
import com.alta.dao.domain.map.MapService;
import com.alta.engine.model.ActingCharacterEngineModel;
import com.alta.engine.model.SimpleNpcEngineModel;
import com.alta.engine.utils.dataBuilder.FrameStageData;
import com.alta.mediator.domain.actor.ActorDataProvider;
import com.alta.mediator.domain.map.FacilityEngineModelMapper;
import com.alta.mediator.domain.map.JumpingEngineModelMapper;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Provides the service to manipulate model related to {@link FrameStageData}
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class FrameStageDataProviderImpl implements FrameStageDataProvider {

    private final MapService mapService;
    private final ActorDataProvider actorDataProvider;
    private final FacilityEngineModelMapper facilityEngineModelMapper;
    private final JumpingEngineModelMapper jumpingEngineModelMapper;

    /**
     * Gets the model of frame stage that created from characterPreservation
     *
     * @param characterPreservationModel - the characterPreservation of game
     * @return the {@link FrameStageData} generated from characterPreservation.
     */
    @Override
    public FrameStageData getFromPreservation(CharacterPreservationModel characterPreservationModel) {
        return this.getByParams(
                characterPreservationModel.getMapName(),
                characterPreservationModel.getMainCharaterSkin(),
                new Point(characterPreservationModel.getFocusX(), characterPreservationModel.getFocusY())
        );
    }

    /**
     * Gets the model of frame stage that created by give params
     *
     * @param mapName - the name of map to be render
     * @param skin    - the skin of acting character
     * @param focus   - the coordinates of focus point on tiled map
     * @return the {@link FrameStageData} instance.
     */
    @Override
    public FrameStageData getByParams(String mapName, String skin, Point focus) {
        log.debug(
                "Start getting FrameStageData by params. Map name: {}, skin: {}, focus point: {}.",
                mapName, skin, focus
        );

        if (Strings.isNullOrEmpty(mapName)) {
            log.error("Name of map is null, but required for creating of FrameStageData");
            return null;
        } else  if (Strings.isNullOrEmpty(skin)) {
            log.error("Skin of acting character is null, but required for creating of FrameStageData");
            return null;
        } else if (focus == null) {
            log.error("The focus point of acting character is null, but required for creating of FrameStageData");
            return null;
        }

        MapModel mapModel = this.mapService.getMap(mapName);
        if (mapModel == null) {
            log.error("Map model is null, but required for creating of FrameStageData");
            return null;
        }

        ActingCharacterEngineModel actingCharacterEngineModel = this.actorDataProvider.getActingCharacter(
                skin,
                focus
        );

        List<SimpleNpcEngineModel> simpleNpcEngineModels = mapModel.getSimpleNpcList().stream()
                .map(simpleNpc ->
                        this.actorDataProvider.getSimpleNpc(
                                simpleNpc.getName(),
                                new Point(simpleNpc.getStartX(), simpleNpc.getStartY()),
                                simpleNpc.getRepeatingMovementDurationTime(),
                                simpleNpc.getDialogue()
                        )
                ).collect(Collectors.toList());

        return FrameStageData.builder()
                .mapDisplayName(mapModel.getDisplayName())
                .mapName(mapModel.getName())
                .tiledMapAbsolutePath(mapModel.getTiledMapAbsolutePath())
                .focusPointMapStartPosition(focus)
                .facilities(this.facilityEngineModelMapper.doMapppingForFacilities(mapModel.getFacilities()))
                .simpleNpc(simpleNpcEngineModels)
                .actingCharacter(actingCharacterEngineModel)
                .jumpingPoints(this.jumpingEngineModelMapper.doMappingForJumpings(mapModel.getMapJumpings()))
                .build();
    }
}
