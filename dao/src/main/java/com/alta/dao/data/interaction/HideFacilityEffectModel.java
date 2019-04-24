package com.alta.dao.data.interaction;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the model that describes the hide of facility effect.
 */
@Getter
@Setter
public class HideFacilityEffectModel extends InteractionEffectModel {

    private String facilityUuid;

    /**
     * Initialize new instance of {@link HideFacilityEffectModel}.
     */
    public HideFacilityEffectModel() {
        super(InteractionEffectType.HIDE_FACILITY);
    }
}
