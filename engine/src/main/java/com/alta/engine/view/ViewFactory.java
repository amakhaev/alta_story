package com.alta.engine.view;

/**
 * The factory to create the views.
 */
public interface ViewFactory {

    /**
     * Creates the frame stage view.
     *
     * @return created {@link FrameStageView}.
     */
    FrameStageView createFrameStageView();

}
