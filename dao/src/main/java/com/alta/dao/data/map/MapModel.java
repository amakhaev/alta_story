package com.alta.dao.data.map;

import com.alta.dao.domain.map.internalEntities.AlterableNpcEntity;
import com.alta.dao.domain.map.internalEntities.MapJumpingEntity;
import com.alta.dao.domain.map.internalEntities.SimpleNpcEntity;
import lombok.*;

import java.util.List;

/**
 * Provides the data that describes the map
 */
@Getter
@Builder
public class MapModel {

    private String name;
    private String tiledMapAbsolutePath;
    private String displayName;

    @Singular("facilities")
    private List<MapFacilityModel> facilities;

    @Singular("simpleNpcList")
    private List<SimpleNpcEntity> simpleNpcList;

    @Singular("alterableNpcList")
    private List<AlterableNpcEntity> alterableNpcList;

    @Singular("mapJumpings")
    private List<MapJumpingEntity> mapJumpings;

}
