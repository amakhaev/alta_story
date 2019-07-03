package com.alta.mediator.domain.interaction;

import com.alta.behaviorprocess.shared.data.InteractionModel;
import com.alta.dao.data.preservation.InteractionPreservationModel;

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
     * @return the {@link InteractionModel} instance.
     */
    InteractionModel getInteractionByRelatedMapName(String relatedMapName,
                                                    String targetUuid,
                                                    int currentChapterIndicator,
                                                    List<InteractionPreservationModel> interactionPreservations);

    /**
     * Gets the interactions by given related map name.
     *
     * @param relatedMapName            - the name of map where interaction should be happens.
     * @param interactionPreservations  - the current preservation data.
     * @param currentChapterIndicator   - the indicator of current chapter.
     * @return the {@link List<InteractionModel>} instance.
     */
    List<InteractionModel> getInteractionsByRelatedMapName(String relatedMapName,
                                                           int currentChapterIndicator,
                                                           List<InteractionPreservationModel> interactionPreservations);

}
