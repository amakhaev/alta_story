package com.alta.dao.data.common.effect.visible;

import com.alta.dao.data.common.effect.EffectDataModel;
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
        super(EffectType.SHOW_FACILITY);
    }
}