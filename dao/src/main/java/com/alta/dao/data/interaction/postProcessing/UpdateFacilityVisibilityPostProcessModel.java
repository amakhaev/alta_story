package com.alta.dao.data.interaction.postProcessing;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the data that describe the update of facility.
 */
@Getter
@Setter
public class UpdateFacilityVisibilityPostProcessModel extends InteractionPostProcessingModel {

    private String facilityUuid;
    private boolean value;

    /**
     * Initialize new instance of {@link UpdateFacilityVisibilityPostProcessModel}.
     */
    public UpdateFacilityVisibilityPostProcessModel() {
        super(ProcessingType.UPDATE_FACILITY_VISIBILITY);
    }
}
