package com.alta.dao.domain.facility;

import com.alta.utils.JsonParser;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Provides the service to make CRUD operations for facilities
 */
@Slf4j
public class FacilityServiceImpl implements FacilityService {

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
        return names.entrySet().stream().map(
                es -> {
                    if (!this.availableFacilities.containsKey(es.getKey())) {
                        this.availableFacilities.put(es.getKey(), this.loadFacilityList(es.getKey()));
                    }

                    es.getValue().parallelStream()
                            .filter()

                    FacilityModel facility = this.availableFacilities.get(es.getKey()).getDetails()
                            .parallelStream()
                            .filter(detail -> detail.getName().equalsIgnoreCase(es.getValue()))
                            .findFirst()
                            .orElse(null);

                    if (facility == null) {
                        log.debug("Can't find facility {} in file {}. Null will be returned.", es.getValue(), es.getKey());
                    }
                    return facility;
                }
        )
        .collect(Collectors.toList());
    }

    private FacilityList loadFacilityList(String name) {
        return JsonParser.parse(
                this.getClass().getClassLoader().getResource(name + ".dscr").getPath(),
                FacilityList.class
        );
    }
}
