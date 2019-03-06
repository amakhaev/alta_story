package com.alta.engine.entityProvision;

import com.alta.engine.data.FacilityEngineModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.awt.*;
import java.util.List;

/**
 * Provides the builder that builds data for creating scene
 */
@Getter
@Builder(toBuilder = true)
public class FrameStageData {

    /**
     * The absolute path to file that described {@link org.newdawn.slick.tiled.TiledMap} instance
     */
    private final String tiledMapAbsolutePath;

    /**
     * The point that provides coordinates of start position on {@link org.newdawn.slick.tiled.TiledMap}
     */
    private final Point focusPointMapStartPosition;

    /**
     * The available facilities on {@link org.newdawn.slick.tiled.TiledMap}
     */
    @Singular
    private List<FacilityEngineModel> facilities;

}
