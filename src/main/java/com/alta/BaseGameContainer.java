package com.alta;

import com.alta.ui.scene.MapScene;
import com.alta.utils.ResourceUtils;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class BaseGameContainer extends BasicGame {

    private MapScene mapScene;

    public BaseGameContainer() {
        super(ResourceUtils.getString("AlTa"));
        this.mapScene = new MapScene();
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        this.mapScene.init(gameContainer);
    }

    @Override
    public void update(GameContainer gameContainer, int i) throws SlickException {
        this.mapScene.update(gameContainer, i);
    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        this.mapScene.render(gameContainer, graphics);
    }
}
