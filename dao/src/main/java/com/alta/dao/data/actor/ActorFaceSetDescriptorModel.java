package com.alta.dao.data.actor;

import lombok.Builder;
import lombok.Getter;

import java.awt.*;
import java.util.Map;

/**
 * Provides the descriptor of face sets for actor.
 */
@Getter
@Builder
public class ActorFaceSetDescriptorModel {

    private int tileWidth;
    private int tileHeight;
    private Map<String, Point> emotions;

}
