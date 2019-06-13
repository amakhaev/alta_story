package com.alta.dao.data.interaction.effect;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the data that describes the hide of facility effect.
 */
@Getter
@Setter
public class HideFacilityEffectDataModel extends InteractionEffectDataModel {

    private String facilityUuid;

    /**
     * Initialize new instance of {@link HideFacilityEffectDataModel}.
     */
    public HideFacilityEffectDataModel() {
        super(InteractionEffectType.HIDE_FACILITY);
    }
}
