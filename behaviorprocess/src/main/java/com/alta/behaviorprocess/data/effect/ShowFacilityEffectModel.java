package com.alta.behaviorprocess.data.effect;

import lombok.Getter;

/**
 * Provides the model that describes the interaction by show facility.
 */
@Getter
public class ShowFacilityEffectModel extends EffectModel {

    private final String facilityUuid;

    /**
     * Initialize new instance of {@link HideFacilityEffectModel}.
     */
    public ShowFacilityEffectModel(String facilityUuid) {
        super(EffectType.SHOW_FACILITY);
        this.facilityUuid = facilityUuid;
    }
}
