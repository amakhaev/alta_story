package com.alta.dao.domain.facility;

import com.alta.dao.data.facility.FacilityList;
import com.alta.dao.data.facility.FacilityModel;
import com.alta.utils.JsonParser;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Provides the service to make CRUD operations for facilities
 */
@Slf4j
public class FacilityServiceImpl implements FacilityService {

    private static final String FACILITIES_FOLDER = "scene_data/facilities/";

    private Map<String, FacilityList> availableFacilities;

    /**
     * Initialize new instance of {@link FacilityServiceImpl}
     */
    public FacilityServiceImpl() {
        this.availableFacilities = new HashMap<>();
    }

    /**
     * Find the facility by given name
     *
     * @param facilityDescriptorName - is .dscr file name
     * @param facilityName           - the name of facility from descriptor
     * @return the {@link FacilityModel} that will found
     */
    @Override
    public FacilityModel findFacilityByName(String facilityDescriptorName, String facilityName) {
        if (Strings.isNullOrEmpty(facilityDescriptorName) || Strings.isNullOrEmpty(facilityName)) {
            log.warn("Invalid incoming arguments");
            return null;
        }

        log.debug("Start finding of facilities with given name '{}'", facilityName);
        if (!this.availableFacilities.containsKey(facilityDescriptorName)) {
            this.availableFacilities.put(facilityDescriptorName, this.loadFacilityList(facilityDescriptorName));
        }

        FacilityModel facilityModel = this.findFacilityByName(
                this.availableFacilities.get(facilityDescriptorName),
                facilityName
        );
        if (facilityModel == null) {
            log.debug(
                    "Can't find facility {} in file {}.",
                    facilityName,
                    facilityDescriptorName
            );
            return null;
        }

        log.debug("Loading of facility with given name '{}' completed", facilityName);
        return facilityModel;
    }

    private FacilityList loadFacilityList(String name) {
        String pathToDscrFile = FACILITIES_FOLDER + name + ".dscr";
        return JsonParser.parse(
                this.getClass().getClassLoader().getResource(pathToDscrFile).getPath(),
                FacilityList.class
        );
    }

    private FacilityModel findFacilityByName(FacilityList facilityList,
                                             String facilityName) {
        FacilityModel facilityModel = facilityList.getDetailsAsMap().get(facilityName);
        if (facilityModel == null) {
            return null;
        }

        facilityModel.setTileWidth(facilityList.getTileWidth());
        facilityModel.setTileHeight(facilityList.getTileHeight());
        facilityModel.setPathToImageSet(
                this.getClass().getClassLoader().getResource(
                        FACILITIES_FOLDER + facilityList.getImageName()
                ).getPath()
        );

        return facilityModel;
    }
}
