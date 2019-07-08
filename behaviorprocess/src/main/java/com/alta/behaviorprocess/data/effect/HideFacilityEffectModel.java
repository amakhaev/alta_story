package com.alta.behaviorprocess.data.effect;

import lombok.Getter;

/**
 * Provides the model that describes the interaction by hide facility.
 */
@Getter
public class HideFacilityEffectModel extends EffectModel {

    private final String facilityUuid;

    /**
     * Initialize new instance of {@link HideFacilityEffectModel}.
     */
    public HideFacilityEffectModel(String facilityUuid) {
        super(EffectType.HIDE_FACILITY);
        this.facilityUuid = facilityUuid;
    }
}
