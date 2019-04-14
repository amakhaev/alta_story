package com.alta.mediator.domain.interaction;

import com.alta.engine.model.InteractionDataModel;

/**
 * Describes the provider of data related to interactions.
 */
public interface InteractionDataProvider {

    /**
     * Gets the interaction engine model by given related map name.
     *
     * @param relatedMapName            - the name of map where interaction should be happens.
     * @param currentChapterIndicator   - the indicator of current chapter.
     * @return the {@link InteractionDataModel} instance.
     */
    InteractionDataModel getInteractionByRelatedMapName(String relatedMapName, int currentChapterIndicator);

}
