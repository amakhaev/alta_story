package com.alta.dao.data.common.effect;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the model that describes the hide of facility effect.
 */
@Getter
@Setter
public class HideFacilityEffectDataModel extends EffectDataModel {

    private String facilityUuid;

    /**
     * Initialize new instance of {@link HideFacilityEffectDataModel}.
     */
    public HideFacilityEffectDataModel() {
        super(InteractionEffectType.HIDE_FACILITY);
    }
}
