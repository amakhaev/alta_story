package com.alta.mediator.domain.frameStage;

import com.alta.dao.data.preservation.CharacterPreservationModel;
import com.alta.engine.model.FrameStageDataModel;

import java.awt.*;

/**
 * Provides the service to manipulate model related to {@link FrameStageDataModel}
 */
public interface FrameStageDataProvider {

    /**
     * Gets the model of frame stage that created from preservation
     *
     * @param characterPreservationModel - the preservation of game
     * @return the {@link FrameStageDataModel} generated from preservation.
     */
    FrameStageDataModel getFromPreservation(CharacterPreservationModel characterPreservationModel);

    /**
     * Gets the model of frame stage that created by give params
     *
     * @param mapName - the name of map to be render
     * @param skin - the skin of acting character
     * @param focus - the coordinates of focus point on tiled map
     * @return the {@link FrameStageDataModel} instance.
     */
    FrameStageDataModel getByParams(String mapName, String skin, Point focus);

}
