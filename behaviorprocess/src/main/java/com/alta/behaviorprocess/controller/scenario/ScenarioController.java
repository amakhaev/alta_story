package com.alta.behaviorprocess.controller.scenario;

import java.awt.*;

/**
 * Provides the controller that allow execute scenarios.
 */
public interface ScenarioController {

    /**
     * Indicates when scenario was running.
     *
     * @return true if any scenario was running, false otherwise.
     */
    boolean isScenarioRunning();

    /**
     * Runs the next step in scenario.
     */
    void runNextScenarioStep();

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
    void runScenario(String targetUuid, Point shiftTileMapCoordinate);

}
