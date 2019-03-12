package com.alta.mediator.domain.frameStage;

import com.alta.dao.data.preservation.PreservationModel;
import com.alta.engine.entityProvision.entityFactory.FrameStageData;

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

}
