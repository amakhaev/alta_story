package com.alta.engine.model.interaction;

import lombok.Getter;

/**
 * Provides the model that describes the interaction by hide facility.
 */
@Getter
public class HideFacilityEffectEngineModel extends InteractionEffectEngineModel {

    private final String facilityUuid;

    /**
     * Initialize new instance of {@link HideFacilityEffectEngineModel}.
     */
    public HideFacilityEffectEngineModel(String facilityUuid) {
        super(EffectType.HIDE_FACILITY);
        this.facilityUuid = facilityUuid;
    }
}
