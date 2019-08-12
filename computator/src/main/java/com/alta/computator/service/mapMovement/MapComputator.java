package com.alta.computator.service.mapMovement;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.map.MapParticipant;
import com.alta.computator.utils.MovementCoordinateComputator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Provides the computations for the map participant
 */
@Slf4j
public class MapComputator {

    @Getter
    private final MapParticipant mapParticipant;

    /**
     * Initialize new instance of {@link MapComputator}
     */
    public MapComputator(MapParticipant mapParticipant) {
        this.mapParticipant = mapParticipant;
    }

    /**
     * Handles the computing of map participant
     *
     * @param altitudeMap - the altitude map
     * @param focusPointGlobalCoordinates - the global coordinates of focus point
     */
    public void onCompute(AltitudeMap altitudeMap, Point focusPointGlobalCoordinates) {
        if (this.mapParticipant == null || altitudeMap == null || focusPointGlobalCoordinates == null) {
            log.warn("One of participant of computation for mat ot found.");
            return;
        }

        this.mapParticipant.updateCurrentGlobalCoordinates(
                MovementCoordinateComputator.calculateGlobalCoordinateOfMap(
                        altitudeMap.getScreenWidth(),
                        altitudeMap.getTileWidth(),
                        focusPointGlobalCoordinates.x
                ),
                MovementCoordinateComputator.calculateGlobalCoordinateOfMap(
                        altitudeMap.getScreenHeight(),
                        altitudeMap.getTileHeight(),
                        focusPointGlobalCoordinates.y
                )
        );
    }
}
