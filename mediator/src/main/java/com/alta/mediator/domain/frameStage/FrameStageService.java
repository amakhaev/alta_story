package com.alta.mediator.domain.frameStage;

import com.alta.engine.entityProvision.FrameStageData;

/**
 * Provides the service to manipulate data related to {@link FrameStageData}
 */
public interface FrameStageService {

    /**
     * Gets the data of frame stage that created from preservation
     *
     * @return the {@link FrameStageData} generated from preservation.
     */
    FrameStageData getFromPreservation();

}
