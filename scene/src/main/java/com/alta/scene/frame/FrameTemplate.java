package com.alta.scene.frame;

/**
 * Provides the template of frame
 */
public interface FrameTemplate {

    /**
     * Creates the frame from template by given state
     *
     * @param state - the frame state
     * @return the {@link Frame} that created
     */
    Frame createFrameFromState(FrameState state);

}
