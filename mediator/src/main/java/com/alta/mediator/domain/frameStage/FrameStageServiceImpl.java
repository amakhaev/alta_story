package com.alta.mediator.domain.frameStage;

import com.alta.dao.data.map.MapModel;
import com.alta.dao.data.preservation.PreservationModel;
import com.alta.dao.domain.map.MapService;
import com.alta.dao.domain.preservation.PreservationService;
import com.alta.engine.entityProvision.FrameStageData;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;


/**
 * Provides the service to manipulate data related to {@link FrameStageData}
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class FrameStageServiceImpl implements FrameStageService {

    private final PreservationService preservationService;
    private final MapService mapService;
    private final FacilityEngineModelMapper facilityEngineModelMapper;

    /**
     * Gets the data of frame stage that created from preservation
     *
     * @return the {@link FrameStageData} generated from preservation.
     */
    @Override
    public FrameStageData getFromPreservation() {
        log.debug("Start getting FrameStageData from preservation. Load preservation.");

        PreservationModel preservationModel = this.preservationService.getPreservation();
        if (preservationModel == null) {
            log.error("Preservation model is null, but required for creating of FrameStageData");
            return null;
        }

        MapModel mapModel = this.mapService.getMap(preservationModel.getMapName());
        if (mapModel == null) {
            log.error("Map model is null, but required for creating of FrameStageData");
            return null;
        }

        return FrameStageData.builder()
                .tiledMapAbsolutePath(mapModel.getTiledMapAbsolutePath())
                .focusPointMapStartPosition(new Point(preservationModel.getFocusX(), preservationModel.getFocusY()))
                .facilities(this.facilityEngineModelMapper.doMapppingForFacilities(mapModel.getFacilities()))
                .build();
    }
}
