package com.alta.mediator.domain.interaction;

import com.alta.behaviorprocess.data.interaction.InteractionModel;
import com.alta.dao.data.preservation.InteractionPreservationModel;

import java.util.List;

/**
 * Describes the provider of model related to interactions.
 */
public interface InteractionDataProvider {

    /**
     * Gets the interaction model by given related map name.
     *
     * @param relatedMapName            - the name of map where interaction should be happens.
     * @param targetUuid                - the uuid of target for interaction.
     * @param interactionPreservations  - the current preservation model.
     * @param currentChapterIndicator   - the indicator of current character.
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
     * @param interactionPreservations  - the current preservation model.
     * @param currentChapterIndicator   - the indicator of current character.
     * @return the {@link List<InteractionModel>} instance.
     */
    List<InteractionModel> getInteractionsByRelatedMapName(String relatedMapName,
                                                           int currentChapterIndicator,
                                                           List<InteractionPreservationModel> interactionPreservations);

}
