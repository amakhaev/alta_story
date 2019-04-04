package com.alta.engine.view;

import com.alta.engine.utils.dataBuilder.FrameStageData;

/**
 * The factory to create the views.
 */
public interface ViewFactory {

    /**
     * Creates the frame stage view.
     *
     * @param frameStageData - the data that used for creating view
     * @return created {@link FrameStageView}.
     */
    FrameStageView createFrameStageView(FrameStageData frameStageData);

}
