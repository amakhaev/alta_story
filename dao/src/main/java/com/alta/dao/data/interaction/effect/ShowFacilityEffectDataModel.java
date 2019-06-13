package com.alta.dao.data.interaction.effect;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the data that describes the show of facility effect.
 */
@Getter
@Setter
public class ShowFacilityEffectDataModel extends InteractionEffectDataModel {

    private String facilityUuid;

    /**
     * Initialize new instance of {@link ShowFacilityEffectDataModel}.
     */
    public ShowFacilityEffectDataModel() {
        super(InteractionEffectType.SHOW_FACILITY);
    }
}