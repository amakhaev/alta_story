package com.alta.behaviorprocess.behaviorAction.interaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.*;

/**
 * Provides the data to be used in {@link com.alta.behaviorprocess.shared.scenario.InteractionScenario}.
 */
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class InteractionScenarioData {

    private final String mapName;
    private final String targetUuid;
    private Point shiftTileMapCoordinate;

}
