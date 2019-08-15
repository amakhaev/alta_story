package com.alta.dao.domain.common.effect;

import com.alta.dao.data.common.effect.background.IncrementChapterIndicatorDataModel;
import com.alta.dao.data.common.effect.visible.DialogueEffectDataModel;
import com.alta.dao.data.common.effect.EffectDataModel;
import com.alta.dao.data.common.effect.visible.HideFacilityEffectDataModel;
import com.alta.dao.data.common.effect.visible.RouteMovementEffectDataModel;
import com.alta.dao.data.common.effect.visible.ShowFacilityEffectDataModel;
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

    private static final String TARGET_UUID_FIELD_NAME = "targetUuid";
    private static final String X_FIELD_NAME = "x";
    private static final String Y_FIELD_NAME = "y";
    private static final String FINAL_DIRECTION_FIELD_NAME = "finalDirection";
    private static final String MOVEMENT_SPEED_FIELD_NAME = "movementSpeed";

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

            EffectDataModel.EffectType type =
                    EffectDataModel.EffectType.valueOf(item.get(TYPE_FIELD_NAME).getAsString());

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
                case ROUTE_MOVEMENT:
                    model = this.parseRouteMovementEffect(item);
                    break;
                case INCREMENT_CHAPTER_INDICATOR:
                    model = this.parseIncrementChapterEffect();
                    break;
                default:
                    log.error("Unknown type of effect {}.", type);
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

    private RouteMovementEffectDataModel parseRouteMovementEffect(JsonObject jsonRouteMovementEffect) {
        try {
            return RouteMovementEffectDataModel.builder()
                    .targetUuid(jsonRouteMovementEffect.get(TARGET_UUID_FIELD_NAME).getAsString())
                    .finalDirection(jsonRouteMovementEffect.get(FINAL_DIRECTION_FIELD_NAME).getAsString())
                    .movementSpeed(jsonRouteMovementEffect.get(MOVEMENT_SPEED_FIELD_NAME).getAsString())
                    .x(jsonRouteMovementEffect.get(X_FIELD_NAME).getAsInt())
                    .y(jsonRouteMovementEffect.get(Y_FIELD_NAME).getAsInt())
                    .build();
        } catch (Exception e) {
            log.error("Parsing of RouteMovementEffectDataModel failed with error", e);
            return null;
        }
    }

    private IncrementChapterIndicatorDataModel parseIncrementChapterEffect() {
        try {
            return new IncrementChapterIndicatorDataModel();
        } catch (Exception e) {
            log.error("Parsing of IncrementChapterIndicatorDataModel was failed with error", e);
            return null;
        }
    }
}
