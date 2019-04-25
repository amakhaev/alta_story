package com.alta.mediator.domain.interaction;

import com.alta.dao.data.interaction.InteractionConditionModel;
import com.alta.dao.data.preservation.InteractionPreservationModel;
import com.alta.dao.domain.preservation.PreservationService;
import com.alta.dao.domain.preservation.TemporaryDataPreservationService;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import java.util.function.Function;

/**
 * Provides the service to create conditions.
 */
@Slf4j
public class InteractionConditionService {

    private final PreservationService preservationService;
    private final TemporaryDataPreservationService temporaryDataPreservationService;
    private final Long currentPreservationId;

    /**
     * Initialize new instance of {@link InteractionConditionService}.
     */
    @Inject
    public InteractionConditionService(PreservationService preservationService,
                                       TemporaryDataPreservationService temporaryDataPreservationService,
                                       @Named("currentPreservationId") Long currentPreservationId) {
        this.preservationService = preservationService;
        this.temporaryDataPreservationService = temporaryDataPreservationService;
        this.currentPreservationId = currentPreservationId;
    }

    /**
     * Builds the condition related to specific interaction.
     *
     * @param conditionData - the data to be used to create condition.
     */
    Function<Void, Boolean> build(InteractionConditionModel conditionData) {
        if (conditionData == null) {
            return null;
        }

        switch (conditionData.getConditionType()) {
            case ITERACTION_COMPLETED:
                return this.createInteractionCompletedCondition(conditionData.getUuid());
            default:
                log.error("Unknown type of condition data {}", conditionData.getConditionType());
                return null;
        }
    }

    private Function<Void, Boolean> createInteractionCompletedCondition(String uuid) {
        if (Strings.isNullOrEmpty(uuid)) {
            log.error(
                    "Invalid condition data. Uuid required for the {} type",
                    InteractionConditionModel.ConditionType.ITERACTION_COMPLETED
            );
            return null;
        }

        return aVoid -> {
            InteractionPreservationModel interactionPreservation = this.temporaryDataPreservationService
                    .findInteractionByPreservationIdAndUuid(this.currentPreservationId, uuid);

            if (interactionPreservation == null) {
                interactionPreservation = this.preservationService.findInteractionByPreservationIdAndUuid(
                        this.currentPreservationId, uuid
                );
            }

            if (interactionPreservation == null) {
                return false;
            }

            return interactionPreservation.isCompleted();
        };
    }

}
