package com.alta.engine.view;

import com.alta.computator.Computator;

/**
 * The factory to create the views.
 */
public interface ViewFactory {

    /**
     * Creates the frame stage view.
     *
     * @return created {@link FrameStageView}.
     */
    FrameStageView createFrameStageView(Computator computator);

}
