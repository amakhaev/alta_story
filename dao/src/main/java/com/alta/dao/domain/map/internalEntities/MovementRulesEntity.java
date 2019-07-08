package com.alta.dao.domain.map.internalEntities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Provides the entity that contains model movement rules.
 */
@Getter
@Setter
public class MovementRulesEntity {

    private boolean looped;
    private List<NpcRouteDescriptionEntity> routeDescription;

}
