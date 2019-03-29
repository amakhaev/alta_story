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

    private String displayName;
    private List<MapFacilityEntity> facilities;
    private List<SimpleNpcEntity> simpleNpcList;
    private List<MapJumpingEntity> jumping;
}
