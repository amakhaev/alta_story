package com.alta.computator.calculator.focusPoint;

import com.alta.computator.core.computator.movement.directionCalculation.MovementDirection;
import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.focusPoint.FocusPointParticipant;
import com.alta.computator.utils.MovementCoordinateComputator;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Provides the calculator of focus point.
 */
@Slf4j
@UtilityClass
class FocusPointCalculator {

    /**
     * Initializes the focus point data.
     *
     * @param metadata      - the metadata to be used for calculation.
     * @param altitudeMap   - the {@link AltitudeMap} instance.
     * @param participant   - the {@link FocusPointParticipant} instance.
     */
    void onInitialize(FocusPointMetadata metadata, AltitudeMap altitudeMap, FocusPointParticipant participant) {
        if (metadata == null || altitudeMap == null || participant == null) {
            log.error("Metadata: {}, AltitudeMap: {}, Participant: {}", metadata, altitudeMap, participant);
            throw new RuntimeException("One or more required arguments are null.");
        }

        metadata.setConstantGlobalStartCoordination(new Point(
                MovementCoordinateComputator.calculateGlobalStartCoordinateOnCenterOfScreen(
                        altitudeMap.getTileWidth(), altitudeMap.getScreenWidth()
                ),
                MovementCoordinateComputator.calculateGlobalStartCoordinateOnCenterOfScreen(
                        altitudeMap.getTileHeight(), altitudeMap.getScreenHeight()
                )
        ));

        participant.updateCurrentGlobalCoordinates(
                MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                        altitudeMap.getTileWidth(),
                        participant.getStartMapCoordinates().x
                ),
                MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                        altitudeMap.getTileHeight(),
                        participant.getStartMapCoordinates().y
                )
        );
    }

    /**
     * Updates the focus point data.
     *
     * @param metadata      - the metadata to be used for calculation.
     * @param altitudeMap   - the {@link AltitudeMap} instance.
     * @param participant   - the {@link FocusPointParticipant} instance.
     */
    void onUpdate(FocusPointMetadata metadata, AltitudeMap altitudeMap, FocusPointParticipant participant) {
        if (metadata == null || altitudeMap == null || participant == null) {
            log.error("Metadata: {}, AltitudeMap: {}, Participant: {}", metadata, altitudeMap, participant);
            throw new RuntimeException("One or more required arguments are null.");
        }

        metadata.getGlobalMovementCalculator().onUpdate();

        // If last update complete computation then it should be cleared, otherwise just update coordinates
        if (metadata.getGlobalMovementCalculator().isCurrentlyRunning()) {
            participant.updateCurrentGlobalCoordinates(
                    metadata.getGlobalMovementCalculator().getGlobalCurrentCoordinates().x,
                    metadata.getGlobalMovementCalculator().getGlobalCurrentCoordinates().y
            );
        } else {
            participant.updateCurrentMapCoordinates(
                    metadata.getGlobalMovementCalculator().getMapTargetCoordinates().x,
                    metadata.getGlobalMovementCalculator().getMapTargetCoordinates().y
            );
            participant.updateCurrentGlobalCoordinates(
                    MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                            altitudeMap.getTileWidth(),
                            participant.getCurrentMapCoordinates().x
                    ),
                    MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                            altitudeMap.getTileHeight(),
                            participant.getCurrentMapCoordinates().y
                    )
            );
            metadata.getGlobalMovementCalculator().clearLastMovement();
        }
    }

    boolean tryToRunMovement(MovementDirection movementDirection, FocusPointMetadata metadata,
                          AltitudeMap altitudeMap, FocusPointParticipant participant) {
        if (metadata == null || altitudeMap == null || participant == null || movementDirection == null) {
            log.error(
                    "Metadata: {}, AltitudeMap: {}, Participant: {}, Direction: {}",
                    metadata, altitudeMap, participant, movementDirection
            );
            throw new RuntimeException("One or more required arguments are null.");
        }

        if (metadata.getGlobalMovementCalculator().isCurrentlyRunning() || metadata.isComputationPause()) {
            return false;
        }

        metadata.setLastMovementDirection(movementDirection);
        metadata.getMovementDirectionStrategy().calculateMovement(
                participant.getCurrentMapCoordinates(), movementDirection, altitudeMap
        );
        Point targetMapPoint = metadata.getMovementDirectionStrategy().getTargetPointForMoving();

        if (metadata.getMovementDirectionStrategy().isCanMoveTo(targetMapPoint, altitudeMap)) {
            metadata.getGlobalMovementCalculator().tryToRunMoveProcess(
                    altitudeMap, participant.getCurrentMapCoordinates(), targetMapPoint
            );

            return metadata.getGlobalMovementCalculator().isCurrentlyRunning();
        } else {
            return false;
        }
    }

}
