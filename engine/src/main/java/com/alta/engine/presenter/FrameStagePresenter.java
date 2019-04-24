package com.alta.engine.presenter;

import com.alta.computator.model.participant.TargetedParticipantSummary;
import com.alta.computator.service.movement.strategy.MovementDirection;
import com.alta.engine.model.FrameStageDataModel;
import com.alta.engine.presenter.sceneProxy.SceneProxy;
import com.alta.engine.view.FrameStageView;
import com.alta.engine.view.ViewFactory;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Provides the presenter of view.
 */
@Slf4j
public class FrameStagePresenter {

    private final SceneProxy sceneProxy;
    private final ViewFactory viewFactory;

    private FrameStageView currentView;

    /**
     * Initialize new instance of {@link FrameStagePresenter}
     */
    @Inject
    public FrameStagePresenter(SceneProxy sceneProxy, ViewFactory viewFactory) {

        this.sceneProxy = sceneProxy;
        this.viewFactory = viewFactory;
        this.sceneProxy.setStateListener(this::onSceneFocusChanged);
    }

    /**
     * Loads scene state from preservation
     */
    public void tryToRenderFrameStageView(FrameStageDataModel data) {
        this.currentView = this.viewFactory.createFrameStageView(data);
        this.sceneProxy.renderFrameStage(this.currentView.getFrameStage());
    }

    /**
     * Start the rendering of scene
     */
    public void startScene() {
        this.sceneProxy.sceneStart();
    }

    /**
     * Performs the movement on scene
     *
     * @param movementDirection - the movement that should be performed
     */
    public void movementPerform(MovementDirection movementDirection) {
        if (this.currentView != null) {
            this.currentView.onMovementPerform(movementDirection);
        }
    }

    /**
     * Gets the map coordinates of acting character.
     */
    public Point getActingCharacterMapCoordinate() {
        return this.currentView == null ? null : this.currentView.getActingCharacterMapCoordinate();
    }

    /**
     * Finds the uuid of participant that targeted by acting character.
     *
     * @return the {@link TargetedParticipantSummary} instance or null if not found.
     */
    public TargetedParticipantSummary findParticipantTargetedByActingCharacter() {
        return this.currentView == null ? null : this.currentView.findParticipantTargetedByActingCharacter();
    }

    /**
     * Starts the interaction with NPC.
     *
     * @param uuid - the UUID of simple NPC to be interacted.
     */
    public void startInteractionWithNpc(String uuid) {
        if (Strings.isNullOrEmpty(uuid)) {
            return;
        }

        if (this.currentView != null) {
            this.currentView.setPauseComputationForSimpleNpc(true, uuid);
            this.currentView.setNpcDirectionTargetedToActingCharacter(uuid);
        }
    }

    /**
     * Stops the interaction with simple NPC.
     *
     * @param uuid - the UUID of simple NPC to be interacted.
     */
    public void stopInteractionWithNpc(String uuid) {
        if (Strings.isNullOrEmpty(uuid)) {
            return;
        }

        if (this.currentView != null) {
            this.currentView.setPauseComputationForSimpleNpc(false, uuid);
        }
    }

    /**
     * Adds the facility to map.
     *
     * @param facilityUuid - the uuid of facility to be added.
     */
    public void addFacility(String facilityUuid) {
        if (this.currentView != null) {
            this.currentView.addFacility(facilityUuid);
        }
    }

    /**
     * Removes the facility from frame stage and computator.
     *
     * @param facilityUuid - the uuid of facility to be removed.
     */
    public void removeFacility(String facilityUuid) {
        if (this.currentView != null) {
            this.currentView.removeFacility(facilityUuid);
        }
    }

    private void onSceneFocusChanged(boolean sceneHasFocus) {
        if (this.currentView == null) {
            return;
        }
        this.currentView.setPauseComputation(!sceneHasFocus);
    }
}
