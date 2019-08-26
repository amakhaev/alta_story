package com.alta.behaviorprocess.controller.scenario;

import com.alta.behaviorprocess.service.Behavior;
import com.alta.behaviorprocess.service.interaction.InteractionScenarioData;
import com.alta.behaviorprocess.service.quest.QuestScenarioData;
import com.alta.behaviorprocess.shared.scenario.Scenario;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.awt.*;

/**
 * Provides the controller that allow execute scenarios.
 */
@Slf4j
public class ScenarioControllerImpl implements ScenarioController {

    private final Behavior<InteractionScenarioData> interactionBehavior;
    private final Behavior<QuestScenarioData> mainQuestBehavior;

    private Scenario currentScenario;

    /**
     * Initialize new instance of {@link ScenarioControllerImpl}.
     *
     * @param interactionBehavior - the {@link Behavior} instance related to interactions.
     * @param mainQuestBehavior     - the {@link Behavior} instance related to main quest.
     */
    @Inject
    public ScenarioControllerImpl(@Named("interactionBehavior") Behavior<InteractionScenarioData> interactionBehavior,
                                  @Named("mainQuestBehavior") Behavior<QuestScenarioData> mainQuestBehavior) {
        this.interactionBehavior = interactionBehavior;
        this.mainQuestBehavior = mainQuestBehavior;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isScenarioRunning() {
        return this.currentScenario != null && !this.currentScenario.isCompleted();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runNextScenarioStep() {
        if (this.isScenarioRunning()) {
            this.currentScenario.runNextEffect();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void runScenario(String targetUuid, Point shiftTileMapCoordinate) {

        // 1. If any scenario in progress then just ignore this call.
        if (this.isScenarioRunning()) {
            log.warn("One of scenarios already running. Attempt ignored to run process for target {}", targetUuid);
            return;
        }

        // 2. Try to get scenario for main quest.
        this.currentScenario = this.mainQuestBehavior.getScenario(new QuestScenarioData(targetUuid));

        if (this.currentScenario == null) {
            // 4. Try to get scenario for interaction.
            this.currentScenario = this.interactionBehavior.getScenario(
                    new InteractionScenarioData(targetUuid, shiftTileMapCoordinate)
            );
        }

        if (this.currentScenario != null) {
            this.currentScenario.execute();
        }
    }
}
