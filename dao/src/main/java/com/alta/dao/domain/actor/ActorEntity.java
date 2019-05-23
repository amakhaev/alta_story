package com.alta.dao.domain.actor;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

/**
 * Provides the entity that describes the actor
 */
@Getter
@Setter
public class ActorEntity {

    private String name;
    private String imageName;
    private int zIndex;
    private int durationTime;
}
