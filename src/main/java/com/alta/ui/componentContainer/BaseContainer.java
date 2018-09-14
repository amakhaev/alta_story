package com.alta.ui.componentContainer;

import com.alta.ui.BaseUIComponent;
import com.alta.ui.ComponentState;
import com.alta.ui.UIComponent;
import lombok.Getter;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides the base container component container.
 */
public abstract class BaseContainer<T extends BaseUIComponent> implements UIComponent {

    @Getter
    private ComponentState state;

    protected List<T> components;

    /**
     * Initialize new instance of {@link Container}
     */
    public BaseContainer() {
        this.components = new ArrayList<>();
    }

    /**
     * Init the UI component.
     *
     * @param gameContainer - the game componentContainer.
     */
    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        for (BaseUIComponent c: this.components) {
            c.init(gameContainer);
            c.setState(ComponentState.READY);
        }
    }

    /**
     * Updates the component.
     *
     * @param gameContainer - the game componentContainer.
     * @param delta - the last update delta
     */
    @Override
    public void update(GameContainer gameContainer, int delta) throws SlickException {
        for (BaseUIComponent c: this.components) {
            c.update(gameContainer, delta);
        }
    }

    /**
     * Renders the component.
     *
     * @param gameContainer - the game componentContainer.
     * @param graphics - the graphics object.
     */
    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        for (BaseUIComponent c: this.components) {
            c.render(gameContainer, graphics);
        }
    }

    /**
     * Destroy the component.
     */
    @Override
    public void destroy() {
        if (this.components != null && this.components.size() > 0) {
            this.components.forEach(c -> {
                c.destroy();
                c.setState(ComponentState.DESTROYED);
            });
        }
        this.components = null;
        this.setState(ComponentState.DESTROYED);
    }

    /**
     * Adds the component to container.
     *
     * @param component - the component to add.
     */
    public void add(T component) {
        this.components.add(component);
    }

    /**
     * Sets the state of container ad all components.
     *
     * @param state - the state.
     */
    public void setState(ComponentState state) {
        this.state = state;
        if (this.components != null) {
            this.components.forEach(c -> c.setState(this.getState()));
        }
    }

}
