package com.alta.mediator.domain.interaction;

import com.alta.dao.data.preservation.InteractionPreservationModel;
import com.alta.engine.data.InteractionEngineDataModel;
import com.alta.interaction.data.InteractionModel;

import java.util.List;

/**
 * Describes the provider of data related to interactions.
 */
public interface InteractionDataProvider {

    /**
     * Gets the interaction data by given related map name.
     *
     * @param relatedMapName            - the name of map where interaction should be happens.
     * @param targetUuid                - the uuid of target for interaction.
     * @param interactionPreservations  - the current preservation data.
     * @param currentChapterIndicator   - the indicator of current chapter.
     * @return the {@link InteractionEngineDataModel} instance.
     */
    InteractionModel getInteractionByRelatedMapName(String relatedMapName,
                                                    String targetUuid,
                                                    int currentChapterIndicator,
                                                    List<InteractionPreservationModel> interactionPreservations);

}
