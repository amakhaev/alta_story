package com.alta.dao.domain.map.internalEntities;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the map that related to map
 */
@Getter
@Setter
public class MapFacilityEntity {

    private String uuid;
    private String name;
    private String descriptorFileName;
    private int startX;
    private int startY;
    private boolean defaultVisible;

}
