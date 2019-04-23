package com.alta.dao.data.map;

import com.alta.dao.data.facility.FacilityModel;
import com.alta.dao.data.facility.FacilityPositionModel;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * Provides the map model that related to decorators
 */
public class MapFacilityModel {


    @Getter
    private final String name;

    @Getter
    private final int startX;

    @Getter
    private final int startY;

    @Getter
    private final String uuid;

    @Getter
    private final boolean defaultVisible;

    private final FacilityModel facility;

    /**
     * Initialize new instance of {@link MapFacilityModel}
     */
    public MapFacilityModel(String uuid, String name, int startX, int startY, boolean defaultVisible, FacilityModel facility) {
        this.name = name;
        this.startX = startX;
        this.startY = startY;
        this.defaultVisible = defaultVisible;
        this.facility = facility;
        this.uuid = uuid;
    }

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
     * Gets the list of map positions
     */
    public List<FacilityPositionModel> getFacilityPositions() {
        return this.facility == null ? Collections.emptyList() : this.facility.getPositions();
    }
}
