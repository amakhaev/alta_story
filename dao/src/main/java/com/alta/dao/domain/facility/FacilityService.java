package com.alta.dao.domain.facility;

import com.alta.dao.data.facility.FacilityModel;

import java.util.List;
import java.util.Map;

/**
 * Provides the service to make CRUD operations for facilities
 */
public interface FacilityService {

    /**
     * Find the facilities by given names
     *
     * @param names - the map of names for facilities. @param key is .dscr file name @param value is name of facility
     * @return the {@link List< FacilityModel >} that were found
     */
    List<FacilityModel> findFacilitiesByName(Map<String, List<String>> names);

}
