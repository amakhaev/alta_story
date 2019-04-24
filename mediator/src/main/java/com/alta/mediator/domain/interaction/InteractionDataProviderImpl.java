package com.alta.mediator.domain.interaction;

import com.alta.dao.data.interaction.DialogueEffectModel;
import com.alta.dao.data.interaction.HideFacilityEffectModel;
import com.alta.dao.data.interaction.InteractionEffectModel;
import com.alta.dao.data.interaction.InteractionModel;
import com.alta.dao.data.preservation.InteractionPreservationModel;
import com.alta.dao.data.preservation.PreservationModel;
import com.alta.dao.domain.interaction.InteractionService;
import com.alta.engine.model.InteractionDataModel;
import com.alta.engine.model.interaction.DialogueEffectEngineModel;
import com.alta.engine.model.interaction.HideFacilityEffectEngineModel;
import com.alta.engine.model.interaction.InteractionEffectEngineModel;
import com.alta.engine.model.interaction.InteractionEngineModel;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Describes the provider of data related to interactions.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class InteractionDataProviderImpl implements InteractionDataProvider {

    private final InteractionService interactionService;

    /**
     * Gets the interaction engine model by given related map name.
     *
     * @param relatedMapName           - the name of map where interaction should be happens.
     * @param interactionPreservations - the current preservation model.
     * @param currentChapterIndicator  - the indicator of current chapter.
     * @return the {@link InteractionDataModel} instance.
     */
    @Override
    public InteractionDataModel getInteractionByRelatedMapName(@NonNull String relatedMapName,
                                                               @NonNull List<InteractionPreservationModel> interactionPreservations,
                                                               int currentChapterIndicator) {
        List<InteractionModel> interactions = this.interactionService.getInteractions(relatedMapName);
        if (interactions == null || interactions.size() == 0) {
            log.debug("Interactions for given map '{}' not found", relatedMapName);
            return InteractionDataModel.builder().interactions(Collections.emptyList()).build();
        }

        log.debug("{} interactions found for '{}' map", interactions.size(), relatedMapName);

        List<InteractionEngineModel> interactionEngineModels = interactions.stream()
                .filter(interactionModel -> interactionModel.getChapterIndicatorFrom() != null)
                .filter(interactionModel -> interactionModel.getChapterIndicatorTo() != null)
                .filter(interactionModel -> currentChapterIndicator >= interactionModel.getChapterIndicatorFrom())
                .filter(interactionModel -> currentChapterIndicator <= interactionModel.getChapterIndicatorTo())
                .map(interactionModel -> this.createInteractionModelWithChildren(
                        interactionModel, interactions, interactionPreservations
                ))
                .collect(Collectors.toList());

        log.info("{} interactions found for '{}' map.", interactions.size(), relatedMapName);

        return InteractionDataModel.builder().interactions(interactionEngineModels).build();
    }

    private InteractionEngineModel createInteractionModelWithChildren(InteractionModel parent,
                                                                      List<InteractionModel> sources,
                                                                      @NonNull List<InteractionPreservationModel> interactionPreservations) {
        InteractionEngineModel childInteraction = null;
        if (!Strings.isNullOrEmpty(parent.getNextInteractionUuid())) {
            InteractionModel child = sources.stream()
                    .filter(s -> s.getUuid().equals(parent.getNextInteractionUuid()))
                    .findFirst()
                    .orElse(null);

            if (child != null) {
                childInteraction = this.createInteractionModelWithChildren(child, sources, interactionPreservations);
            }
        }

        InteractionPreservationModel interactionPreservationModel = interactionPreservations
                .stream()
                .filter(interaction -> interaction.getUuid().equals(parent.getUuid()))
                .findFirst()
                .orElse(null);

        return InteractionEngineModel.builder()
                .uuid(parent.getUuid())
                .targetUuid(parent.getTargetUuid())
                .interactionEffects(this.createEffects(parent.getEffects()))
                .shiftTileX(parent.getShiftTileX())
                .shiftTileY(parent.getShiftTileY())
                .isCompleted(interactionPreservationModel != null)
                .next(childInteraction)
                .build();
    }

    private List<InteractionEffectEngineModel> createEffects(List<InteractionEffectModel> effects) {
        if (effects == null || effects.size() == 0) {
            return Collections.emptyList();
        }

        return effects.stream()
                .map(effect -> {
                    switch (effect.getType()) {
                        case DIALOGUE:
                            return new DialogueEffectEngineModel(((DialogueEffectModel)effect).getText());
                        case HIDE_FACILITY:
                            return new HideFacilityEffectEngineModel(((HideFacilityEffectModel)effect).getFacilityUuid());
                        default:
                            throw new IllegalArgumentException("Unknown type of interaction effect: " + effect.getType());
                    }
                })
                .collect(Collectors.toList());
    }
}
