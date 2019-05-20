package com.alta.dao.domain.map;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.List;

/**
 * Provides the entity that contains data movement rules.
 */
@Getter
@Setter
public class MovementRulesEntity {

    private boolean looped;
    private List<Point> points;

}
