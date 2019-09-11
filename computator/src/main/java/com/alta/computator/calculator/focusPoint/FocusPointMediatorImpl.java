package com.alta.computator.calculator.focusPoint;

import com.alta.computator.calculator.CalculatorCache;
import com.alta.computator.calculator.MovementUpdater;
import com.alta.computator.core.computator.movement.directionCalculation.MovementDirection;
import com.alta.computator.core.storage.StorageReader;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Provides the implementation of movement calculator that applied to focus point only.
 */
@Slf4j
public class FocusPointMediatorImpl implements FocusPointMediator, MovementUpdater {

    private final StorageReader storageReader;
    private final CalculatorCache calculatorCache;

    @Setter
    private FocusPointEventListener eventListener;

    /**
     * Initialize new instance of {@link FocusPointMediatorImpl}.
     *
     * @param storageReader     - the {@link StorageReader} instance.
     * @param calculatorCache   - the {@link CalculatorCache} instance.
     */
    public FocusPointMediatorImpl(StorageReader storageReader, CalculatorCache calculatorCache) {
        this.storageReader = storageReader;
        this.calculatorCache = calculatorCache;
        this.calculatorCache.pushFocusPointMetadata(new FocusPointMetadata());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tryToRunMovement(MovementDirection movementDirection) {
        boolean isMovementRan = FocusPointCalculator.tryToRunMovement(
                movementDirection,
                this.calculatorCache.getFocusPointMetadata(),
                this.storageReader.getAltitudeMap(),
                this.storageReader.getFocusPoint()
        );

        Point targetPoint = this.calculatorCache.getFocusPointMetadata().getGlobalMovementCalculator().getMapTargetCoordinates();
        if (isMovementRan &&
                this.eventListener != null &&
                this.storageReader.getAltitudeMap().isJumpTileState(targetPoint.x, targetPoint.y)) {
            this.eventListener.onBeforeMovingToJumpTile(targetPoint);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMoving() {
        return this.calculatorCache.getFocusPointMetadata().getGlobalMovementCalculator().isCurrentlyRunning();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPause(boolean isPause) {
        this.calculatorCache.getFocusPointMetadata().setComputationPause(isPause);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdate(int delta) {
        if (!this.checkRequiredFields()) {
            return;
        }

        if (!this.calculatorCache.getFocusPointMetadata().isInitializedFirstTime()) {
            FocusPointCalculator.onInitialize(
                    this.calculatorCache.getFocusPointMetadata(),
                    this.storageReader.getAltitudeMap(),
                    this.storageReader.getFocusPoint()
            );
            this.calculatorCache.getFocusPointMetadata().setInitializedFirstTime(true);
        }

        if (this.calculatorCache.getFocusPointMetadata().getGlobalMovementCalculator().isCurrentlyRunning()) {
            FocusPointCalculator.onUpdate(
                    this.calculatorCache.getFocusPointMetadata(),
                    this.storageReader.getAltitudeMap(),
                    this.storageReader.getFocusPoint()
            );
        }
    }

    private boolean checkRequiredFields() {
        if (this.storageReader.getAltitudeMap() == null) {
            log.warn("The altitude map not found in the storage");
            return false;
        }

        if (this.storageReader.getFocusPoint() == null) {
            log.warn("The focus point not found in the storage");
            return false;
        }

        return true;
    }
}
