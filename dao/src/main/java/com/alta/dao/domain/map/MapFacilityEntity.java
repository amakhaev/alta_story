package com.alta.dao.domain.map;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the map that related to map
 */
@Getter
@Setter
class MapFacilityEntity {

    private String uuid;
    private String name;
    private String descriptorFileName;
    private int startX;
    private int startY;

}
