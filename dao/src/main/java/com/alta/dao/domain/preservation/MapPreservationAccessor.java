package com.alta.dao.domain.preservation;

import com.alta.dao.data.preservation.MapPreservationModel;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;

/**
 * Provides the accessor to map preservation table.
 */
@Accessor
public interface MapPreservationAccessor {

    /**
     * Finds the maps by given preservation id and map name.
     *
     * @param preservationId    - the preservation id to be used in search.
     * @param mapName           - the map name to be used in search.
     * @return the {@link Result} of maps.
     */
    @Query("SELECT * FROM " + MapPreservationModel.TABLE_NAME +
            " WHERE " + MapPreservationModel.PRESERVATION_FIELD + "=:pId AND " +
            MapPreservationModel.MAP_NAME_FIELD + "=:mapName ALLOW FILTERING")
    Result<MapPreservationModel> findAllByPreservationIdAndMapName(@Param("pId") int preservationId,
                                                                   @Param("mapName") String mapName);

    /**
     * Finds the maps by given preservation id.
     *
     * @param preservationId    - the preservation id to be used in search.
     * @return the {@link Result} of maps.
     */
    @Query("SELECT * FROM " + MapPreservationModel.TABLE_NAME +
            " WHERE " + MapPreservationModel.PRESERVATION_FIELD + "=:pId ALLOW FILTERING")
    Result<MapPreservationModel> findAllByPreservationId(@Param("pId") int preservationId);

}
