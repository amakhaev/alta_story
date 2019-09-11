package com.alta.computator.calculator.facility;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.participant.facility.FacilityParticipant;
import com.alta.computator.utils.MovementCoordinateComputator;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Provides the calculator of facility.
 */
@Slf4j
@UtilityClass
class FacilityCalculator {

    /**
     * Initializes the participant.
     *
     * @param altitudeMap - the {@link AltitudeMap} instance.
     * @param participant - the facility participant ot be initialized.
     */
    void initialize(AltitudeMap altitudeMap, FacilityParticipant participant) {
        if (altitudeMap == null || participant == null) {
            log.error("AltitudeMap: {}, Participant: {}", altitudeMap, participant);
            throw new RuntimeException("One or more required arguments are null.");
        }

        participant.getFacilityPartParticipants().forEach(fp -> {
                    int x = fp.getStartMapCoordinates().x + fp.getShiftTilePosition().x;
                    int y = fp.getStartMapCoordinates().y + fp.getShiftTilePosition().y;
                    altitudeMap.setTileState(x, y, fp.getTileState());
                }
        );
    }

    /**
     * Updates the global coordinates of whole facility.
     *
     * @param participant                   - the participant for which update is coming.
     * @param altitudeMap                   - the {@link AltitudeMap} instance.
     * @param focusPointGlobalCoordinates   - the focus point global coordinates.
     */
    void updateFacilityCoordinates(FacilityParticipant participant,
                                   AltitudeMap altitudeMap,
                                   Point focusPointGlobalCoordinates) {
        if (altitudeMap == null || participant == null || focusPointGlobalCoordinates == null) {
            log.error("AltitudeMap: {}, Participant: {}, FocusPoint: {}", altitudeMap, participant, focusPointGlobalCoordinates);
            throw new RuntimeException("One or more required arguments are null.");
        }

        participant.getFacilityPartParticipants().parallelStream().forEach(partParticipant -> {
            int x = (altitudeMap.getScreenWidth() / 2 - altitudeMap.getTileWidth() / 2) - focusPointGlobalCoordinates.x;
            int y = (altitudeMap.getScreenHeight() / 2 - altitudeMap.getTileHeight() / 2) - focusPointGlobalCoordinates.y;
            x += MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                    altitudeMap.getTileWidth(),
                    partParticipant.getStartMapCoordinates().x
            );

            y += MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                    altitudeMap.getTileHeight(),
                    partParticipant.getStartMapCoordinates().y
            );

            x += MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                    altitudeMap.getTileWidth(),
                    partParticipant.getShiftTilePosition().x
            );

            y += MovementCoordinateComputator.calculateGlobalStartCoordinateOfObject(
                    altitudeMap.getTileHeight(),
                    partParticipant.getShiftTilePosition().y
            );

            partParticipant.updateCurrentGlobalCoordinates(x, y);
        });
    }

}
