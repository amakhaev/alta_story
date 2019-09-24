package com.alta.mediator.domain.frameStage;

import com.alta.dao.data.map.MapFacilityModel;
import com.alta.dao.data.map.MapModel;
import com.alta.dao.data.preservation.MapPreservationModel;
import com.alta.dao.data.preservation.PreservationModel;
import com.alta.dao.domain.map.MapService;
import com.alta.dao.domain.map.internalEntities.AlterableNpcEntity;
import com.alta.dao.domain.map.internalEntities.SimpleNpcEntity;
import com.alta.dao.domain.preservation.PreservationService;
import com.alta.engine.data.FrameStageEngineDataModel;
import com.alta.engine.data.frameStage.ActingCharacterEngineModel;
import com.alta.engine.data.frameStage.FacilityEngineModel;
import com.alta.engine.data.frameStage.NpcEngineModel;
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
    private final ActorDataProvider actorDataProvider;
    private final FacilityEngineModelMapper facilityEngineModelMapper;
    private final JumpingEngineModelMapper jumpingEngineModelMapper;
    private final Long currentPreservationId;
    private final PreservationService preservationService;

    /**
     * Initialize new instance of {@link FrameStageDataProviderImpl}.
     */
    @Inject
    public FrameStageDataProviderImpl(MapService mapService,
                                      ActorDataProvider actorDataProvider,
                                      FacilityEngineModelMapper facilityEngineModelMapper,
                                      JumpingEngineModelMapper jumpingEngineModelMapper,
                                      @Named("currentPreservationId") Long currentPreservationId,
                                      PreservationService preservationService) {
        this.mapService = mapService;
        this.actorDataProvider = actorDataProvider;
        this.facilityEngineModelMapper = facilityEngineModelMapper;
        this.jumpingEngineModelMapper = jumpingEngineModelMapper;
        this.currentPreservationId = currentPreservationId;
        this.preservationService = preservationService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FrameStageEngineDataModel getFromPreservation(PreservationModel preservationModel) {
        return this.getByParams(
                preservationModel.getActingCharacter().getMapName(),
                preservationModel.getActingCharacter().getSkin(),
                new Point(preservationModel.getActingCharacter().getFocusX(), preservationModel.getActingCharacter().getFocusY())
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
        List<MapPreservationModel> mapPreservation = this.preservationService.getMaps(
                this.currentPreservationId.intValue(), mapName
        );

        // Apply preservation to facilities
        mapPreservation.forEach(mapPreservationModel -> {
            facilityEngineModels.stream()
                    .filter(facilityEngineModel -> mapPreservationModel.getParticipantUuid().toString().equals(facilityEngineModel.getUuid()))
                    .findFirst()
                    .ifPresent(facilityEngineModel -> {
                        facilityEngineModel.setVisible(mapPreservationModel.isVisible());
                    });
        });

        return facilityEngineModels;
    }

    private List<NpcEngineModel> createNpcList(List<SimpleNpcEntity> simpleNpcList,
                                               List<AlterableNpcEntity> alterableNpcList) {

        // Simple npcMovement
        List<NpcEngineModel> engineModels = simpleNpcList.stream()
                .map(this.actorDataProvider::getSimpleNpc)
                .collect(Collectors.toList());

        // Alterable npcMovement
        engineModels.addAll(
                alterableNpcList.stream().map(this.actorDataProvider::getAlterableNpc).collect(Collectors.toList())
        );

        return engineModels;
    }
}
