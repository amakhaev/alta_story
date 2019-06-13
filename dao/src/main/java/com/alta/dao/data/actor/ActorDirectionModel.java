package com.alta.dao.data.actor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Provides the data that describes the direction of actors
 */
@Getter
@AllArgsConstructor
public class ActorDirectionModel {

    private int x;
    private int y;
    private boolean stopFrame;

}
