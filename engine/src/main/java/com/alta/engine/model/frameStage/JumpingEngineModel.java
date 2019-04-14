package com.alta.engine.model.frameStage;

import lombok.Builder;
import lombok.Getter;

import java.awt.*;

/**
 * Provides the engine model to work with jumping
 */
@Builder
@Getter
public class JumpingEngineModel {

    private final Point from;
    private final Point to;
    private final String mapName;
}
