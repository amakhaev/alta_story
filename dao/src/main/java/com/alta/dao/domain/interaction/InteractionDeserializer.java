package com.alta.dao.domain.interaction;

import com.alta.dao.data.interaction.*;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides custom deserializer of interaction.
 */
@Slf4j
public class InteractionDeserializer implements JsonDeserializer<List<InteractionModel>> {

    private static final String UUID_FIELD_NAME = "uuid";
    private static final String TARGET_UUID_FIELD_NAME = "targetUuid";
    private static final String CHAPTER_INDICATOR_FROM_FIELD_NAME = "chapterIndicatorFrom";
    private static final String CHAPTER_INDICATOR_TO_FIELD_NAME = "chapterIndicatorTo";
    private static final String NEXT_INTERACTION_UUID_FIELD_NAME = "nextInteractionUuid";
    private static final String SHIFT_TILE_X_FIELD_NAME = "shiftTileX";
    private static final String SHIFT_TILE_Y_FIELD_NAME = "shiftTileY";

    private static final String EFFECTS_FIELD_NAME = "effects";
    private static final String TYPE_FIELD_NAME = "type";
    private static final String TEXT_FIELD_NAME = "text";
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
     * @param json    The Json data being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @param context
     * @return a deserialized object of the specified type typeOfT which is a subclass of {@code T}
     * @throws JsonParseException if json is not in the expected format of {@code typeofT}
     */
    @Override
    public List<InteractionModel> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray jsonInteractions = json.getAsJsonArray();

        List<InteractionModel> result = new ArrayList<>();
        jsonInteractions.forEach(jsonInteractionItem -> {
            JsonObject item = jsonInteractionItem.getAsJsonObject();

            InteractionModel interactionModel = InteractionModel.builder()
                    .uuid(item.get(UUID_FIELD_NAME).getAsString())
                    .targetUuid(item.get(TARGET_UUID_FIELD_NAME).getAsString())
                    .chapterIndicatorFrom(item.has(CHAPTER_INDICATOR_FROM_FIELD_NAME) ?
                            item.get(CHAPTER_INDICATOR_FROM_FIELD_NAME).getAsInt() : null
                    )
                    .chapterIndicatorTo(item.has(CHAPTER_INDICATOR_TO_FIELD_NAME) ?
                            item.get(CHAPTER_INDICATOR_TO_FIELD_NAME).getAsInt() : null
                    )
                    .shiftTileX(item.has(SHIFT_TILE_X_FIELD_NAME) ?
                            item.get(SHIFT_TILE_X_FIELD_NAME).getAsInt() : null
                    )
                    .shiftTileY(item.has(SHIFT_TILE_Y_FIELD_NAME) ?
                            item.get(SHIFT_TILE_Y_FIELD_NAME).getAsInt() : null
                    )
                    .nextInteractionUuid(item.has(NEXT_INTERACTION_UUID_FIELD_NAME) ?
                            item.get(NEXT_INTERACTION_UUID_FIELD_NAME).getAsString() : null
                    )
                    .effects(this.parseEffects(item.getAsJsonArray(EFFECTS_FIELD_NAME)))
                    .build();
            result.add(interactionModel);
        });

        return result;
    }

    private List<InteractionEffectModel> parseEffects(JsonArray interactionEffects) {
        if (interactionEffects == null || interactionEffects.size() == 0) {
            return Collections.emptyList();
        }

        List<InteractionEffectModel> result = new ArrayList<>();
        interactionEffects.forEach(jsonInteractionEffectItem -> {
            JsonObject item = jsonInteractionEffectItem.getAsJsonObject();

            InteractionEffectModel.InteractionEffectType type =
                    InteractionEffectModel.InteractionEffectType.valueOf(item.get(TYPE_FIELD_NAME).getAsString());

            InteractionEffectModel model = null;
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

    private DialogueEffectModel parseDialogueEffect(JsonObject jsonDialogueModel) {
        try {
            DialogueEffectModel model = new DialogueEffectModel();
            model.setText(jsonDialogueModel.get(TEXT_FIELD_NAME).getAsString());
            return model;
        } catch (Exception e) {
            log.error("Parsing of DialogueEffectModel was failed with error: {}", e.getMessage());
            return null;
        }
    }

    private HideFacilityEffectModel parseHideFacilityEffect(JsonObject jsonHideFacilityEffect) {
        try {
            HideFacilityEffectModel model = new HideFacilityEffectModel();
            model.setFacilityUuid(jsonHideFacilityEffect.get(FACILITY_UUID_FIELD_NAME).getAsString());
            return model;
        } catch (Exception e) {
            log.error("Parsing of HideFacilityEffectModel was failed with error: {}", e.getMessage());
            return null;
        }
    }

    private ShowFacilityEffectModel parseShowFacilityEffect(JsonObject jsonHideFacilityEffect) {
        try {
            ShowFacilityEffectModel model = new ShowFacilityEffectModel();
            model.setFacilityUuid(jsonHideFacilityEffect.get(FACILITY_UUID_FIELD_NAME).getAsString());
            return model;
        } catch (Exception e) {
            log.error("Parsing of ShowFacilityEffectModel was failed with error: {}", e.getMessage());
            return null;
        }
    }
}
