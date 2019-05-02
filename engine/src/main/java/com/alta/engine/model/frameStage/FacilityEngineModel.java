package com.alta.engine.model.frameStage;

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

    private final String uuid;
    private final String pathToImageSet;
    private final int startX;
    private final int startY;
    private final int tileWidth;
    private final int tileHeight;
    private final List<Position> positions;
    private boolean visible;

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
