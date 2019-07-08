package com.alta.dao.domain.common.effect;

import com.alta.dao.data.common.effect.DialogueEffectDataModel;
import com.alta.dao.data.common.effect.EffectDataModel;
import com.alta.dao.data.common.effect.HideFacilityEffectDataModel;
import com.alta.dao.data.common.effect.ShowFacilityEffectDataModel;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class EffectDeserializer implements JsonDeserializer<List<EffectDataModel>> {

    private static final String TYPE_FIELD_NAME = "type";
    private static final String TEXT_FIELD_NAME = "text";
    private static final String SPEAKER_UUID_FIELD_NAME = "speakerUuid";
    private static final String SPEAKER_EMOTION_FIELD_NAME = "speakerEmotion";
    private static final String SPEAKER_NAME_FIELD_NAME = "speakerName";
    private static final String FACILITY_UUID_FIELD_NAME = "facilityUuid";

    /**
     * Gson invokes this call-back method during deserialization when it encounters a field of the
     * specified type.
     * <p>In the implementation of this call-back method, you should consider invoking
     * {@link JsonDeserializationContext#deserialize(JsonElement, Type)} method to create objects
     * for any non-trivial field of the returned object. However, you should never invoke it on the
     * the same type passing {@code json} since that will cause an infinite loop (Gson will call your
     * call-back method again).
     *
     * @param json    The Json model being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @param context
     * @return a deserialized object of the specified type typeOfT which is a subclass of {@code T}
     * @throws JsonParseException if json is not in the expected format of {@code typeofT}
     */
    @Override
    public List<EffectDataModel> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray jsonEffects = json.getAsJsonArray();
        return this.parseEffects(jsonEffects);
    }

    private List<EffectDataModel> parseEffects(JsonArray effects) {
        if (effects == null || effects.size() == 0) {
            return Collections.emptyList();
        }

        List<EffectDataModel> result = new ArrayList<>();
        effects.forEach(jsonInteractionEffectItem -> {
            JsonObject item = jsonInteractionEffectItem.getAsJsonObject();

            EffectDataModel.InteractionEffectType type =
                    EffectDataModel.InteractionEffectType.valueOf(item.get(TYPE_FIELD_NAME).getAsString());

            EffectDataModel model = null;
            switch (type) {
                case DIALOGUE:
                    model = this.parseDialogueEffect(item);
                    break;
                case HIDE_FACILITY:
                    model = this.parseHideFacilityEffect(item);
                    break;
                case SHOW_FACILITY:
                    model = this.parseShowFacilityEffect(item);
                    break;
                default:
                    log.error("Unknown type of interaction effect {}", type);
            }

            if (model != null) {
                result.add(model);
            }
        });

        return result;
    }

    private DialogueEffectDataModel parseDialogueEffect(JsonObject jsonDialogueModel) {
        try {
            return DialogueEffectDataModel.builder()
                    .text(jsonDialogueModel.get(TEXT_FIELD_NAME).getAsString())
                    .speakerUuid(
                            jsonDialogueModel.has(SPEAKER_UUID_FIELD_NAME) ?
                                    jsonDialogueModel.get(SPEAKER_UUID_FIELD_NAME).getAsString() : null
                    )
                    .speakerEmotion(
                            jsonDialogueModel.has(SPEAKER_EMOTION_FIELD_NAME) ?
                                    jsonDialogueModel.get(SPEAKER_EMOTION_FIELD_NAME).getAsString() : null
                    )
                    .speakerName(
                            jsonDialogueModel.has(SPEAKER_NAME_FIELD_NAME) ?
                                    jsonDialogueModel.get(SPEAKER_NAME_FIELD_NAME).getAsString() : null
                    )
                    .build();
        } catch (Exception e) {
            log.error("Parsing of DialogueEffectDataModel was failed with error: {}", e.getMessage());
            return null;
        }
    }

    private HideFacilityEffectDataModel parseHideFacilityEffect(JsonObject jsonHideFacilityEffect) {
        try {
            HideFacilityEffectDataModel model = new HideFacilityEffectDataModel();
            model.setFacilityUuid(jsonHideFacilityEffect.get(FACILITY_UUID_FIELD_NAME).getAsString());
            return model;
        } catch (Exception e) {
            log.error("Parsing of HideFacilityEffectDataModel was failed with error: {}", e.getMessage());
            return null;
        }
    }

    private ShowFacilityEffectDataModel parseShowFacilityEffect(JsonObject jsonHideFacilityEffect) {
        try {
            ShowFacilityEffectDataModel model = new ShowFacilityEffectDataModel();
            model.setFacilityUuid(jsonHideFacilityEffect.get(FACILITY_UUID_FIELD_NAME).getAsString());
            return model;
        } catch (Exception e) {
            log.error("Parsing of ShowFacilityEffectDataModel was failed with error: {}", e.getMessage());
            return null;
        }
    }
}
