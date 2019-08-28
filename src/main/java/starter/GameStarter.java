package starter;

import com.alta.mediator.Mediator;
import com.google.inject.Injector;

/**
 * Provides the starter of game.
 */
public class GameStarter implements Starter {

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Injector injector) {
        injector.getInstance(Mediator.class).loadSavedGameAndStart();
    }
}
