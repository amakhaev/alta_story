package com.alta.dao.domain.map;

import com.alta.dao.data.map.MapModel;

import java.util.List;

/**
 * Provides the service to make CRUD with maps
 */
public interface MapService {

    /**
     * Gets the list of maps that available for usage
     *
     * @return the {@link List} of map instance
     */
    List<MapModel> getAvailableMaps();

    /**
     * Gets the absolute path to map by relative path
     *
     * @param relativePath - the relative path of map
     * @return the absolute path
     */
    String getAbsolutePathToMap(String relativePath);
}
