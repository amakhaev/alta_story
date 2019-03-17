package com.alta.mediator.domain.frameStage;

import com.alta.dao.data.preservation.PreservationModel;
import com.alta.engine.processing.dataBuilder.FrameStageData;

import java.awt.*;

/**
 * Provides the service to manipulate data related to {@link FrameStageData}
 */
public interface FrameStageDataProvider {

    /**
     * Gets the data of frame stage that created from preservation
     *
     * @param preservationModel - the preservation of game
     * @return the {@link FrameStageData} generated from preservation.
     */
    FrameStageData getFromPreservation(PreservationModel preservationModel);

    /**
     * Gets the data of frame stage that created by give params
     *
     * @param mapName - the name of map to be render
     * @param skin - the skin of acting character
     * @param focus - the coordinates of focus point on tiled map
     * @return the {@link FrameStageData} instance.
     */
    FrameStageData getByParams(String mapName, String skin, Point focus);

}
