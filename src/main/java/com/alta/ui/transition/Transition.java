package com.alta.ui.transition;

import com.alta.ui.BaseUIComponent;
import com.alta.ui.ComponentState;
import com.alta.ui.componentContainer.BaseContainer;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

/**
 * Provides the transition component
 */
public abstract class Transition extends BaseUIComponent {

    protected static final int OPACITY_SPEED = 6;

    protected final BaseContainer componentContainer;
    protected Subject<Object> completeTransition;

    /**
     * Initialize new instance of {@link TransitionOutComponent}.
     *
     * @param componentContainer - the container with components that should be shown.
     */
    public Transition(BaseContainer componentContainer) {
        this.componentContainer = componentContainer;
        this.setState(ComponentState.READY);
    }

    /**
     * Init the UI component.
     *
     * @param gameContainer - the game componentContainer.
     */
    @Override
    public final void init(GameContainer gameContainer) throws SlickException {
    }

    /**
     * Destroy the component.
     */
    @Override
    public void destroy() {
        this.completeTransition = null;
        super.destroy();
    }

    /**
     * Starts the transition and subscribe to the end.
     *
     * @return the {@link Subject} subscription.
     */
    public Subject<Object> startAndSubscribeOnFinish() {
        this.completeTransition = ReplaySubject.create();
        this.setState(ComponentState.IN_PROGRESS);
        return this.completeTransition;
    }

}
