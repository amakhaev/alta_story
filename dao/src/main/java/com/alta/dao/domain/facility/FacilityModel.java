package com.alta.dao.domain.facility;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the model that describes the facility
 */
@Getter
@Setter
public class FacilityModel {

    private String name;
    private int tileWidth;
    private int tileHeight;
    private int shiftX;
    private int shiftY;
    private FacilityTileType tileType;
    private int zIndex;

}
