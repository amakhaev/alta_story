package com.alta.mediator.domain.map;

import com.alta.computator.model.altitudeMap.TileState;
import com.alta.dao.data.facility.FacilityPositionModel;
import com.alta.dao.data.facility.FacilityTileType;
import com.alta.dao.data.map.MapFacilityModel;
import com.alta.engine.model.frameStage.FacilityEngineModel;
import com.google.inject.Singleton;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides the mapper for {@link FacilityEngineModel}
 */
@Singleton
public class FacilityEngineModelMapper {

    /**
     * Maps the {@link MapFacilityModel} instance to {@link FacilityEngineModel}
     *
     * @param mapFacilityModels - the models that should be mapped
     * @return mapper collection of {@link FacilityEngineModel}
     */
    public List<FacilityEngineModel> doMapppingForFacilities(List<MapFacilityModel> mapFacilityModels) {
        if (mapFacilityModels == null || mapFacilityModels.isEmpty()) {
            return Collections.emptyList();
        }

        return mapFacilityModels.stream().map(this::doMappingForFacility).collect(Collectors.toList());
    }

    private List<FacilityEngineModel.Position> doMappingForPositions(List<FacilityPositionModel> mapFacilityPositions) {
        if (mapFacilityPositions == null || mapFacilityPositions.isEmpty()) {
            return Collections.emptyList();
        }

        return mapFacilityPositions.stream().map(this::doMappingForPosition).collect(Collectors.toList());
    }

    private FacilityEngineModel doMappingForFacility(MapFacilityModel facilityModel) {
        return FacilityEngineModel.builder()
                .uuid(facilityModel.getUuid())
                .pathToImageSet(facilityModel.getPathToImageSet())
                .tileWidth(facilityModel.getTileWidth())
                .tileHeight(facilityModel.getTileHeight())
                .startX(facilityModel.getStartX())
                .startY(facilityModel.getStartY())
                .visible(facilityModel.isDefaultVisible())
                .positions(this.doMappingForPositions(facilityModel.getFacilityPositions()))
                .build();
    }

    private FacilityEngineModel.Position doMappingForPosition(FacilityPositionModel mapFacilityPosition) {
        return FacilityEngineModel.Position.builder()
                .x(mapFacilityPosition.getX())
                .y(mapFacilityPosition.getY())
                .shiftFromStartX(mapFacilityPosition.getShiftFromStartX())
                .shiftFromStartY(mapFacilityPosition.getShiftFromStartY())
                .zIndex(mapFacilityPosition.getZIndex())
                .tileState(this.mapTileState(mapFacilityPosition.getTileType()))
                .build();
    }

    private TileState mapTileState(FacilityTileType tileType) {
        switch (tileType) {
            case FREE:
                return TileState.FREE;
            case BARRIER:
                return TileState.BARRIER;
            case JUMP:
                return TileState.JUMP;
            default:
                return TileState.FREE;
        }
    }
}
