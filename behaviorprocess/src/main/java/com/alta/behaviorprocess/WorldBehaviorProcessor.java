package com.alta.behaviorprocess;

import com.alta.behaviorprocess.behaviorAction.Behavior;
import com.alta.behaviorprocess.behaviorAction.interaction.InteractionScenarioData;
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

    private Scenario currentScenario;

    /**
     * Initialize new instance of {@link WorldBehaviorProcessor}.
     *
     * @param interactionBehavior - the {@link Behavior} instance.
     */
    @Inject
    public WorldBehaviorProcessor(@Named("interactionBehavior") Behavior<InteractionScenarioData> interactionBehavior) {
        this.interactionBehavior = interactionBehavior;
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
     * 2. Try to get scenario for quest.
     * 3. Try to get scenario for interaction.
     *
     * @param targetUuid                - the uuid of target.
     * @param shiftTileMapCoordinate    - the coordinate of target tile.
     */
    public synchronized void runProcessing(String targetUuid, Point shiftTileMapCoordinate) {
        if (this.isProcessRunning()) {
            log.warn("One of scenarios already running. Attempt ignored to run process for target {}", targetUuid);
            return;
        }

        this.currentScenario = this.interactionBehavior.getScenario(
                new InteractionScenarioData(targetUuid, shiftTileMapCoordinate)
        );

        if (this.currentScenario != null) {
            this.currentScenario.execute();
        }
    }
}
