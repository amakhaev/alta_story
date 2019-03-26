package com.alta.dao.data.facility;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the position of map
 */
@Getter
@Setter
public class FacilityPositionModel {

    private int x;
    private int y;
    private int shiftFromStartX;
    private int shiftFromStartY;
    private FacilityTileType tileType;
    private int zIndex;

}
