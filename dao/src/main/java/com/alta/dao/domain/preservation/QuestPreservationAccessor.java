package com.alta.dao.domain.preservation;

import com.alta.dao.data.preservation.QuestPreservationModel;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;

/**
 * Provides the accessor to quest preservation table.
 */
@Accessor
public interface QuestPreservationAccessor {

    /**
     * Finds the quests by given preservation id.
     *
     * @param preservationId    - the preservation id to be used in search.
     * @return the {@link Result} of quests.
     */
    @Query("SELECT * FROM " + QuestPreservationModel.TABLE_NAME +
            " WHERE " + QuestPreservationModel.PRESERVATION_ID_FIELD + "=:pId ALLOW FILTERING")
    Result<QuestPreservationModel> findAllByPreservationId(@Param("pId") int preservationId);

}
