package com.alta.dao.domain.map;

import com.alta.dao.ResourcesLocation;
import com.alta.dao.data.map.MapModel;
import com.alta.utils.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides the service to make CRUD with maps
 */
@Slf4j
public class MapServiceImpl implements MapService {

    /**
     * Gets the list of maps that available for usage
     *
     * @return the {@link List} of map instance
     */
    @Override
    public List<MapModel> getAvailableMaps() {
        try {
            return JsonParser.parse(
                    this.getClass().getClassLoader().getResource(ResourcesLocation.MAPS_DESCRIPTOR_FILE).getPath(),
                    new TypeToken<ArrayList<MapModel>>(){}.getType()
            );
        } catch (NullPointerException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * Gets the absolute path to map by relative path
     *
     * @param relativePath - the relative path of map
     * @return the absolute path
     */
    @Override
    public String getAbsolutePathToMap(String relativePath) {
        return this.getClass().getClassLoader().getResource(relativePath).getPath();
    }
}
