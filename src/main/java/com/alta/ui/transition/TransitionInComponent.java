package com.alta.ui.transition;

import com.alta.ui.ComponentState;
import com.alta.ui.componentContainer.BaseContainer;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * Provides the transition IN component.
 */
public class TransitionInComponent extends Transition {

    private int opacity = 255;

    /**
     * Initialize new instance of {@link TransitionInComponent}.
     *
     * @param componentContainer - the container with components that should be shown.
     */
    public TransitionInComponent(BaseContainer componentContainer) {
        super(componentContainer);
    }

    /**
     * Updates the component.
     *
     * @param gameContainer - the game componentContainer.
     * @param delta - the last update delta
     */
    @Override
    public void onUpdateForInProgressState(GameContainer gameContainer, int delta) throws SlickException {
        if (this.opacity > 0) {
            this.opacity -= OPACITY_SPEED;
        }
        else {
            this.setState(ComponentState.DETACH);
            this.completeTransition.onNext(new Object());
            this.completeTransition.onComplete();
        }
    }

    /**
     * Renders the component whe it has IN_PROGRESS state.
     *
     * @param gameContainer - the game componentContainer.
     * @param graphics - the graphics object.
     */
    @Override
    public void onRenderForInProgressState(GameContainer gameContainer, Graphics graphics) throws SlickException {
        this.componentContainer.render(gameContainer, graphics);
        graphics.setColor(new Color(0, 0, 0, this.opacity));
        graphics.fillRect(0, 0, gameContainer.getWidth(), gameContainer.getHeight());
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
