package com.alta.mediator.domain.effect;

import com.alta.dao.data.common.effect.EffectDataModel;

import java.util.List;

/**
 * Provides the service for working with background post effects.
 */
public interface BackgroundEffectService {

    /**
     * Executes the background effects.
     *
     * @param effects - the list of effects to be executed.
     */
    void executeBackgroundEffects(List<EffectDataModel> effects);

}
