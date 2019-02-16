package com.alta.dao.domain.preservation;

import com.google.inject.Inject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

/**
 * Provides the service to make CRUD operation with preservation
 */
@Slf4j
public class PreservationServiceImpl implements PreservationService {

    private Dao<PreservationModel, Integer> dao;

    /**
     * Initialize new instance of {@link PreservationServiceImpl}
     */
    @Inject
    public PreservationServiceImpl(ConnectionSource connectionSource) {
        try {
            this.dao = DaoManager.createDao(connectionSource, PreservationModel.class);
        } catch (SQLException e) {
            log.error(e.getMessage());
            this.dao = null;
        }
    }

    /**
     * Gets the preservation
     */
    @Override
    public PreservationModel getPreservation() {
        try {
            return this.dao.queryBuilder().queryForFirst();
        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
