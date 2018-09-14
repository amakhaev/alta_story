package com.alta.ui.scene;

import com.alta.domain.data.buffer.MapBuffer;
import com.alta.ui.component.FocusPointComponent;
import com.alta.ui.component.MapComponent;
import com.alta.ui.componentContainer.Container;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * Provides the scene related to map.
 */
public class MapScene extends Scene {

    private MapBuffer mapBuffer;

    /**
     * Initialize new instance of {@link Scene}.     *
     */
    public MapScene() {
        super(new Container());
        this.mapBuffer = new MapBuffer();
        this.componentContainer.add(new MapComponent(this.mapBuffer));
        this.componentContainer.add(new FocusPointComponent(this.mapBuffer));
    }

    protected void initInternal(GameContainer gameContainer) throws SlickException {
        super.initInternal(gameContainer);
    }

    protected void updateInternal(GameContainer gameContainer, int delta) throws SlickException {
        super.updateInternal(gameContainer, delta);
    }

    protected void renderInternal(GameContainer gameContainer, Graphics graphics) throws SlickException {
        super.renderInternal(gameContainer, graphics);
    }

    /**
     * Destroy the component.
     */
    @Override
    public void destroy() {
        this.mapBuffer = null;
        super.destroy();
    }
}
