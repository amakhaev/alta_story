package com.alta.mediator.domain.frameStage;

import com.alta.dao.data.preservation.CharacterPreservationModel;
import com.alta.engine.data.FrameStageEngineDataModel;

import java.awt.*;

/**
 * Provides the service to manipulate model related to {@link FrameStageEngineDataModel}
 */
public interface FrameStageDataProvider {

    /**
     * Gets the model of frame stage that created from preservation
     *
     * @param characterPreservationModel - the preservation of game
     * @return the {@link FrameStageEngineDataModel} generated from preservation.
     */
    FrameStageEngineDataModel getFromPreservation(CharacterPreservationModel characterPreservationModel);

    /**
     * Gets the model of frame stage that created by give params
     *
     * @param mapName - the name of map to be render
     * @param skin - the skin of acting character
     * @param focus - the coordinates of focus point on tiled map
     * @return the {@link FrameStageEngineDataModel} instance.
     */
    FrameStageEngineDataModel getByParams(String mapName, String skin, Point focus);

}
