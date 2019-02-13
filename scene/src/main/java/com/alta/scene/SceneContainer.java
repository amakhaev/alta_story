package com.alta.scene;

import com.alta.scene.configuration.SceneConfig;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import javax.inject.Named;

@Slf4j
public class SceneContainer extends BasicGame {

    private TiledMap tiledMap;

    @Inject
    public SceneContainer(@Named("sceneConfig") SceneConfig config) {
        super(config.getApp().getName());
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        this.tiledMap = new TiledMap("scene/data/maps/test/map.tmx");
    }

    @Override
    public void update(GameContainer gameContainer, int i) throws SlickException {

    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        this.tiledMap.render(0, 0);
    }
}
