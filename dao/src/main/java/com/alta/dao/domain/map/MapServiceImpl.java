package com.alta.dao.domain.map;

import com.alta.dao.ResourcesLocation;
import com.alta.dao.data.map.MapFacilityModel;
import com.alta.dao.data.map.MapModel;
import com.alta.dao.domain.facility.FacilityService;
import com.alta.dao.domain.map.internalEntities.MapDecoratorEntity;
import com.alta.dao.domain.map.internalEntities.MapEntity;
import com.alta.utils.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides the service to make CRUD with maps
 */
@Slf4j
public class MapServiceImpl implements MapService {

    private final FacilityService facilityService;
    private List<MapEntity> availableMaps;

    /**
     * Initialize new instance of {@link MapServiceImpl}
     */
    @Inject
    public MapServiceImpl(FacilityService facilityService) {
        this.facilityService = facilityService;
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
        log.info("Try to initialize map with given name '{}'", name);
        MapEntity matchedMapEntity = this.availableMaps
                .stream()
                .filter(mapEntity -> mapEntity.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);

        if (matchedMapEntity == null) {
            log.error("Map with given name '{}' not found", name);
            return null;
        }

        MapDecoratorEntity internalDecorator = JsonParser.parse(
                this.getClass().getClassLoader().getResource(matchedMapEntity.getDecoratorPath()).getPath(),
                MapDecoratorEntity.class
        );

        MapModel mapModel = MapModel.builder()
                .name(matchedMapEntity.getName())
                .displayName(internalDecorator.getDisplayName())
                .tiledMapAbsolutePath(this.getAbsolutePathToMap(matchedMapEntity.getTiledMapPath()))
                .facilities(this.getFacilities(internalDecorator))
                .simpleNpcList(internalDecorator.getSimpleNpcList())
                .alterableNpcList(internalDecorator.getAlterableNpcList())
                .mapJumpings(internalDecorator.getJumping())
                .build();

        log.info("Initialization for map '{}' completed", name);
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

    private List<MapFacilityModel> getFacilities(MapDecoratorEntity internalDecorator) {
        try {
            return internalDecorator.getFacilities()
                    .stream()
                    .map(
                            facilityEntity -> new MapFacilityModel(
                                    facilityEntity.getUuid(),
                                    facilityEntity.getName(),
                                    facilityEntity.getStartX(),
                                    facilityEntity.getStartY(),
                                    facilityEntity.isDefaultVisible(),
                                    this.facilityService.findFacilityByName(
                                            facilityEntity.getDescriptorFileName(),
                                            facilityEntity.getName()
                                    ))
                    )
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
