package com.alta.dao.domain.map.internalEntities;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the entity that contains raw information about map
 */
@Getter
@Setter
public class MapEntity {

    private String name;
    private String tiledMapPath;
    private String decoratorPath;

}
