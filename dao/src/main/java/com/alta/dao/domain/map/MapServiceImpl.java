package com.alta.dao.domain.map;

import com.alta.dao.ResourcesLocation;
import com.alta.dao.data.map.MapFacilityModel;
import com.alta.dao.data.map.MapModel;
import com.alta.dao.domain.facility.FacilityService;
import com.alta.utils.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Provides the service to make CRUD with maps
 */
@Slf4j
public class MapServiceImpl implements MapService {

    private final FacilityService facilityService;
    private Map<String, MapModel> mapsByName;
    private List<MapEntity> availableMaps;

    /**
     * Initialize new instance of {@link MapServiceImpl}
     */
    @Inject
    public MapServiceImpl(FacilityService facilityService) {
        this.facilityService = facilityService;
        this.mapsByName = new HashMap<>();
        this.loadAvailableMaps();
    }

    /**
     * Gets the map that available for usage
     *
     * @param name - the name of map
     * @return the {@link MapModel} instance
     */
    @Override
    public MapModel getMap(String name) {
        if (this.mapsByName.containsKey(name)) {
            log.debug("Map with given name {} already initialized. Return it.", name);
            return this.mapsByName.get(name);
        }

        log.debug("Map with given name {} not initialized. Try to initialize.", name);
        MapEntity matchedMapEntity = this.availableMaps
                .stream()
                .filter(mapEntity -> mapEntity.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);

        if (matchedMapEntity == null) {
            log.error("Map with given name {} not found", name);
            return null;
        }

        MapModel mapModel = new MapModel(
                matchedMapEntity.getName(),
                this.getAbsolutePathToMap(matchedMapEntity.getTiledMapPath()),
                this.getFacilities(matchedMapEntity.getDecoratorPath())
        );

        this.mapsByName.put(matchedMapEntity.getName(), mapModel);
        log.info("Map initialization completed successfully", name);
        return mapModel;
    }

    /**
     * Gets the absolute path to map by relative path
     *
     * @param relativePath - the relative path of map
     * @return the absolute path
     */
    private String getAbsolutePathToMap(String relativePath) {
        return this.getClass().getClassLoader().getResource(relativePath).getPath();
    }

    private void loadAvailableMaps() {
        try {
            this.availableMaps = JsonParser.parse(
                    this.getClass().getClassLoader().getResource(ResourcesLocation.MAPS_DESCRIPTOR_FILE).getPath(),
                    new TypeToken<ArrayList<MapEntity>>(){}.getType()
            );
        } catch (NullPointerException e) {
            log.error(e.getMessage());
        }
    }

    private List<MapFacilityModel> getFacilities(String decoratorPath) {
        try {
            MapDecoratorEntity internalDecorator = JsonParser.parse(
                    this.getClass().getClassLoader().getResource(decoratorPath).getPath(),
                    MapDecoratorEntity.class
            );

            return internalDecorator.getFacilities()
                    .parallelStream()
                    .map(
                            facilityEntity -> new MapFacilityModel(
                                    facilityEntity.getName(),
                                    facilityEntity.getStartX(),
                                    facilityEntity.getStartY(),
                                    this.facilityService.findFacilityByName(
                                            facilityEntity.getDescriptorFileName(),
                                            facilityEntity.getName()
                                    ),
                                    name1, startX1, startY1, facility1)
                    )
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
