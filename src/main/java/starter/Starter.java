package starter;

import com.google.inject.Injector;

/**
 * Provides the starter of application.
 */
public interface Starter {

    /**
     * Starts the part of application.
     *
     * @param injector - the guice injector.
     */
    void start(Injector injector);

}
