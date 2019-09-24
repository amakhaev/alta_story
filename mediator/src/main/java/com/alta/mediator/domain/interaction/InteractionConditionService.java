package com.alta.mediator.domain.interaction;

import com.alta.dao.data.interaction.InteractionConditionModel;
import com.alta.dao.data.preservation.InteractionPreservationModel;
import com.alta.dao.domain.preservation.PreservationService;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import java.util.UUID;
import java.util.function.Function;

/**
 * Provides the service to create conditions.
 */
@Slf4j
public class InteractionConditionService {

    private final PreservationService preservationService;
    private final Long currentPreservationId;

    /**
     * Initialize new instance of {@link InteractionConditionService}.
     */
    @Inject
    public InteractionConditionService(PreservationService preservationService,
                                       @Named("currentPreservationId") Long currentPreservationId) {
        this.preservationService = preservationService;
        this.currentPreservationId = currentPreservationId;
    }

    /**
     * Builds the condition related to specific interaction.
     *
     * @param conditionData - the model to be used to create condition.
     */
    Function<Void, Boolean> build(InteractionConditionModel conditionData) {
        if (conditionData == null) {
            return null;
        }

        switch (conditionData.getConditionType()) {
            case INTERACTION_COMPLETED:
                return this.createInteractionCompletedCondition(conditionData.getUuid());
            default:
                log.error("Unknown type of condition model {}", conditionData.getConditionType());
                return null;
        }
    }

    private Function<Void, Boolean> createInteractionCompletedCondition(String uuid) {
        if (Strings.isNullOrEmpty(uuid)) {
            log.error(
                    "Invalid condition model. Uuid required for the {} type",
                    InteractionConditionModel.ConditionType.INTERACTION_COMPLETED
            );
            return null;
        }

        return aVoid -> {
            InteractionPreservationModel interactionPreservation = this.preservationService.getInteraction(
                    this.currentPreservationId.intValue(), UUID.fromString(uuid)
            );

            if (interactionPreservation == null) {
                return false;
            }

            return interactionPreservation.isCompleted();
        };
    }

}
