package com.alta.dao.domain.interaction;

import com.alta.dao.ResourcesLocation;
import com.alta.dao.data.interaction.InteractionModel;
import com.alta.utils.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides the implementation of {@link InteractionService}
 */
@Slf4j
public class InteractionServiceImpl implements InteractionService {

    private final InteractionDeserializer interactionDeserializer;
    private List<InteractionEntity> availableInteractions;

    /**
     * Initialize new instance of {@link InteractionServiceImpl}
     */
    @Inject
    public InteractionServiceImpl(InteractionDeserializer interactionDeserializer) {
        this.interactionDeserializer = interactionDeserializer;
        this.loadAvailableInteractions();
    }

    /**
     * Gets the list of interactions that available on specific map.
     *
     * @param relatedMapName - the name of related map.
     * @return the {@link List} of interactions.
     */
    @Override
    public List<InteractionModel> getInteractions(String relatedMapName) {
        log.info("Try to get interaction with related map name '{}'", relatedMapName);
        InteractionEntity matchedInteractionEntity = this.availableInteractions
                .stream()
                .filter(mapEntity -> mapEntity.getName().equalsIgnoreCase(relatedMapName))
                .findFirst()
                .orElse(null);

        if (matchedInteractionEntity == null) {
            log.error("Interaction with given related map name '{}' not found", relatedMapName);
            return null;
        }

        return JsonParser.parse(
                this.getClass().getClassLoader().getResource(matchedInteractionEntity.getDecoratorPath()).getPath(),
                new TypeToken<ArrayList<InteractionModel>>(){}.getType(),
                this.interactionDeserializer
        );
    }

    /**
     * Gets the interaction by uuid for given map.
     *
     * @param relatedMapName  - the name of related map.
     * @param interactionUuid - the uuid of interaction.
     * @return the {@link InteractionModel} instance or null if not found.
     */
    @Override
    public InteractionModel getInteraction(@NonNull String relatedMapName, @NonNull String interactionUuid) {
        List<InteractionModel> interactionsForMap = this.getInteractions(relatedMapName);
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
