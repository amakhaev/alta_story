package com.alta.mediator.domain.interaction;

import com.alta.dao.data.preservation.InteractionPreservationModel;
import com.alta.engine.model.InteractionEngineDataModel;

import java.util.List;

/**
 * Describes the provider of data related to interactions.
 */
public interface InteractionDataProvider {

    /**
     * Gets the interaction engine model by given related map name.
     *
     * @param relatedMapName            - the name of map where interaction should be happens.
     * @param interactionPreservations  - the current preservation model.
     * @param currentChapterIndicator   - the indicator of current chapter.
     * @return the {@link InteractionEngineDataModel} instance.
     */
    InteractionEngineDataModel getInteractionByRelatedMapName(String relatedMapName,
                                                              List<InteractionPreservationModel> interactionPreservations,
                                                              int currentChapterIndicator);

}
