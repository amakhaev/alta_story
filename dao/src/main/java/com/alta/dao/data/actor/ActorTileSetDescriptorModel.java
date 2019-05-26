package com.alta.dao.data.actor;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

/**
 * Provides the descriptor of tile sets for actor.
 */
@Getter
@Builder
public class ActorTileSetDescriptorModel {

    private int tileWidth;
    private int tileHeight;

    @Singular("directionDown")
    private List<ActorDirectionModel> directionDown;

    @Singular("directionLeft")
    private List<ActorDirectionModel> directionLeft;

    @Singular("directionRight")
    private List<ActorDirectionModel> directionRight;

    @Singular("directionUp")
    private List<ActorDirectionModel> directionUp;

}
