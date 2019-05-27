package com.alta.mediator.domain.interaction;

import com.alta.dao.data.interaction.InteractionDataModel;
import com.alta.dao.data.interaction.effect.DialogueEffectDataModel;
import com.alta.dao.data.interaction.effect.HideFacilityEffectDataModel;
import com.alta.dao.data.interaction.effect.InteractionEffectDataModel;
import com.alta.dao.data.interaction.effect.ShowFacilityEffectDataModel;
import com.alta.dao.data.preservation.InteractionPreservationModel;
import com.alta.dao.domain.interaction.InteractionService;
import com.alta.interaction.data.DialogueEffectModel;
import com.alta.interaction.data.EffectModel;
import com.alta.interaction.data.HideFacilityEffectModel;
import com.alta.interaction.data.ShowFacilityEffectModel;
import com.alta.interaction.interactionOnMap.InteractionModel;
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
    private final InteractionConditionService conditionService;

    /**
     * Gets the interaction engine model by given related map name.
     *
     * @param relatedMapName           - the name of map where interaction should be happens.
     * @param interactionPreservations - the current preservation model.
     * @param currentChapterIndicator  - the indicator of current chapter.
     * @return the {@link com.alta.engine.model.InteractionDataModel} instance.
     */
    @Override
    public com.alta.engine.model.InteractionDataModel getInteractionByRelatedMapName(@NonNull String relatedMapName,
                                                                                     @NonNull List<InteractionPreservationModel> interactionPreservations,
                                                                                     int currentChapterIndicator) {
        List<InteractionDataModel> interactions = this.interactionService.getInteractions(relatedMapName);
        if (interactions == null || interactions.size() == 0) {
            log.debug("Interactions for given map '{}' not found", relatedMapName);
            return com.alta.engine.model.InteractionDataModel.builder().interactions(Collections.emptyList()).build();
        }

        log.debug("{} interactions found for '{}' map", interactions.size(), relatedMapName);

        List<com.alta.interaction.interactionOnMap.InteractionModel> interactionEngineModels = interactions.stream()
                .filter(interactionModel -> interactionModel.getChapterIndicatorFrom() != null)
                .filter(interactionModel -> interactionModel.getChapterIndicatorTo() != null)
                .filter(interactionModel -> currentChapterIndicator >= interactionModel.getChapterIndicatorFrom())
                .filter(interactionModel -> currentChapterIndicator <= interactionModel.getChapterIndicatorTo())
                .map(interactionModel -> this.createInteractionModelWithChildren(
                        interactionModel, interactions, interactionPreservations
                ))
                .collect(Collectors.toList());

        log.info("{} interactions found for '{}' map.", interactions.size(), relatedMapName);

        return com.alta.engine.model.InteractionDataModel.builder().interactions(interactionEngineModels).build();
    }

    private InteractionModel createInteractionModelWithChildren(InteractionDataModel parent,
                                                                List<InteractionDataModel> sources,
                                                                @NonNull List<InteractionPreservationModel> interactionPreservations) {
        InteractionModel childInteraction = null;
        if (!Strings.isNullOrEmpty(parent.getNextInteractionUuid())) {
            InteractionDataModel child = sources.stream()
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

        return InteractionModel.builder()
                .uuid(parent.getUuid())
                .targetUuid(parent.getTargetUuid())
                .interactionEffects(this.createEffects(parent.getEffects()))
                .failedPreConditionInteractionEffects(this.createEffects(parent.getFailedPreConditionEffects()))
                .shiftTiles(parent.getShiftTiles())
                .isCompleted(interactionPreservationModel != null)
                .preCondition(this.conditionService.build(parent.getPreCondition()))
                .next(childInteraction)
                .build();
    }

    private List<EffectModel> createEffects(List<InteractionEffectDataModel> effects) {
        if (effects == null || effects.size() == 0) {
            return Collections.emptyList();
        }

        return effects.stream()
                .map(effect -> {
                    switch (effect.getType()) {
                        case DIALOGUE:
                            return new DialogueEffectModel(((DialogueEffectDataModel)effect).getText());
                        case HIDE_FACILITY:
                            return new HideFacilityEffectModel(((HideFacilityEffectDataModel)effect).getFacilityUuid());
                        case SHOW_FACILITY:
                            return new ShowFacilityEffectModel(((ShowFacilityEffectDataModel)effect).getFacilityUuid());
                        default:
                            throw new IllegalArgumentException("Unknown type of interaction effect: " + effect.getType());
                    }
                })
                .collect(Collectors.toList());
    }
}
