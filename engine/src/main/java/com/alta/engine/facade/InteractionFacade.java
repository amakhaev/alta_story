package com.alta.engine.facade;

import com.alta.behaviorprocess.controller.scenario.ScenarioController;
import com.alta.computator.model.participant.TargetedParticipantSummary;
import com.alta.engine.presenter.FrameStagePresenter;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides the facade to cooperate interaction in engine.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class InteractionFacade {

    private final FrameStagePresenter frameStagePresenter;
    private final ScenarioController scenarioControllerImpl;

    /**
     * Triggers the interaction between acting character and another participant.
     */
    public void triggerInteraction() {
        if (this.scenarioControllerImpl.isScenarioRunning()) {
            this.scenarioControllerImpl.runNextScenarioStep();
            return;
        }

        TargetedParticipantSummary targetedParticipant = this.frameStagePresenter.findParticipantTargetedByActingCharacter();
        if (targetedParticipant == null) {
            log.debug("Participant for interaction not found.");
            return;
        }

        this.scenarioControllerImpl.runScenario(
                targetedParticipant.getUuid(), targetedParticipant.getRelatedMapCoordinates()
        );
    }
}
