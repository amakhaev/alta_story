package com.alta.dao.domain.preservation;

import com.alta.dao.data.preservation.InteractionPreservationModel;
import com.alta.dao.data.preservation.MapPreservationModel;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;

/**
 * Provides the accessor to interaction preservation table.
 */
@Accessor
public interface InteractionPreservationAccessor {

    /**
     * Finds the interactions by given preservation id and map name.
     *
     * @param preservationId    - the preservation id to be used in search.
     * @param mapName           - the map name to be used in search.
     * @return the {@link Result} of maps.
     */
    @Query("SELECT * FROM " + InteractionPreservationModel.TABLE_NAME +
            " WHERE " + InteractionPreservationModel.PRESERVATION_FIELD + "=:pId AND " +
            InteractionPreservationModel.MAP_NAME_FIELD + "=:mapName ALLOW FILTERING")
    Result<InteractionPreservationModel> findAllByPreservationIdAndMapName(@Param("pId") int preservationId,
                                                                           @Param("mapName") String mapName);

    /**
     * Finds the interactions by given preservation id.
     *
     * @param preservationId    - the preservation id to be used in search.
     * @return the {@link Result} of maps.
     */
    @Query("SELECT * FROM " + InteractionPreservationModel.TABLE_NAME +
            " WHERE " + InteractionPreservationModel.PRESERVATION_FIELD + "=:pId ALLOW FILTERING")
    Result<InteractionPreservationModel> findAllByPreservationId(@Param("pId") int preservationId);

}
