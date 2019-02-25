package com.alta.dao.domain.map;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the entity that contains raw information about map
 */
@Getter
@Setter
class MapEntity {

    private String name;
    private String tiledMapPath;
    private String decoratorPath;

}
