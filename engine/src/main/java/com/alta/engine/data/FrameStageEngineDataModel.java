package com.alta.engine.data;

import com.alta.engine.data.frameStage.ActingCharacterEngineModel;
import com.alta.engine.data.frameStage.FacilityEngineModel;
import com.alta.engine.data.frameStage.JumpingEngineModel;
import com.alta.engine.data.frameStage.NpcEngineModel;
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
public class FrameStageEngineDataModel {

    /**
     * The display name of map.
     */
    private final String mapDisplayName;

    /**
     * The name of map.
     */
    private final String mapName;

    /**
     * The absolute path to file that described {@link org.newdawn.slick.tiled.TiledMap} instance
     */
    private final String tiledMapAbsolutePath;

    /**
     * The point that provides coordinates of start position on {@link org.newdawn.slick.tiled.TiledMap}
     */
    private final Point focusPointMapStartPosition;

    /**
     * The data that describes the acting character on stage
     */
    private ActingCharacterEngineModel actingCharacter;

    /**
     * The available facilities on {@link org.newdawn.slick.tiled.TiledMap}
     */
    @Singular
    private List<FacilityEngineModel> facilities;

    /**
     * The available non player characters.
     */
    @Singular("npcList")
    private List<NpcEngineModel> npcList;

    /**
     * The available jumping points on map.
     */
    @Singular("jumpingPoints")
    private List<JumpingEngineModel> jumpingPoints;
}
