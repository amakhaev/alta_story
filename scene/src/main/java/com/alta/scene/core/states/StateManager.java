package com.alta.scene.core.states;

import com.google.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides the manager that is manipulating of states.
 */
@Slf4j
public class StateManager {

    private static final byte COUNT_OF_FRAME_STAGE_STATE = 2;

    @Getter
    private List<FrameStageState> frameStageStateStates;

    @Getter
    private FrameStageState selectedFrameStageState;

    /**
     * Initialize new instance of {@link StateManager}
     */
    @Inject
    public StateManager() {
        this.frameStageStateStates = new ArrayList<>();
        this.createFrameStageStates();
    }

    /**
     * Gets the free frame stage state
     */
    public FrameStageState getFreeFrameStageState() {
        return this.frameStageStateStates.stream()
                .filter(frameStageState -> frameStageState.getID() != this.selectedFrameStageState.getID())
                .findFirst()
                .orElse(null);
    }

    /**
     * Sets the selected frame stage state
     *
     * @param id - the id of state.
     */
    public void setSelectedFrameStageState(int id) {
        this.frameStageStateStates.stream()
                .filter(frameStageState -> frameStageState.getID() == id)
                .findFirst()
                .ifPresent(state -> this.selectedFrameStageState = state);
    }

    /**
     * Creates the states for frame stages.
     *
     * @return created {@link List} of frame stage.
     */
    private List<FrameStageState> createFrameStageStates() {
        for (int i = 0; i < COUNT_OF_FRAME_STAGE_STATE; i++) {
            byte frameId = 100;
            frameId += i;
            this.frameStageStateStates.add(new FrameStageState(frameId));
        }

        this.selectedFrameStageState = this.frameStageStateStates.get(0);
        log.info("The frame stages were created with count {}", this.frameStageStateStates.size());
        return this.frameStageStateStates;
    }
}
