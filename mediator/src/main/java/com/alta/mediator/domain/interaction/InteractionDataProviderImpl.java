package com.alta.mediator.domain.interaction;

import com.alta.behaviorprocess.data.interaction.InteractionModel;
import com.alta.dao.data.interaction.InteractionDataModel;
import com.alta.dao.data.preservation.InteractionPreservationModel;
import com.alta.dao.domain.interaction.InteractionService;
import com.alta.mediator.domain.effect.EffectDataProvider;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Describes the provider of model related to interactions.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class InteractionDataProviderImpl implements InteractionDataProvider {

    private final InteractionService interactionService;
    private final InteractionConditionService conditionService;
    private final EffectDataProvider effectDataProvider;

    /**
     * Gets the interaction model by given related map name.
     *
     * @param relatedMapName           - the name of map where interaction should be happens.
     * @param targetUuid               - the uuid of target for interaction.
     * @param currentChapterIndicator  - the indicator of current character.
     * @param interactionPreservations - the current preservation model.
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

    /**
     * Gets the interactions by given related map name.
     *
     * @param relatedMapName           - the name of map where interaction should be happens.
     * @param currentChapterIndicator  - the indicator of current character.
     * @param interactionPreservations - the current preservation model.
     * @return the {@link List<InteractionModel>} instance.
     */
    @Override
    public List<InteractionModel> getInteractionsByRelatedMapName(String relatedMapName,
                                                                  int currentChapterIndicator,
                                                                  List<InteractionPreservationModel> interactionPreservations) {
        List<InteractionDataModel> interactions = this.interactionService.getInteractions(
                relatedMapName
        );

        if (interactions == null || interactions.size() == 0) {
            log.debug("Interactions for given map '{}' not found", relatedMapName);
            return Collections.emptyList();
        }

        Map<String, InteractionDataModel> interactionsAsMap = interactions.stream()
                .filter(interaction -> interaction.getChapterIndicatorFrom() != null)
                .filter(interaction -> interaction.getChapterIndicatorTo() != null)
                .filter(interaction -> interaction.getChapterIndicatorFrom() <= currentChapterIndicator)
                .filter(interaction -> interaction.getChapterIndicatorTo() >= currentChapterIndicator)
                .collect(Collectors.toMap(InteractionDataModel::getUuid, i -> i));

        log.debug("{} interactions found for '{}' map", interactions.size(), relatedMapName);

        return this.createInteractions(relatedMapName, interactionsAsMap, interactionPreservations);
    }

    private List<InteractionModel> createInteractions(String relatedMapName,
                                                      Map<String, InteractionDataModel> rawData,
                                                      List<InteractionPreservationModel> interactionPreservations) {
        List<String> interactionUuids = new ArrayList<>(rawData.keySet());

        Map<String, InteractionModel> interactionModels = rawData.entrySet().stream()
                .map(es -> this.createInteraction(relatedMapName, es.getValue(), interactionPreservations))
                .collect(Collectors.toMap(InteractionModel::getUuid, interaction -> interaction));

        interactionUuids.forEach(interactionUuid -> {
            if (!Strings.isNullOrEmpty(rawData.get(interactionUuid).getNextInteractionUuid())) {
                interactionModels.get(interactionUuid).setNext(
                        interactionModels.get(rawData.get(interactionUuid).getNextInteractionUuid())
                );
                // resultList.add(interactionModels.get(interactionUuid));
                // interactionModels.remove(interactionUuid);
            }
        });

        List<InteractionModel> resultList = new ArrayList<>();
        interactionModels.forEach((key, value) -> {
            boolean exists = interactionModels.values().stream()
                    .anyMatch(i -> i.getNext() != null && i.getNext().getUuid().equals(key));

            if (!exists) {
                resultList.add(value);
            }
        });

        return resultList.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private InteractionModel createInteraction(String relatedMapName,
                                               InteractionDataModel rawData,
                                               List<InteractionPreservationModel> interactionPreservations) {

        InteractionPreservationModel interactionPreservationModel = interactionPreservations
                .stream()
                .filter(interaction -> interaction.getInteractionUuid().toString().equals(rawData.getUuid()))
                .findFirst()
                .orElse(null);

        return InteractionModel.builder()
                .uuid(rawData.getUuid())
                .targetUuid(rawData.getTargetUuid())
                .interactionEffects(this.effectDataProvider.getEffects(rawData.getEffects()))
                .failedPreConditionInteractionEffects(this.effectDataProvider.getEffects(rawData.getFailedPreConditionEffects()))
                .shiftTiles(rawData.getShiftTiles())
                .isCompleted(interactionPreservationModel != null)
                .preCondition(this.conditionService.build(rawData.getPreCondition()))
                .mapName(relatedMapName)
                .build();
    }
}
