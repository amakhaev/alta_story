package com.alta.ui.scene;

import com.alta.ui.ComponentState;
import com.alta.ui.UIComponent;
import com.alta.ui.componentContainer.BaseContainer;
import com.alta.ui.transition.Transition;
import com.alta.ui.transition.TransitionInComponent;
import com.alta.ui.transition.TransitionOutComponent;
import io.reactivex.Observable;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * Provides scene with all components.
 */
public abstract class Scene implements UIComponent {

    protected BaseContainer componentContainer;
    private TransitionInComponent transitionIn;
    private TransitionOutComponent transitionOut;
    private Transition currentTransition;

    /**
     * Initialize new instance of {@link Scene}.
     *
     * @param container - the container with UI components.
     */
    protected Scene(BaseContainer container) {
        this.componentContainer = container;
        this.transitionOut = new TransitionOutComponent(this.componentContainer);
        this.transitionIn = new TransitionInComponent(this.componentContainer);
    }

    /**
     * Init the UI component.
     *
     * @param gameContainer - the game componentContainer.
     */
    @Override
    public final void init(GameContainer gameContainer) throws SlickException {
        this.initInternal(gameContainer);

        this.transitionIn.init(gameContainer);
        this.transitionIn.startAndSubscribeOnFinish().subscribe(data -> {
                    this.componentContainer.setState(ComponentState.IN_PROGRESS);
                    this.currentTransition = null;
                }
        );

        this.transitionOut.init(gameContainer);
        this.currentTransition = this.transitionIn;
    }

    /**
     * Updates the component.
     *
     * @param gameContainer - the game componentContainer.
     * @param delta - the last update delta
     */
    @Override
    public final void update(GameContainer gameContainer, int delta) throws SlickException {
        if (this.currentTransition != null) {
            this.currentTransition.update(gameContainer, delta);
        } else if (this.componentContainer.getState() == ComponentState.IN_PROGRESS) {
            this.updateInternal(gameContainer, delta);
        }
    }

    /**
     * Renders the component.
     *
     * @param gameContainer - the game componentContainer.
     * @param graphics - the graphics object.
     */
    @Override
    public final void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        if (this.currentTransition != null) {
            this.currentTransition.render(gameContainer, graphics);
        } else if (this.componentContainer.getState() == ComponentState.IN_PROGRESS) {
            this.renderInternal(gameContainer, graphics);
        }
    }

    /**
     * Destroy the component.
     */
    @Override
    public void destroy() {
        this.transitionOut.destroy();
        this.transitionIn.destroy();
        this.componentContainer.destroy();
        this.transitionIn = null;
        this.componentContainer = null;
        this.transitionOut = null;
    }

    /**
     * Finishes works of scene.
     *
     * @return the observable indicates when scene will be destroyed.
     */
    public Observable<Void> finish() {
        this.componentContainer.setState(ComponentState.DETACH);
        this.currentTransition = this.transitionOut;

        return Observable.fromCallable(() -> {
            this.transitionOut.startAndSubscribeOnFinish().subscribe(f -> {
                this.componentContainer.destroy();
                this.currentTransition = null;
            });

            return null;
        });
    }

    protected void initInternal(GameContainer gameContainer) throws SlickException {
        this.componentContainer.init(gameContainer);
    }

    protected void updateInternal(GameContainer gameContainer, int delta) throws SlickException {
        this.componentContainer.update(gameContainer, delta);
    }

    protected void renderInternal(GameContainer gameContainer, Graphics graphics) throws SlickException {
        this.componentContainer.render(gameContainer, graphics);
    }
}
