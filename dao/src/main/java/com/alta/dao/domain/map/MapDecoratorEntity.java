package com.alta.dao.domain.map;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Provides the internal decorator of map
 */
@Getter
@Setter
class MapDecoratorEntity {
    private List<MapFacilityEntity> facilities;
}
