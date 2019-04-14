package com.alta.mediator.domain.interaction;

import com.alta.dao.data.interaction.DialogueEffectModel;
import com.alta.dao.data.interaction.InteractionEffectModel;
import com.alta.dao.data.interaction.InteractionModel;
import com.alta.dao.domain.interaction.InteractionService;
import com.alta.engine.model.InteractionDataModel;
import com.alta.engine.model.interaction.DialogueEffectEngineModel;
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
     * @param relatedMapName          - the name of map where interaction should be happens.
     * @param currentChapterIndicator - the indicator of current chapter.
     * @return the {@link InteractionDataModel} instance.
     */
    @Override
    public InteractionDataModel getInteractionByRelatedMapName(@NonNull String relatedMapName, int currentChapterIndicator) {
        List<InteractionModel> interactions = this.interactionService.getInteractions(relatedMapName);
        if (interactions == null || interactions.size() == 0) {
            log.debug("Interactions for given map '{}' not found", relatedMapName);
            return null;
        }

        log.debug("{} interactions found for '{}' map", interactions.size(), relatedMapName);

        List<InteractionEngineModel> interactionEngineModels = interactions.stream()
                .filter(interactionModel ->
                        interactionModel.getChapterIndicatorFrom() != null && interactionModel.getChapterIndicatorTo() != null
                )
                .filter(interactionModel ->
                        currentChapterIndicator >= interactionModel.getChapterIndicatorFrom() &&
                        currentChapterIndicator <= interactionModel.getChapterIndicatorTo()
                )
                .map(interactionModel -> this.createInteractionModelWithChildren(interactionModel, interactions))
                .collect(Collectors.toList());

        log.info("{} interactions found for '{}' map.", interactions.size(), relatedMapName);

        return InteractionDataModel.builder().interactions(interactionEngineModels).build();
    }

    private InteractionEngineModel createInteractionModelWithChildren(InteractionModel parent, List<InteractionModel> sources) {
        InteractionEngineModel childInteraction = null;
        if (!Strings.isNullOrEmpty(parent.getNextInteractionUuid())) {
            InteractionModel child = sources.stream()
                    .filter(s -> s.getUuid().equals(parent.getNextInteractionUuid()))
                    .findFirst()
                    .orElse(null);

            if (child != null) {
                childInteraction = this.createInteractionModelWithChildren(child, sources);
            }
        }

        return InteractionEngineModel.builder()
                .uuid(parent.getUuid())
                .targetUuid(parent.getTargetUuid())
                .showOnce(parent.isShowOnce())
                .interactionEffects(this.createEffects(parent.getEffects()))
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
                        default:
                            throw new IllegalArgumentException("Unknown type of interaction effect: " + effect.getType());
                    }
                })
                .collect(Collectors.toList());
    }
}
