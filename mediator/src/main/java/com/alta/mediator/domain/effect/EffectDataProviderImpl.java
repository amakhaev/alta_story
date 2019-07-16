package com.alta.mediator.domain.effect;

import com.alta.behaviorprocess.data.common.FaceSetDescription;
import com.alta.behaviorprocess.data.effect.DialogueEffectModel;
import com.alta.behaviorprocess.data.effect.EffectModel;
import com.alta.behaviorprocess.data.effect.HideFacilityEffectModel;
import com.alta.behaviorprocess.data.effect.ShowFacilityEffectModel;
import com.alta.dao.data.actor.ActorModel;
import com.alta.dao.data.common.effect.visible.DialogueEffectDataModel;
import com.alta.dao.data.common.effect.EffectDataModel;
import com.alta.dao.data.common.effect.visible.HideFacilityEffectDataModel;
import com.alta.dao.data.common.effect.visible.ShowFacilityEffectDataModel;
import com.alta.dao.domain.actor.ActorService;
import com.alta.mediator.domain.actor.ActorEngineMapper;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Describes the provider of model related to effects.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class EffectDataProviderImpl implements EffectDataProvider {

    private final ActorService actorService;
    private final ActorEngineMapper actorEngineMapper;

    /**
     * Gets effects from DAO models.
     *
     * @param effects - the effects to be converted.
     * @return the {@link List} of effects.
     */
    @Override
    public List<EffectModel> getEffects(List<EffectDataModel> effects) {
        if (effects == null || effects.size() == 0) {
            return Collections.emptyList();
        }

        return effects.stream()
                .map(effect -> {
                    switch (effect.getType()) {
                        case DIALOGUE:
                            return this.createDialogueEffectDataModel(((DialogueEffectDataModel)effect));
                        case HIDE_FACILITY:
                            return new HideFacilityEffectModel(((HideFacilityEffectDataModel)effect).getFacilityUuid());
                        case SHOW_FACILITY:
                            return new ShowFacilityEffectModel(((ShowFacilityEffectDataModel)effect).getFacilityUuid());
                        default:
                            throw new IllegalArgumentException("Unknown type of effect: " + effect.getType());
                    }
                })
                .collect(Collectors.toList());
    }

    private DialogueEffectModel createDialogueEffectDataModel(DialogueEffectDataModel effectDataModel) {
        FaceSetDescription faceSetDescription = null;
        if (!Strings.isNullOrEmpty(effectDataModel.getSpeakerName())) {
            ActorModel actorModel = this.actorService.getActorModel(effectDataModel.getSpeakerName());
            if (actorModel == null) {
                log.debug("Actor with given name not found {}", effectDataModel.getSpeakerName());
                return null;
            }

            faceSetDescription = this.actorEngineMapper.doMappingForFaceSetDescriptor(actorModel);
        }

        return new DialogueEffectModel(
                effectDataModel.getText(),
                new DialogueEffectModel.DialogueSpeaker(
                        effectDataModel.getSpeakerUuid(),
                        effectDataModel.getSpeakerEmotion(),
                        faceSetDescription
                )
        );
    }
}
