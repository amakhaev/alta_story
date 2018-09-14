package com.alta.ui.component;

import com.alta.domain.data.buffer.MapBuffer;
import com.alta.domain.map.MapCoordinateCalculatorUtils;
import com.alta.ui.BaseUIComponent;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import java.awt.*;

/**
 * Provides the component for focusing on map
 */
public class FocusPointComponent extends BaseUIComponent {

    private Point currentPointPosition;
    private MapBuffer mapBuffer;
    private int tileWidth = 0;
    private int tileHeight = 0;
    private Point centerGlobalCoordinate;

    /**
     * Initialize new instance of {@link FocusPointComponent}.
     */
    public FocusPointComponent(MapBuffer mapBuffer) {
        this.mapBuffer = mapBuffer;
        this.subscribeToBufferChange();
    }

    /**
     * Init the UI component.
     *
     * @param gameContainer - the game componentContainer.
     */
    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        this.currentPointPosition = new Point(25, 95);
        this.centerGlobalCoordinate = new Point(
                MapCoordinateCalculatorUtils.calculateGlobalCenterPositionOfTileX(this.tileWidth, gameContainer.getWidth()),
                MapCoordinateCalculatorUtils.calculateGlobalCenterPositionOfTileY(this.tileHeight, gameContainer.getHeight())
        );

        this.mapBuffer.setGlobalOffsetPointPosition(
                MapCoordinateCalculatorUtils.calculateOffsetTileGlobalX(this.tileWidth, this.currentPointPosition.x),
                MapCoordinateCalculatorUtils.calculateOffsetTileGlobalY(this.tileHeight, this.currentPointPosition.y)
        );
    }

    /**
     * Destroy the component.
     */
    @Override
    public void destroy() {
        this.currentPointPosition = null;
        this.mapBuffer = null;
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
        this.mapBuffer.setGlobalOffsetPointPosition(
                MapCoordinateCalculatorUtils.calculateOffsetTileGlobalX(this.tileWidth, this.currentPointPosition.x),
                MapCoordinateCalculatorUtils.calculateOffsetTileGlobalY(this.tileHeight, this.currentPointPosition.y)
        );
    }

    /**
     * Called in render when component state is READY.
     *
     * @param gameContainer - the game componentContainer.
     * @param graphics - the graphics object.
     */
    @Override
    protected void onRenderForReadyState(GameContainer gameContainer, Graphics graphics) throws SlickException {
        graphics.setColor(new Color(255, 0, 0));

        graphics.fillRect(
                this.centerGlobalCoordinate.x,
                this.centerGlobalCoordinate.y,
                this.tileWidth,
                this.tileHeight
        );
    }

    /**
     * Called in render when component state is IN_PROGRESS.
     *
     * @param gameContainer - the game componentContainer.
     * @param graphics - the graphics object.
     */
    @Override
    protected void onRenderForInProgressState(GameContainer gameContainer, Graphics graphics) throws SlickException {
        graphics.setColor(new Color(255, 0, 0));

        graphics.fillRect(
                this.centerGlobalCoordinate.x,
                this.centerGlobalCoordinate.y,
                this.tileWidth,
                this.tileHeight
        );
    }

    private void subscribeToBufferChange() {
        if (this.mapBuffer == null) {
            return;
        }

        this.mapBuffer.subscribeToTileWidthChange().subscribe(width -> this.tileWidth = width);
        this.mapBuffer.subscribeToTileHeightChange().subscribe(height -> this.tileHeight = height);
    }
}
