package com.alta.dao.domain.map.internalEntities;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the description of route for NPC.
 */
@Getter
@Setter
public class NpcRouteDescriptionEntity {

    private int x;
    private int y;
    private String finalDirection;

}
