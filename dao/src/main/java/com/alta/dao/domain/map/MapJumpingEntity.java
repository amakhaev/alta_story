package com.alta.dao.domain.map;

import lombok.Builder;
import lombok.Getter;

/**
 * Provides the jumping point on map
 */
@Getter
@Builder
public class MapJumpingEntity {

    private final int fromX;
    private final int fromY;
    private final int toX;
    private final int toY;
    private final String mapName;

}
