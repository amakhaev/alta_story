package com.alta.dao.domain.actor;

import com.alta.dao.data.actor.ActorDirectionModel;
import com.alta.dao.data.actor.ActorTileSetDescriptorModel;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActorTileSetDescriptorDeserializer implements JsonDeserializer<ActorTileSetDescriptorModel> {

    private static final String TILE_WIDTH_FIELD_NAME = "tileWidth";
    private static final String TILE_HEIGHT_FIELD_NAME = "tileHeight";
    private static final String DIRECTION_DOWN_FIELD_NAME = "directionDown";
    private static final String DIRECTION_UP_FIELD_NAME = "directionUp";
    private static final String DIRECTION_LEFT_FIELD_NAME = "directionLeft";
    private static final String DIRECTION_RIGHT_FIELD_NAME = "directionRight";

    private static final String X_FIELD_NAME = "x";
    private static final String Y_FIELD_NAME = "y";
    private static final String STOP_FRAME_FIELD_NAME = "stopFrame";

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
    public ActorTileSetDescriptorModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject tileSetJson = json.getAsJsonObject();

        return ActorTileSetDescriptorModel.builder()
                .tileWidth(tileSetJson.get(TILE_WIDTH_FIELD_NAME).getAsInt())
                .tileHeight(tileSetJson.get(TILE_HEIGHT_FIELD_NAME).getAsInt())
                .directionDown(this.parseDirections(tileSetJson.getAsJsonArray(DIRECTION_DOWN_FIELD_NAME)))
                .directionLeft(this.parseDirections(tileSetJson.getAsJsonArray(DIRECTION_LEFT_FIELD_NAME)))
                .directionRight(this.parseDirections(tileSetJson.getAsJsonArray(DIRECTION_RIGHT_FIELD_NAME)))
                .directionUp(this.parseDirections(tileSetJson.getAsJsonArray(DIRECTION_UP_FIELD_NAME)))
                .build();
    }

    private List<ActorDirectionModel> parseDirections(JsonArray points) {
        if (points == null || points.size() == 0) {
            return Collections.emptyList();
        }

        List<ActorDirectionModel> result = new ArrayList<>();
        points.forEach(point -> result.add(this.parseDirection(point.getAsJsonObject())));
        return result;
    }

    private ActorDirectionModel parseDirection(JsonObject point) {
        return new ActorDirectionModel(
                point.get(X_FIELD_NAME).getAsInt(),
                point.get(Y_FIELD_NAME).getAsInt(),
                point.get(STOP_FRAME_FIELD_NAME).getAsBoolean()
        );
    }
}
