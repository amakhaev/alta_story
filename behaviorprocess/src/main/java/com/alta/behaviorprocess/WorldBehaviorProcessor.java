package com.alta.behaviorprocess;

import com.alta.behaviorprocess.service.Behavior;
import com.alta.behaviorprocess.service.interaction.InteractionScenarioData;
import com.alta.behaviorprocess.service.quest.QuestScenarioData;
import com.alta.behaviorprocess.shared.scenario.Scenario;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.awt.*;

/**
 * Provides the behavior processor for game world.
 */
@Slf4j
public class WorldBehaviorProcessor {

    private final Behavior<InteractionScenarioData> interactionBehavior;
    private final Behavior<QuestScenarioData> mainQuestBehavior;

    private Scenario currentScenario;

    /**
     * Initialize new instance of {@link WorldBehaviorProcessor}.
     *
     * @param interactionBehavior - the {@link Behavior} instance related to interactions.
     * @param mainQuestBehavior     - the {@link Behavior} instance related to main quest.
     */
    @Inject
    public WorldBehaviorProcessor(@Named("interactionBehavior") Behavior<InteractionScenarioData> interactionBehavior,
                                  @Named("mainQuestBehavior") Behavior<QuestScenarioData> mainQuestBehavior) {
        this.interactionBehavior = interactionBehavior;
        this.mainQuestBehavior = mainQuestBehavior;
    }

    /**
     * Indicates when process already running.
     *
     * @return true if any process already running, false otherwise.
     */
    public boolean isProcessRunning() {
        return this.currentScenario != null && !this.currentScenario.isCompleted();
    }

    /**
     * Runs the next step in scenario.
     */
    public void runNextStep() {
        if (this.isProcessRunning()) {
            this.currentScenario.runNextEffect();
        }
    }

    /**
     * Runs the process execution. Use rules for detect which exactly process (scenario) should be executed.
     * 1. If any scenario in progress then just ignore this call.
     * 2. Try to get scenario for main quest.
     * 3. Try to get scenario for secondary quest.
     * 4. Try to get scenario for interaction.
     *
     * @param targetUuid                - the uuid of target.
     * @param shiftTileMapCoordinate    - the coordinate of target tile.
     */
    public synchronized void runProcessing(String targetUuid, Point shiftTileMapCoordinate) {

        // 1. If any scenario in progress then just ignore this call.
        if (this.isProcessRunning()) {
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
