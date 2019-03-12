package com.alta.mediator.domain.frameStage;

import com.alta.dao.data.map.MapModel;
import com.alta.dao.data.preservation.PreservationModel;
import com.alta.dao.domain.map.MapService;
import com.alta.engine.data.ActingCharacterEngineModel;
import com.alta.engine.data.SimpleNpcEngineModel;
import com.alta.engine.entityProvision.entityFactory.FrameStageData;
import com.alta.mediator.domain.actor.ActorDataProvider;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Provides the service to manipulate data related to {@link FrameStageData}
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class FrameStageDataProviderImpl implements FrameStageDataProvider {

    private final MapService mapService;
    private final ActorDataProvider actorDataProvider;
    private final FacilityEngineModelMapper facilityEngineModelMapper;

    /**
     * Gets the data of frame stage that created from preservation
     *
     * @param preservationModel - the preservation of game
     * @return the {@link FrameStageData} generated from preservation.
     */
    @Override
    public FrameStageData getFromPreservation(PreservationModel preservationModel) {
        log.debug("Start getting FrameStageData from preservation. Load preservation.");

        if (preservationModel == null) {
            log.error("Preservation model is null, but required for creating of FrameStageData");
            return null;
        }

        MapModel mapModel = this.mapService.getMap(preservationModel.getMapName());
        if (mapModel == null) {
            log.error("Map model is null, but required for creating of FrameStageData");
            return null;
        }

        ActingCharacterEngineModel actingCharacterEngineModel = this.actorDataProvider.getActingCharacter(
                preservationModel.getMainCharaterSkin(),
                new Point(preservationModel.getFocusX(), preservationModel.getFocusY())
        );

        List<SimpleNpcEngineModel> simpleNpcEngineModels = mapModel.getSimpleNpcList().stream()
                .map(simpleNpc ->
                        this.actorDataProvider.getSimpleNpc(
                                simpleNpc.getName(),
                                new Point(simpleNpc.getStartX(), simpleNpc.getStartY())
                        )
                ).collect(Collectors.toList());

        return FrameStageData.builder()
                .tiledMapAbsolutePath(mapModel.getTiledMapAbsolutePath())
                .focusPointMapStartPosition(new Point(preservationModel.getFocusX(), preservationModel.getFocusY()))
                .facilities(this.facilityEngineModelMapper.doMapppingForFacilities(mapModel.getFacilities()))
                .simpleNpc(simpleNpcEngineModels)
                .actingCharacter(actingCharacterEngineModel)
                .build();
    }
}
