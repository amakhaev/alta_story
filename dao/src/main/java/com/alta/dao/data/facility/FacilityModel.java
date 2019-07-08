package com.alta.dao.data.facility;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Provides the model that describes the map
 */
@Getter
@Setter
public class FacilityModel {

    private String name;
    private String pathToImageSet;
    private int tileWidth;
    private int tileHeight;
    private List<FacilityPositionModel> positions;

}
