package com.alta.domain.data.buffer;

import com.alta.domain.data.map.TileType;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;
import lombok.Getter;

import java.awt.*;

/**
 * Provides the buffer related to buffered components.
 */
public class MapBuffer {

    @Getter
    private TileType[][] altitudeMap;

    @Getter
    private Point globalOffsetPointPosition;

    @Getter
    private int tileWidth;

    @Getter
    private int tileHeight;

    private Subject<TileType[][]> altitudeMapChanged;
    private Subject<Point> globalOffsetPointPositionChanged;
    private Subject<Integer> tileWidthChanged;
    private Subject<Integer> tileHeightChanged;

    /**
     * Initialize new instance of {@link MapBuffer}.
     */
    public MapBuffer() {
        this.altitudeMapChanged = ReplaySubject.create();
        this.globalOffsetPointPositionChanged = ReplaySubject.create();
        this.tileWidthChanged = ReplaySubject.create();
        this.tileHeightChanged = ReplaySubject.create();
    }

    /**
     * Sets the altitude map.
     *
     * @param altitudeMap - the new map value.
     */
    public void setAltitudeMap(TileType[][] altitudeMap) {
        this.altitudeMap = altitudeMap;
        this.altitudeMapChanged.onNext(this.altitudeMap);
    }

    /**
     * Sets the tile width.
     *
     * @param width - the width of tile.
     */
    public void setTileWidth(int width) {
        this.tileWidth = width;
        this.tileWidthChanged.onNext(this.tileWidth);
    }

    /**
     * Sets the tile height.
     *
     * @param height - the height of tile.
     */
    public void setTileHeight(int height) {
        this.tileHeight = height;
        this.tileHeightChanged.onNext(this.tileHeight);
    }

    /**
     * Sets the altitude map.
     */
    public void setGlobalOffsetPointPosition(int x, int y) {
        if (this.globalOffsetPointPosition == null) {
            this.globalOffsetPointPosition = new Point();
        }

        this.globalOffsetPointPosition.x = x;
        this.globalOffsetPointPosition.y = y;
        this.globalOffsetPointPositionChanged.onNext(this.globalOffsetPointPosition);
    }


    /**
     * Subscribe to changing of altitude map.
     */
    public Subject<TileType[][]> subscribeToAltitudeMapChange() {
        return this.altitudeMapChanged;
    }

    /**
     * Subscribe to changing of global focus point position.
     */
    public Subject<Point> subscribeToGlobalOffsetPointPositionChange() {
        return this.globalOffsetPointPositionChanged;
    }

    /**
     * Subscribe to changing of tile width.
     */
    public Subject<Integer> subscribeToTileWidthChange() {
        return this.tileWidthChanged;
    }

    /**
     * Subscribe to changing of tile height.
     */
    public Subject<Integer> subscribeToTileHeightChange() {
        return this.tileHeightChanged;
    }
}
