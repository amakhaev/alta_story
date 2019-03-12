package com.alta.dao.domain.map;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the entity that describes the simple NPC
 */
@Getter
@Setter
public class SimpleNpcEntity {

    private String name;
    private int startX;
    private int startY;
    private int repeatingMovementDurationTime;

}
