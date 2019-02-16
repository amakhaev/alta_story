package com.alta.dao.domain.map;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides the container for maps. Make usage more simple
 */
public class MapsContainer {

    private Map<String, MapModel> mapsByName;

    /**
     * Initialize ew instance of {@link MapsContainer}
     */
    public MapsContainer(List<MapModel> maps) {
        this.mapsByName = new HashMap<>();
        maps.forEach(m -> mapsByName.put(m.getName(), m));
    }

    /**
     * Gets the map by given name
     *
     * @param name - the name to search
     * @return the {@link MapModel} instance
     */
    public MapModel getMapByName(String name) {
        return this.mapsByName.get(name);
    }
}
