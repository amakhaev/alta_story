package com.alta.dao.data.map;

import com.alta.dao.data.facility.FacilityModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Provides the facility model that related to decorators
 */
@Getter
@Setter
@AllArgsConstructor
public class MapFacilityModel {

    private String name;
    private int startX;
    private int startY;
    private FacilityModel facility;

}
