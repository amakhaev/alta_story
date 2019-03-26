package com.alta.engine.data;

import com.alta.computator.model.altitudeMap.TileState;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * Provides the model that describes map
 */
@Data
@Builder
public class FacilityEngineModel {

    private UUID uuid;
    private String pathToImageSet;
    private int startX;
    private int startY;
    private int tileWidth;
    private int tileHeight;
    private List<Position> positions;

    @Getter
    @Setter
    @Builder
    public static class Position {
        private int x;
        private int y;
        private int shiftFromStartX;
        private int shiftFromStartY;
        private TileState tileState;
        private int zIndex;
    }
}
