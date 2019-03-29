package com.alta.dao.data.map;

import com.alta.dao.domain.map.MapJumpingEntity;
import com.alta.dao.domain.map.MapJumpingEntity;
import com.alta.dao.domain.map.SimpleNpcEntity;
import lombok.*;

import java.util.List;

/**
 * Provides the model that describes the map
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

    @Singular("mapJumpings")
    private List<MapJumpingEntity> mapJumpings;

}
