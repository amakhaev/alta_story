package com.alta.dao.di;

import com.alta.dao.ResourcesLocation;
import com.google.inject.Provider;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
public class DatabaseConnectionProvider implements Provider<ConnectionSource> {

    private ConnectionSource connectionSource;

    /**
     * Initialize ew instance of DatabaseConnectionProvider
     */
    public DatabaseConnectionProvider() {
        this.connect();
    }

    /**
     * Provides an instance of {@code T}.
     */
    @Override
    public ConnectionSource get() {
        return this.connectionSource;
    }

    private void connect() {
        try {
            String url = "jdbc:sqlite:" + ResourcesLocation.DATABASE_NAME;
            this.connectionSource = new JdbcConnectionSource(url);
            log.info("Connection to SQLite has been established.");

        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }
}
