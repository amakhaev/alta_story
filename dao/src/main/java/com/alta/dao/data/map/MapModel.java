package com.alta.dao.data.map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Provides the model that describes the map
 */
@Getter
@Setter
@AllArgsConstructor
public class MapModel {

    private String name;
    private String tiledMapAbsolutePath;
    private MapDecoratorModel decorator;

}
