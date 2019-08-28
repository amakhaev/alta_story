package com.alta.dao.configuration;

import com.alta.utils.YamlParser;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.net.URL;

/**
 * Provides the connector to cassandra database.
 */
@Slf4j
public class CassandraConnector {

    private static final String FILE_NAME = "dao.yaml";

    private final String host;
    private final int port;
    private Cluster cluster;
    private Session session;

    @Inject
    public CassandraConnector() {
        URL daoUrl = this.getClass().getClassLoader().getResource(FILE_NAME);
        if (daoUrl == null) {
            throw new RuntimeException("Config with given name " + FILE_NAME + " not found.");
        }

        DaoConfig daoConfig = YamlParser.parse(daoUrl.getPath(), DaoConfig.class);
        this.host = daoConfig.getDbCon().getHost();
        this.port = daoConfig.getDbCon().getPort();
    }

    /**
     * Gets the connection to the cassandra session.
     *
     * @return the {@link Session} instance.
     */
    public Session getSession() {
        if (this.session == null || this.session.isClosed()) {
            this.connect();
        }

        return this.session;
    }

    /**
     * Closes the connection to cassandra database.
     */
    public void close() {
        this.session.close();
        this.cluster.close();
    }

    private void connect() {
        log.info("Connect to cassandra database using {}:{}", this.host, this.port);
        this.cluster = Cluster.builder().addContactPoint(this.host).withPort(this.port).build();
        this.session = cluster.connect();
        log.info("Connection has been established");
    }

}
