package com.alta.dao.domain.interaction;

import com.alta.dao.ResourcesLocation;
import com.alta.dao.data.common.effect.EffectDataModel;
import com.alta.dao.data.interaction.InteractionDataModel;
import com.alta.dao.domain.common.effect.EffectDeserializer;
import com.alta.utils.JsonParser;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides the implementation of {@link InteractionService}
 */
@Slf4j
public class InteractionServiceImpl implements InteractionService {

    private final InteractionDeserializer interactionDeserializer;
    private final EffectDeserializer effectDeserializer;
    private List<InteractionEntity> availableInteractions;

    /**
     * Initialize new instance of {@link InteractionServiceImpl}
     */
    @Inject
    public InteractionServiceImpl(InteractionDeserializer interactionDeserializer, EffectDeserializer effectDeserializer) {
        this.interactionDeserializer = interactionDeserializer;
        this.effectDeserializer = effectDeserializer;
        this.loadAvailableInteractions();
    }

    /**
     * Gets the list of interactions that available on specific map.
     *
     * @param relatedMapName - the name of related map.
     * @return the {@link List} of interactions.
     */
    @Override
    public List<InteractionDataModel> getInteractions(String relatedMapName) {
        log.debug("Try to get interaction with related map name '{}'", relatedMapName);
        InteractionEntity matchedInteractionEntity = this.availableInteractions
                .stream()
                .filter(mapEntity -> mapEntity.getName().equalsIgnoreCase(relatedMapName))
                .findFirst()
                .orElse(null);

        if (matchedInteractionEntity == null) {
            log.error("Interaction with given related map name '{}' not found", relatedMapName);
            return null;
        }

        Map<Type, JsonDeserializer> deserializers = new HashMap<>();
        deserializers.put(new TypeToken<ArrayList<InteractionDataModel>>(){}.getType(), this.interactionDeserializer);
        deserializers.put(new TypeToken<ArrayList<EffectDataModel>>(){}.getType(), this.effectDeserializer);

        return JsonParser.parse(
                this.getClass().getClassLoader().getResource(matchedInteractionEntity.getDecoratorPath()).getPath(),
                new TypeToken<ArrayList<InteractionDataModel>>(){}.getType(),
                deserializers
        );
    }

    /**
     * Gets the list of interactions that available for given parameters.
     *
     * @param relatedMapName          - the name of map where interactions are searching.
     * @param targetUuid              - the uuid of target for interaction.
     * @param currentChapterIndicator - the indicator of current character.
     * @return the {@link Map} of found interactions or empty list.
     */
    @Override
    public Map<String, InteractionDataModel> getInteractions(String relatedMapName, String targetUuid, int currentChapterIndicator) {
        List<InteractionDataModel> interactionsForMap = this.getInteractions(relatedMapName);
        if (interactionsForMap == null || interactionsForMap.isEmpty()) {
            log.warn("Interactions related to map name '{}' not found", relatedMapName);
            return Collections.emptyMap();
        }

        return interactionsForMap.stream()
                .filter(interaction -> interaction.getTargetUuid().equals(targetUuid))
                .filter(interaction -> interaction.getChapterIndicatorFrom() != null)
                .filter(interaction -> interaction.getChapterIndicatorTo() != null)
                .filter(interaction -> interaction.getChapterIndicatorFrom() <= currentChapterIndicator)
                .filter(interaction -> interaction.getChapterIndicatorTo() >= currentChapterIndicator)
                .collect(Collectors.toMap(InteractionDataModel::getUuid, i -> i));
    }

    /**
     * Gets the interaction by uuid for given map.
     *
     * @param relatedMapName  - the name of related map.
     * @param interactionUuid - the uuid of interaction.
     * @return the {@link InteractionDataModel} instance or null if not found.
     */
    @Override
    public InteractionDataModel getInteraction(@NonNull String relatedMapName, @NonNull String interactionUuid) {
        List<InteractionDataModel> interactionsForMap = this.getInteractions(relatedMapName);
        if (interactionsForMap == null || interactionsForMap.isEmpty()) {
            log.warn("Interactions related to map name '{}' not found", relatedMapName);
            return null;
        }

        return interactionsForMap.stream()
                .filter(interaction -> interaction.getUuid().equals(interactionUuid))
                .findFirst()
                .orElse(null);
    }

    private void loadAvailableInteractions() {
        try {
            this.availableInteractions = JsonParser.parse(
                    this.getClass().getClassLoader().getResource(ResourcesLocation.INTERACTIONS_DESCRIPTOR_FILE).getPath(),
                    new TypeToken<ArrayList<InteractionEntity>>(){}.getType()
            );
        } catch (NullPointerException e) {
            log.error(e.getMessage());
        }
    }
}
