package com.alta.dao.domain.actor;

import com.alta.dao.data.actor.ActorFaceSetDescriptorModel;
import com.google.gson.*;

import java.awt.*;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ActorFaceSetDescriptoDeserializer implements JsonDeserializer<ActorFaceSetDescriptorModel> {

    private static final String TILE_WIDTH_FIELD_NAME = "tileWidth";
    private static final String TILE_HEIGHT_FIELD_NAME = "tileHeight";
    private static final String EMOTIONS_FIELD_NAME = "emotions";

    private static final String X_FIELD_NAME = "x";
    private static final String Y_FIELD_NAME = "y";
    private static final String TYPE_FIELD_NAME = "type";

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
    public ActorFaceSetDescriptorModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject faceSetJson = json.getAsJsonObject();
        return ActorFaceSetDescriptorModel.builder()
                .tileWidth(faceSetJson.get(TILE_WIDTH_FIELD_NAME).getAsInt())
                .tileHeight(faceSetJson.get(TILE_HEIGHT_FIELD_NAME).getAsInt())
                .emotions(this.parseEmotions(faceSetJson.getAsJsonArray(EMOTIONS_FIELD_NAME)))
                .build();
    }

    private Map<String, Point> parseEmotions(JsonArray emotions) {
        if (emotions == null || emotions.size() == 0) {
            return Collections.emptyMap();
        }

        Map<String, Point> result = new HashMap<>();
        emotions.forEach(emotion -> {
            JsonObject emotionJson = emotion.getAsJsonObject();

            result.put(
                    emotionJson.get(TYPE_FIELD_NAME).getAsString(),
                    new Point(emotionJson.get(X_FIELD_NAME).getAsInt(), emotionJson.get(Y_FIELD_NAME).getAsInt())
            );
        });
        return result;
    }
}
