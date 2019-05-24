package com.alta.dao.domain.actor;

import com.alta.dao.data.actor.ActorDirectionModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Provides the descriptor of tile sets for actor.
 */
@Setter
@Getter
public class TileSetDescriptorEntity {

    private int tileWidth;
    private int tileHeight;
    private List<ActorDirectionModel> directionDown;
    private List<ActorDirectionModel> directionLeft;
    private List<ActorDirectionModel> directionRight;
    private List<ActorDirectionModel> directionUp;

}
