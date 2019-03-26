package com.alta.mediator.domain.map;

import com.alta.dao.domain.map.MapJumpingEntity;
import com.alta.engine.data.JumpingEngineModel;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides the model mapper for jumping point.
 */
public class JumpingEngineModelMapper {

    /**
     * Maps the list of {@link MapJumpingEntity} to list of {@link JumpingEngineModel}.
     *
     * @param mapJumpingEntities - the entities to be mapped
     * @return mapped {@link JumpingEngineModel} instances
     */
    public List<JumpingEngineModel> doMappingForJumpings(List<MapJumpingEntity> mapJumpingEntities) {
        if (mapJumpingEntities == null) {
            return null;
        }

        return mapJumpingEntities.parallelStream().map(this::doMappingForJumping).collect(Collectors.toList());
    }

    private JumpingEngineModel doMappingForJumping(MapJumpingEntity mapJumpingEntity) {
        if (mapJumpingEntity == null) {
            return null;
        }

        return JumpingEngineModel.builder()
                .from(new Point(mapJumpingEntity.getFromX(), mapJumpingEntity.getFromY()))
                .mapName(mapJumpingEntity.getMapName())
                .to(new Point(mapJumpingEntity.getToX(), mapJumpingEntity.getToY()))
                .build();
    }

}
