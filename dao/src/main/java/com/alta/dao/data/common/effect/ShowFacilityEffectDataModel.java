package com.alta.dao.data.common.effect;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the model that describes the show of facility effect.
 */
@Getter
@Setter
public class ShowFacilityEffectDataModel extends EffectDataModel {

    private String facilityUuid;

    /**
     * Initialize new instance of {@link ShowFacilityEffectDataModel}.
     */
    public ShowFacilityEffectDataModel() {
        super(InteractionEffectType.SHOW_FACILITY);
    }
}