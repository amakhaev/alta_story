package com.alta.computator.calculator.actingCharacter;

import com.alta.computator.calculator.CalculatorCache;
import com.alta.computator.calculator.MovementUpdater;
import com.alta.computator.core.computator.movement.directionCalculation.MovementDirection;
import com.alta.computator.core.storage.StorageReader;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.awt.*;

/**
 * Provides the implementation of movement calculator that applied to acting character only.
 */
@RequiredArgsConstructor
public class ActingCharacterMediatorImpl implements ActingCharacterMediator, MovementUpdater {

    private final StorageReader storageReader;
    private final CalculatorCache calculatorCache;

    /**
     * {@inheritDoc}
     */
    @Setter
    private ActingCharacterEventListener eventListener;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdate(int delta) {
        boolean isParticipantMoved = ActingCharacterCalculator.moveParticipantToFocusPoint(
                this.storageReader.getActingCharacter(),
                this.storageReader.getAltitudeMap(),
                this.storageReader.getFocusPoint().getCurrentMapCoordinates()
        );

        if (isParticipantMoved) {
            this.produceJumpEventIfNeeded();
        }

        ActingCharacterCalculator.updateParticipantCoordinates(
                this.storageReader.getActingCharacter(), this.calculatorCache.getFocusPointMetadata().getConstantGlobalStartCoordination()
        );

        ActingCharacterCalculator.updateDirection(
                this.calculatorCache.getFocusPointMetadata().getLastMovementDirection(),
                this.storageReader.getActingCharacter().getCurrentDirection(),
                this.storageReader.getActingCharacter()
        );
        this.storageReader.getActingCharacter().setMoving(
                this.calculatorCache.getFocusPointMetadata().getGlobalMovementCalculator().isCurrentlyRunning()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getMapCoordinatesOfTargetParticipant() {
        Point actingCharacterMapCoordinates = this.storageReader.getActingCharacter().getCurrentMapCoordinates();

        Point targetCharacterMapCoordinate = new Point(actingCharacterMapCoordinates);
        if (this.storageReader.getActingCharacter().getCurrentDirection() == null) {
            this.storageReader.getActingCharacter().setCurrentDirection(MovementDirection.DOWN);
        }

        switch (this.storageReader.getActingCharacter().getCurrentDirection()) {
            case UP:
                targetCharacterMapCoordinate.y--;
                break;
            case DOWN:
                targetCharacterMapCoordinate.y++;
                break;
            case LEFT:
                targetCharacterMapCoordinate.x--;
                break;
            case RIGHT:
                targetCharacterMapCoordinate.x++;
                break;
        }

        return targetCharacterMapCoordinate;
    }

    private void produceJumpEventIfNeeded() {
        if (this.eventListener == null ||
                !this.storageReader.getAltitudeMap().isJumpTileState(
                        this.storageReader.getActingCharacter().getCurrentMapCoordinates().x,
                        this.storageReader.getActingCharacter().getCurrentMapCoordinates().y
                )) {
            return;
        }

        this.eventListener.onAfterMovingToJumpPoint(
                this.storageReader.getActingCharacter().getCurrentMapCoordinates(),
                this.storageReader.getActingCharacter().getUuid()
        );
    }
}
