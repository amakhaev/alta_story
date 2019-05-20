package com.alta.dao.domain.map.internalEntities;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the entity that describes the NPC
 */
@Getter
@Setter
public class NpcEntity {

    private String uuid;
    private String name;
    private int startX;
    private int startY;
    private int repeatingMovementDurationTime;
    private String movementSpeed;
    private boolean animatedAlways;
    private String movementStrategy;
    private String initialDirection;

}
