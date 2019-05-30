package com.alta.mediator.domain.frameStage;

import com.alta.dao.data.map.MapFacilityModel;
import com.alta.dao.data.map.MapModel;
import com.alta.dao.data.preservation.CharacterPreservationModel;
import com.alta.dao.data.preservation.MapPreservationModel;
import com.alta.dao.domain.map.MapService;
import com.alta.dao.domain.map.internalEntities.AlterableNpcEntity;
import com.alta.dao.domain.map.internalEntities.SimpleNpcEntity;
import com.alta.dao.domain.preservation.PreservationService;
import com.alta.dao.domain.preservation.TemporaryDataPreservationService;
import com.alta.engine.model.FrameStageEngineDataModel;
import com.alta.engine.model.frameStage.ActingCharacterEngineModel;
import com.alta.engine.model.frameStage.FacilityEngineModel;
import com.alta.engine.model.frameStage.NpcEngineModel;
import com.alta.mediator.domain.actor.ActorDataProvider;
import com.alta.mediator.domain.map.FacilityEngineModelMapper;
import com.alta.mediator.domain.map.JumpingEngineModelMapper;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * Provides the service to manipulate model related to {@link FrameStageEngineDataModel}
 */
@Slf4j
public class FrameStageDataProviderImpl implements FrameStageDataProvider {

    private final MapService mapService;
    private final PreservationService preservationService;
    private final TemporaryDataPreservationService temporaryDataPreservationService;
    private final ActorDataProvider actorDataProvider;
    private final FacilityEngineModelMapper facilityEngineModelMapper;
    private final JumpingEngineModelMapper jumpingEngineModelMapper;
    private final Long currentPreservationId;

    /**
     * Initialize new instance of {@link FrameStageDataProviderImpl}.
     */
    @Inject
    public FrameStageDataProviderImpl(MapService mapService,
                                      PreservationService preservationService,
                                      TemporaryDataPreservationService temporaryDataPreservationService,
                                      ActorDataProvider actorDataProvider,
                                      FacilityEngineModelMapper facilityEngineModelMapper,
                                      JumpingEngineModelMapper jumpingEngineModelMapper,
                                      @Named("currentPreservationId") Long currentPreservationId) {
        this.mapService = mapService;
        this.preservationService = preservationService;
        this.temporaryDataPreservationService = temporaryDataPreservationService;
        this.actorDataProvider = actorDataProvider;
        this.facilityEngineModelMapper = facilityEngineModelMapper;
        this.jumpingEngineModelMapper = jumpingEngineModelMapper;
        this.currentPreservationId = currentPreservationId;
    }

    /**
     * Gets the model of frame stage that created from preservation
     *
     * @param characterPreservationModel - the preservation of game
     * @return the {@link FrameStageEngineDataModel} generated from preservation.
     */
    @Override
    public FrameStageEngineDataModel getFromPreservation(CharacterPreservationModel characterPreservationModel) {
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
     * @return the {@link FrameStageEngineDataModel} instance.
     */
    @Override
    public FrameStageEngineDataModel getByParams(String mapName, String skin, Point focus) {
        log.debug(
                "Start getting FrameStageEngineDataModel by params. Map name: {}, skin: {}, focus point: {}.",
                mapName, skin, focus
        );

        if (Strings.isNullOrEmpty(mapName)) {
            log.error("Name of map is null, but required for creating of FrameStageEngineDataModel");
            return null;
        } else  if (Strings.isNullOrEmpty(skin)) {
            log.error("Skin of acting character is null, but required for creating of FrameStageEngineDataModel");
            return null;
        } else if (focus == null) {
            log.error("The focus point of acting character is null, but required for creating of FrameStageEngineDataModel");
            return null;
        }

        MapModel mapModel = this.mapService.getMap(mapName);
        if (mapModel == null) {
            log.error("Map model is null, but required for creating of FrameStageEngineDataModel");
            return null;
        }

        ActingCharacterEngineModel actingCharacterEngineModel = this.actorDataProvider.getActingCharacter(
                skin,
                focus,
                UUID.randomUUID().toString()
        );

        return FrameStageEngineDataModel.builder()
                .mapDisplayName(mapModel.getDisplayName())
                .mapName(mapModel.getName())
                .tiledMapAbsolutePath(mapModel.getTiledMapAbsolutePath())
                .focusPointMapStartPosition(focus)
                .facilities(this.createFacilityList(mapModel.getFacilities(), mapModel.getName()))
                .npcList(this.createNpcList(mapModel.getSimpleNpcList(), mapModel.getAlterableNpcList()))
                .actingCharacter(actingCharacterEngineModel)
                .jumpingPoints(this.jumpingEngineModelMapper.doMappingForJumpings(mapModel.getMapJumpings()))
                .build();
    }

    private List<FacilityEngineModel> createFacilityList(List<MapFacilityModel> facilities, String mapName) {
        if (facilities == null || facilities.isEmpty()) {
            return Collections.emptyList();
        }

        List<FacilityEngineModel> facilityEngineModels = this.facilityEngineModelMapper.doMapppingForFacilities(facilities);

        // The map preservation should be applied to facility if needed,
        List<MapPreservationModel> mapPreservations = this.preservationService.getMapsPreservation(
                this.currentPreservationId, mapName
        );

        this.temporaryDataPreservationService.getMapsPreservation(this.currentPreservationId, mapName)
                .forEach(temporaryMapPreservation -> {
                    mapPreservations.removeIf(mp -> mp.getId().equals(temporaryMapPreservation.getId()));
                    mapPreservations.add(temporaryMapPreservation);
                });

        // Apply preservation to facilities
        mapPreservations.forEach(mapPreservationModel -> {
            facilityEngineModels.stream()
                    .filter(facilityEngineModel -> mapPreservationModel.getParticipantUuid().equals(facilityEngineModel.getUuid()))
                    .findFirst()
                    .ifPresent(facilityEngineModel -> {
                        facilityEngineModel.setVisible(mapPreservationModel.isVisible());
                    });
        });

        return facilityEngineModels;
    }

    private List<NpcEngineModel> createNpcList(List<SimpleNpcEntity> simpleNpcList,
                                               List<AlterableNpcEntity> alterableNpcList) {

        // Simple npc
        List<NpcEngineModel> engineModels = simpleNpcList.stream()
                .map(this.actorDataProvider::getSimpleNpc)
                .collect(Collectors.toList());

        // Alterable npc
        engineModels.addAll(
                alterableNpcList.stream().map(this.actorDataProvider::getAlterableNpc).collect(Collectors.toList())
        );

        return engineModels;
    }
}
