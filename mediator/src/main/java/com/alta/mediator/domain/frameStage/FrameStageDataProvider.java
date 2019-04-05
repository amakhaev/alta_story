package com.alta.mediator.domain.frameStage;

import com.alta.dao.data.characterPreservation.CharacterPreservationModel;
import com.alta.engine.utils.dataBuilder.FrameStageData;

import java.awt.*;

/**
 * Provides the service to manipulate model related to {@link FrameStageData}
 */
public interface FrameStageDataProvider {

    /**
     * Gets the model of frame stage that created from characterPreservation
     *
     * @param characterPreservationModel - the characterPreservation of game
     * @return the {@link FrameStageData} generated from characterPreservation.
     */
    FrameStageData getFromPreservation(CharacterPreservationModel characterPreservationModel);

    /**
     * Gets the model of frame stage that created by give params
     *
     * @param mapName - the name of map to be render
     * @param skin - the skin of acting character
     * @param focus - the coordinates of focus point on tiled map
     * @return the {@link FrameStageData} instance.
     */
    FrameStageData getByParams(String mapName, String skin, Point focus);

}
