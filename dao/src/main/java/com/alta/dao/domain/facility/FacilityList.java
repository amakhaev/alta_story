package com.alta.dao.domain.facility;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Provides the list of facilities that available for application
 */
@Getter
@Setter
class FacilityList {

    private Map<String, FacilityModel> detailsAsMap;

    private String imageName;
    private int tileWidth;
    private int tileHeight;
    private List<FacilityModel> details;

    public Map<String, FacilityModel> getDetailsAsMap() {
        if (this.detailsAsMap == null && this.details != null) {
            this.detailsAsMap = this.details.parallelStream()
                    .collect(Collectors.toMap(FacilityModel::getName, detail -> detail));
        }

        return this.detailsAsMap;
    }
}
