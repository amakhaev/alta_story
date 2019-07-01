package com.alta.mediator.domain.interaction;

import com.alta.behaviorprocess.shared.data.*;
import com.alta.dao.data.interaction.InteractionDataModel;
import com.alta.dao.data.common.effect.DialogueEffectDataModel;
import com.alta.dao.data.common.effect.HideFacilityEffectDataModel;
import com.alta.dao.data.common.effect.EffectDataModel;
import com.alta.dao.data.common.effect.ShowFacilityEffectDataModel;
import com.alta.dao.data.preservation.InteractionPreservationModel;
import com.alta.dao.domain.interaction.InteractionService;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
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
     * Gets the interaction data by given related map name.
     *
     * @param relatedMapName           - the name of map where interaction should be happens.
     * @param targetUuid               - the uuid of target for interaction.
     * @param currentChapterIndicator  - the indicator of current chapter.
     * @param interactionPreservations - the current preservation data.
     * @return the {@link InteractionModel} instance.
     */
    @Override
    public InteractionModel getInteractionByRelatedMapName(@NonNull String relatedMapName,
                                                           @NonNull String targetUuid,
                                                           int currentChapterIndicator,
                                                           List<InteractionPreservationModel> interactionPreservations) {
        Map<String, InteractionDataModel> interactions = this.interactionService.getInteractions(
                relatedMapName, targetUuid, currentChapterIndicator
        );

        if (interactions == null || interactions.size() == 0) {
            log.debug("Interactions for given map '{}' not found", relatedMapName);
            return null;
        }

        log.debug("{} interactions found for '{}' map", interactions.size(), relatedMapName);

        List<InteractionModel> interactionEngineModels = this.createInteractions(
                relatedMapName, interactions, interactionPreservations
        );

        if (interactionEngineModels.size() > 1) {
            log.warn("Found {} interactions but should be one. First will be returned.", interactionEngineModels.size());
        }

        return interactionEngineModels.isEmpty() ? null : interactionEngineModels.get(0);
    }

    private List<InteractionModel> createInteractions(String relatedMapName,
                                                      Map<String, InteractionDataModel> rawData,
                                                      List<InteractionPreservationModel> interactionPreservations) {
        List<String> interactionUuids = new ArrayList<>(rawData.keySet());

        Map<String, InteractionModel> interactionModels = rawData.entrySet().stream()
                .map(es -> this.createInteraction(relatedMapName, es.getValue(), interactionPreservations))
                .collect(Collectors.toMap(InteractionModel::getUuid, interaction -> interaction));

        List<InteractionModel> resultList = new ArrayList<>();
        interactionUuids.forEach(interactionUuid -> {
            if (!Strings.isNullOrEmpty(rawData.get(interactionUuid).getNextInteractionUuid())) {
                interactionModels.get(interactionUuid).setNext(
                        interactionModels.get(rawData.get(interactionUuid).getNextInteractionUuid())
                );
                resultList.add(interactionModels.get(interactionUuid));
                interactionModels.remove(interactionUuid);

            }
        });

        interactionModels.forEach((key, value) -> resultList.add(value));

        return resultList.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private InteractionModel createInteraction(String relatedMapName,
                                               InteractionDataModel rawData,
                                               List<InteractionPreservationModel> interactionPreservations) {

        InteractionPreservationModel interactionPreservationModel = interactionPreservations
                .stream()
                .filter(interaction -> interaction.getUuid().equals(rawData.getUuid()))
                .findFirst()
                .orElse(null);

        return InteractionModel.builder()
                .uuid(rawData.getUuid())
                .targetUuid(rawData.getTargetUuid())
                .interactionEffects(this.createEffects(rawData.getEffects()))
                .failedPreConditionInteractionEffects(this.createEffects(rawData.getFailedPreConditionEffects()))
                .shiftTiles(rawData.getShiftTiles())
                .isCompleted(interactionPreservationModel != null)
                .preCondition(this.conditionService.build(rawData.getPreCondition()))
                .mapName(relatedMapName)
                .build();
    }

    private InteractionModel createInteractionModelWithChildren(String relatedMapName,
                                                                String rawDataUuid,
                                                                Map<String, InteractionDataModel> rawData,
                                                                List<InteractionPreservationModel> interactionPreservations) {
        if (rawData.isEmpty() || !rawData.containsKey(rawDataUuid)) {
            return null;
        }

        InteractionDataModel rawDataModel = rawData.get(rawDataUuid);
        rawData.remove(rawDataUuid);

        InteractionModel childInteraction = null;
        if (!Strings.isNullOrEmpty(rawDataModel.getNextInteractionUuid())) {
            InteractionDataModel child = rawData.get(rawDataModel.getNextInteractionUuid());

            if (child != null) {
                childInteraction = this.createInteractionModelWithChildren(
                        relatedMapName, child.getUuid(), rawData, interactionPreservations
                );
            }
        }

        InteractionPreservationModel interactionPreservationModel = interactionPreservations
                .stream()
                .filter(interaction -> interaction.getUuid().equals(rawDataModel.getUuid()))
                .findFirst()
                .orElse(null);

        return InteractionModel.builder()
                .uuid(rawDataModel.getUuid())
                .targetUuid(rawDataModel.getTargetUuid())
                .interactionEffects(this.createEffects(rawDataModel.getEffects()))
                .failedPreConditionInteractionEffects(this.createEffects(rawDataModel.getFailedPreConditionEffects()))
                .shiftTiles(rawDataModel.getShiftTiles())
                .isCompleted(interactionPreservationModel != null)
                .preCondition(this.conditionService.build(rawDataModel.getPreCondition()))
                .next(childInteraction)
                .mapName(relatedMapName)
                .build();
    }

    private List<EffectModel> createEffects(List<EffectDataModel> effects) {
        if (effects == null || effects.size() == 0) {
            return Collections.emptyList();
        }

        return effects.stream()
                .map(effect -> {
                    switch (effect.getType()) {
                        case DIALOGUE:
                            DialogueEffectDataModel effectDataModel = ((DialogueEffectDataModel)effect);
                            return new DialogueEffectModel(
                                    effectDataModel.getText(),
                                    new DialogueEffectModel.DialogueSpeaker(
                                            effectDataModel.getSpeakerUuid(), effectDataModel.getSpeakerEmotion()
                                    )
                            );
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
