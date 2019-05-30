package com.alta.engine.view;

import com.alta.engine.model.FrameStageEngineDataModel;

/**
 * The factory to create the views.
 */
public interface ViewFactory {

    /**
     * Creates the frame stage view.
     *
     * @param frameStageEngineDataModel - the data that used for creating view
     * @return created {@link FrameStageView}.
     */
    FrameStageView createFrameStageView(FrameStageEngineDataModel frameStageEngineDataModel);

}
