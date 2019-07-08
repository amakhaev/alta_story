package com.alta.mediator.domain.effect;

import com.alta.behaviorprocess.data.effect.EffectModel;
import com.alta.dao.data.common.effect.EffectDataModel;

import java.util.List;

/**
 * Describes the provider of model related to effects.
 */
public interface EffectDataProvider {

    /**
     * Gets effects from DAO models.
     *
     * @param effects - the effects to be converted.
     * @return the {@link List} of effects.
     */
    List<EffectModel> getEffects(List<EffectDataModel> effects);

}
