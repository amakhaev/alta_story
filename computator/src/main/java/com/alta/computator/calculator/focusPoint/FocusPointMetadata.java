package com.alta.computator.calculator.focusPoint;

import com.alta.computator.core.computator.movement.GlobalMovementCalculator;
import com.alta.computator.core.computator.movement.GlobalMovementCalculatorImpl;
import com.alta.computator.core.computator.movement.MovementFactory;
import com.alta.computator.core.computator.movement.directionCalculation.MovementDirection;
import com.alta.computator.core.computator.movement.directionCalculation.MovementDirectionStrategy;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

/**
 * Provides the metadata for focus point calculation.
 */
public class FocusPointMetadata {

    @Getter
    private final GlobalMovementCalculator globalMovementCalculator;

    @Getter
    private final MovementDirectionStrategy movementDirectionStrategy;

    @Getter
    @Setter
    private boolean isComputationPause;

    @Getter
    @Setter
    private boolean isInitializedFirstTime;

    @Getter
    @Setter
    private Point constantGlobalStartCoordination;

    @Getter
    @Setter
    private MovementDirection lastMovementDirection;

    /**
     * Initialize new instance of {@link FocusPointMetadata}.
     */
    FocusPointMetadata() {
        this.globalMovementCalculator = MovementFactory.createGlobalCalculator(GlobalMovementCalculatorImpl.NORMAL_MOVE_SPEED);
        this.movementDirectionStrategy = MovementFactory.createFocusPointStrategy();
        this.isComputationPause = false;
    }
}
