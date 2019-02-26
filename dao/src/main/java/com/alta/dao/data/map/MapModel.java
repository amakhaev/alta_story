package com.alta.dao.data.map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Provides the model that describes the map
 */
@Getter
@Setter
@AllArgsConstructor
public class MapModel {

    private String name;
    private String tiledMapAbsolutePath;
    private List<MapFacilityModel> facilities;

}
