package com.alta.dao.data.facility;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the position of facility
 */
@Getter
@Setter
public class FacilityPositionModel {

    private int x;
    private int y;
    private FacilityTileType tileType;
    private int zIndex;

}
