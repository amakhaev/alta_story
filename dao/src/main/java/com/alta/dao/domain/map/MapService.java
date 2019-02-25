package com.alta.dao.domain.map;

import com.alta.dao.data.map.MapModel;

import java.util.List;

/**
 * Provides the service to make CRUD with maps
 */
public interface MapService {

    /**
     * Gets the map that available for usage
     *
     * @param name - the name of map
     * @return the {@link MapModel} instance
     */
    MapModel getMap(String name);
}
