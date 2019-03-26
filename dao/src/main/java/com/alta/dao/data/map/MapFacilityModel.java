package com.alta.dao.data.map;

import com.alta.dao.data.facility.FacilityModel;
import com.alta.dao.data.facility.FacilityPositionModel;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Provides the map model that related to decorators
 */
public class MapFacilityModel {

    @Getter private final String name;
    @Getter private final int startX;
    @Getter private final int startY;
    @Getter private final UUID uuid;
    private final FacilityModel facility;

    /**
     * Initialize new instance of {@link MapFacilityModel}
     */
    public MapFacilityModel(String name, int startX, int startY, FacilityModel facility) {
        this.name = name;
        this.startX = startX;
        this.startY = startY;
        this.facility = facility;
        this.uuid = UUID.randomUUID();
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
