package com.alta.behaviorprocess.service.interaction;

import com.alta.behaviorprocess.shared.scenario.ScenarioImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.*;

/**
 * Provides the model to be used in {@link ScenarioImpl}.
 */
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class InteractionScenarioData {

    private final String targetUuid;
    private Point shiftTileMapCoordinate;

}
