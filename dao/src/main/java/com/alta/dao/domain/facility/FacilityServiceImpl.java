package com.alta.dao.domain.facility;

import com.alta.dao.data.facility.FacilityList;
import com.alta.dao.data.facility.FacilityModel;
import com.alta.utils.JsonParser;
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
     * Find the facilities by given names
     *
     * @param names - the map of names for facilities. @param key is .dscr file name @param value is name of facility
     * @return the {@link List <FacilityModel>} that were found
     */
    @Override
    public List<FacilityModel> findFacilitiesByName(Map<String, List<String>> names) {
        if (names == null || names.size() == 0) {
            return Collections.emptyList();
        }

        log.debug("Start finding of facilities");
        List<FacilityModel> result = new ArrayList<>();
        names.forEach((key, value) -> {
            if (!this.availableFacilities.containsKey(key)) {
                this.availableFacilities.put(key, this.loadFacilityList(key));
            }

            value.parallelStream().forEach(facilityName -> {
                FacilityModel facilityModel = this.findFacilityByName(
                        this.availableFacilities.get(key),
                        facilityName
                );
                if (facilityModel == null) {
                    log.debug(
                            "Can't find facility {} in file {}.",
                            facilityName,
                            key
                    );
                    return;
                }
                result.add(facilityModel);
            });
        });

        log.debug("Loading of facilities completed");
        return result;
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
