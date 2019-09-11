package com.alta.engine.view.components.frameStage;

import com.alta.computator.Computator;
import com.alta.engine.view.components.actor.ActorCharacterComponent;
import com.alta.engine.view.components.facility.FacilityComponent;
import com.alta.engine.view.components.frameTemplate.FrameTemplateComponent;

import java.util.List;

/**
 * The factory for creating {@link FrameStageComponent}
 */
public interface FrameStageComponentFactory {

    /**
     * Creates the {@link FrameStageComponent} instance.
     *
     * @param frameTemplate     - the {@link FrameTemplateComponent} instance.
     * @param actorCharacters   - the {@link List} of actor characters instance.
     * @param facilities        - the {@link List} of facilities instance.
     * @param computator        - the {@link Computator} instance.
     * @return created {@link FrameStageComponent} instance.
     */
    FrameStageComponent createFrameStage(FrameTemplateComponent frameTemplate,
                                         List<ActorCharacterComponent> actorCharacters,
                                         List<FacilityComponent> facilities,
                                         Computator computator);

}
