package com.alta.engine.view;

import com.alta.engine.model.FrameStageDataModel;

/**
 * The factory to create the views.
 */
public interface ViewFactory {

    /**
     * Creates the frame stage view.
     *
     * @param frameStageDataModel - the data that used for creating view
     * @return created {@link FrameStageView}.
     */
    FrameStageView createFrameStageView(FrameStageDataModel frameStageDataModel);

}
