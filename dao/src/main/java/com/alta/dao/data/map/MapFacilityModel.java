package com.alta.dao.data.map;

import com.alta.dao.data.facility.FacilityModel;
import com.alta.dao.data.facility.FacilityPositionModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Provides the facility model that related to decorators
 */
@AllArgsConstructor
public class MapFacilityModel {

    @Getter private String name;
    @Getter private int startX;
    @Getter private int startY;
    private FacilityModel facility;

    /**
     * Gets the path to image set (SpriteSheet)
     */
    public String getPathToImageSet() {
        return this.facility == null ? null : this.facility.getPathToImageSet();
    }

    /**
     * Gets the width of tile
     */
    public int getTileWidth() {
        return this.facility == null ? 0 : this.facility.getTileWidth();
    }

    /**
     * Gets the height of tile
     */
    public int getTileHeight() {
        return this.facility == null ? 0 : this.facility.getTileHeight();
    }

    /**
     * Gets the list of facility positions
     */
    public List<FacilityPositionModel> getFacilityPositions() {
        return this.facility == null ? null : this.facility.getPositions();
    }
}
