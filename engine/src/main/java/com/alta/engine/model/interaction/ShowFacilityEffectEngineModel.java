package com.alta.engine.model.interaction;

import lombok.Getter;

/**
 * Provides the model that describes the interaction by show facility.
 */
@Getter
public class ShowFacilityEffectEngineModel extends InteractionEffectEngineModel {

    private final String facilityUuid;

    /**
     * Initialize new instance of {@link HideFacilityEffectEngineModel}.
     */
    public ShowFacilityEffectEngineModel(String facilityUuid) {
        super(EffectType.SHOW_FACILITY);
        this.facilityUuid = facilityUuid;
    }
}
