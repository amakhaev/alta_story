package com.alta.dao.domain.facility;

import com.alta.dao.data.facility.FacilityModel;

import java.util.List;
import java.util.Map;

/**
 * Provides the service to make CRUD operations for facilities
 */
public interface FacilityService {

    /**
     * Find the map by given name
     *
     * @param facilityDescriptorName - is .dscr file name
     * @param facilityName - the name of map from descriptor
     * @return the {@link FacilityModel} that will found
     */
    FacilityModel findFacilityByName(String facilityDescriptorName, String facilityName);

}
