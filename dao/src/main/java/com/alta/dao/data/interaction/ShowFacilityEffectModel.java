package com.alta.dao.data.interaction;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the model that describes the show of facility effect.
 */
@Getter
@Setter
public class ShowFacilityEffectModel extends InteractionEffectModel {

    private String facilityUuid;

    /**
     * Initialize new instance of {@link ShowFacilityEffectModel}.
     */
    public ShowFacilityEffectModel() {
        super(InteractionEffectType.SHOW_FACILITY);
    }
}