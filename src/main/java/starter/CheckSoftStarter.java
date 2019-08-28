package starter;

import com.alta.dao.configuration.CassandraConnector;
import com.datastax.driver.core.Session;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides the starter that started the is checking soft.
 */
@Slf4j
public class CheckSoftStarter implements Starter {

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Injector injector) {
        log.info("Start soft checking...");

        boolean isSuccessful = true;
        if (this.isDatabaseAvailable(injector.getInstance(CassandraConnector.class))) {
            log.info("Database checked.");
        } else {
            isSuccessful = false;
            log.error("Can't get cassandra session. Was cassandra installed?");
        }

        if (isSuccessful) {
            log.info("Soft checking completed. Required dependencies are installed.");
        } else {
            log.info("Soft checking completed. One or more required dependency isn't installed.");
        }
    }

    private boolean isDatabaseAvailable(CassandraConnector connector) {
        Session session = connector.getSession();
        return session != null && session.getState() != null;
    }
}
