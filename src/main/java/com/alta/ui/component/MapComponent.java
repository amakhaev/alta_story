package com.alta.ui.component;

import com.alta.domain.data.buffer.MapBuffer;
import com.alta.domain.data.map.MapEntity;
import com.alta.domain.map.MapCoordinateCalculatorUtils;
import com.alta.domain.map.MapService;
import com.alta.ui.BaseUIComponent;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import java.awt.*;

/**
 * Provides the map component.
 */
public class MapComponent extends BaseUIComponent {

    private MapService mapService;
    private MapEntity mapEntity;
    private MapBuffer mapBuffer;
    private Point focusedPointPositions;

    /**
     * Initialize new instance of {@link MapComponent}.
     */
    public MapComponent(MapBuffer buffer) {
        this.mapService = new MapService();
        this.mapBuffer = buffer;
        this.subscribeToBufferChange();
    }

    /**
     * Init the UI component.
     *
     * @param gameContainer - the game componentContainer.
     */
    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        String path = this.mapService.getPath("test");
        if (path == null || path.isEmpty()) {
            throw new SlickException("The map 'test.tmx' doesn't exists");
        }

        this.mapEntity = new MapEntity(new TiledMap(path));
        this.mapBuffer.setAltitudeMap(this.mapEntity.getAltitudeMap());
        this.mapBuffer.setTileWidth(this.mapEntity.getTiledMap().getTileWidth());
        this.mapBuffer.setTileHeight(this.mapEntity.getTiledMap().getTileHeight());
    }

    /**
     * Destroy the component.
     */
    @Override
    public void destroy() {
        this.mapService = null;
        this.mapBuffer = null;
        this.mapEntity = null;
        this.focusedPointPositions = null;
        super.destroy();
    }

    /**
     * Called in update when component state is IN_PROGRESS.
     *
     * @param gameContainer - the game componentContainer.
     * @param delta - the last update delta
     */
    @Override
    protected void onUpdateForInProgressState(GameContainer gameContainer, int delta) throws SlickException {
    }

    /**
     * Called in render when component state is READY.
     *
     * @param gameContainer - the game componentContainer.
     * @param graphics - the graphics object.
     */
    @Override
    protected void onRenderForReadyState(GameContainer gameContainer, Graphics graphics) throws SlickException {
        int x = MapCoordinateCalculatorUtils.calculateCenterByOffsetX(this.focusedPointPositions.x,
                this.mapEntity.getTiledMap().getTileWidth(), gameContainer.getWidth());
        int y = MapCoordinateCalculatorUtils.calculateCenterByOffsetY(this.focusedPointPositions.y,
                this.mapEntity.getTiledMap().getTileHeight(), gameContainer.getHeight());
        this.mapEntity.getTiledMap().render(x, y);
    }

    /**
     * Called in render when component state is IN_PROGRESS.
     *
     * @param gameContainer - the game componentContainer.
     * @param graphics - the graphics object.
     */
    @Override
    protected void onRenderForInProgressState(GameContainer gameContainer, Graphics graphics) throws SlickException {
        int x = MapCoordinateCalculatorUtils.calculateCenterByOffsetX(this.focusedPointPositions.x,
                this.mapEntity.getTiledMap().getTileWidth(), gameContainer.getWidth());
        int y = MapCoordinateCalculatorUtils.calculateCenterByOffsetY(this.focusedPointPositions.y,
                this.mapEntity.getTiledMap().getTileHeight(), gameContainer.getHeight());
        this.mapEntity.getTiledMap().render(x, y);
    }

    private void subscribeToBufferChange() {
        if (this.mapBuffer == null) {
            return;
        }

        this.mapBuffer.subscribeToGlobalOffsetPointPositionChange().subscribe(position -> this.focusedPointPositions = position);
    }
}
