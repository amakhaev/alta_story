package com.alta.dao.data.map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Provides the decorator for the map
 */
@Getter
@Setter
@AllArgsConstructor
public class MapDecoratorModel {

    private List<MapFacilityModel> facilities;

}
