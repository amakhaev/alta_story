package com.alta.computator.calculator.map;

import com.alta.computator.calculator.MovementUpdater;
import com.alta.computator.core.storage.StorageReader;
import com.alta.computator.utils.MovementCoordinateComputator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Provides the implementation of movement calculator that applied to map only.
 */
@Slf4j
@RequiredArgsConstructor
public class MapMediatorImpl implements MapMediator, MovementUpdater {

    private final StorageReader storageReader;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdate(int delta) {
        if (this.storageReader.getMap() == null ||
                this.storageReader.getAltitudeMap() == null ||
                this.storageReader.getFocusPoint() == null) {
            log.warn("One or more required arguments are null. Map: {}, AltitudeMap: {}, Focus point: {}",
                    this.storageReader.getMap(),
                    this.storageReader.getAltitudeMap(),
                    this.storageReader.getFocusPoint()
            );
        }

        this.storageReader.getMap().updateCurrentGlobalCoordinates(
                MovementCoordinateComputator.calculateGlobalCoordinateOfMap(
                        this.storageReader.getAltitudeMap().getScreenWidth(),
                        this.storageReader.getAltitudeMap().getTileWidth(),
                        this.storageReader.getFocusPoint().getCurrentGlobalCoordinates().x
                ),
                MovementCoordinateComputator.calculateGlobalCoordinateOfMap(
                        this.storageReader.getAltitudeMap().getScreenHeight(),
                        this.storageReader.getAltitudeMap().getTileHeight(),
                        this.storageReader.getFocusPoint().getCurrentGlobalCoordinates().y
                )
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getMapGlobalCoordinates() {
        return this.storageReader.getMap() != null ? this.storageReader.getMap().getCurrentGlobalCoordinates() : null;
    }
}
