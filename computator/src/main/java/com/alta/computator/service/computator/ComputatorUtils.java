package com.alta.computator.service.computator;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.altitudeMap.TileState;
import com.alta.computator.model.participant.CoordinatedParticipant;
import com.alta.computator.model.participant.actor.ActorParticipant;
import com.alta.computator.service.computator.movement.MovementWorker;
import com.alta.computator.utils.MovementCoordinateComputator;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Provides the utilities to help calculate values easily.
 */
@Slf4j
@UtilityClass
public class ComputatorUtils {

    /**
     * Initializes the participant for future computation.
     *
     * @param altitudeMap                   - the {@link AltitudeMap} instance.
     * @param participant                   - the {@link CoordinatedParticipant} to be used in calculations.
     * @param focusPointGlobalCoordinates   - the focus point coordinates.
     */
    public void initializeParticipant(AltitudeMap altitudeMap,
                                      CoordinatedParticipant participant,
                                      Point focusPointGlobalCoordinates) {
        if (altitudeMap == null || participant == null || focusPointGlobalCoordinates == null) {
            log.warn(
                    "Invalid arguments for initialize participant. One of model argument is null: " +
                            "AltitudeMap: {}, Participant: {}, Focus point: {}",
                    altitudeMap,
                    participant,
                    focusPointGlobalCoordinates
            );
            return;
        }

        int x = MovementCoordinateComputator.calculateGlobalCoordinatesDependsOnAnotherObject(
                altitudeMap.getScreenWidth(),
                altitudeMap.getTileWidth(),
                participant.getStartMapCoordinates().x,
                focusPointGlobalCoordinates.x
        );

        int y = MovementCoordinateComputator.calculateGlobalCoordinatesDependsOnAnotherObject(
                altitudeMap.getScreenHeight(),
                altitudeMap.getTileHeight(),
                participant.getStartMapCoordinates().y,
                focusPointGlobalCoordinates.y
        );

        participant.updateCurrentGlobalCoordinates(x, y);

        altitudeMap.setTileState(
                participant.getCurrentMapCoordinates().x,
                participant.getCurrentMapCoordinates().y,
                TileState.BARRIER
        );
    }

    /**
     * Calculates the coordinates of step for participant.
     *
     * @param worker                        - the {@link MovementWorker} instance.
     * @param participant                   - the participant for which data is calculating.
     * @param altitudeMap                   - the {@link AltitudeMap} instance.
     * @param focusPointGlobalCoordinates   - the focus point coordinates.
     */
    public void calculateMovementStep(MovementWorker worker,
                                      ActorParticipant participant,
                                      AltitudeMap altitudeMap,
                                      Point focusPointGlobalCoordinates) {
        worker.onUpdate();

        // If last update complete computation then it should be cleared, otherwise just update coordinates
        if (worker.isCurrentlyRunning()) {
            makeCalculationForRunningWorker(worker, participant, altitudeMap, focusPointGlobalCoordinates);
        } else {
            makeCalculationForStoppedWorker(worker, participant, altitudeMap, focusPointGlobalCoordinates);
        }
    }

    /**
     * Calculates the global coordinates of participant and updates the model value.
     *
     * @param actorParticipant              - the participant for which data is calculating.
     * @param altitudeMap                   - the {@link AltitudeMap} instance.
     * @param focusPointGlobalCoordinates   - the focus point coordinates.
     */
    public void calculateGlobalCoordinates(ActorParticipant actorParticipant,
                                           AltitudeMap altitudeMap,
                                           Point focusPointGlobalCoordinates) {
        int x = MovementCoordinateComputator.calculateGlobalCoordinatesDependsOnAnotherObject(
                altitudeMap.getScreenWidth(),
                altitudeMap.getTileWidth(),
                actorParticipant.getCurrentMapCoordinates().x,
                focusPointGlobalCoordinates.x
        );

        int y = MovementCoordinateComputator.calculateGlobalCoordinatesDependsOnAnotherObject(
                altitudeMap.getScreenHeight(),
                altitudeMap.getTileHeight(),
                actorParticipant.getCurrentMapCoordinates().y,
                focusPointGlobalCoordinates.y
        );

        actorParticipant.updateCurrentGlobalCoordinates(x, y);
    }

    private void makeCalculationForRunningWorker(MovementWorker worker,
                                                 ActorParticipant participant,
                                                 AltitudeMap altitudeMap,
                                                 Point focusPointGlobalCoordinates) {
        int x = worker.getGlobalCurrentCoordinates().x +
                MovementCoordinateComputator.calculateGlobalCoordinateOfMap(
                        altitudeMap.getScreenWidth(), altitudeMap.getTileWidth(), focusPointGlobalCoordinates.x
                );

        int y = worker.getGlobalCurrentCoordinates().y +
                MovementCoordinateComputator.calculateGlobalCoordinateOfMap(
                        altitudeMap.getScreenHeight(), altitudeMap.getTileHeight(), focusPointGlobalCoordinates.y
                );

        participant.updateCurrentGlobalCoordinates(x, y);
    }

    private void makeCalculationForStoppedWorker(MovementWorker worker,
                                                 ActorParticipant participant,
                                                 AltitudeMap altitudeMap,
                                                 Point focusPointGlobalCoordinates) {
        altitudeMap.setTileState(
                participant.getCurrentMapCoordinates().x,
                participant.getCurrentMapCoordinates().y,
                TileState.FREE
        );

        participant.updateCurrentMapCoordinates(
                worker.getMapTargetCoordinates().x, worker.getMapTargetCoordinates().y
        );
        calculateGlobalCoordinates(participant, altitudeMap, focusPointGlobalCoordinates);
        worker.clearLastMovement();
        participant.setMoving(false);
    }
}
