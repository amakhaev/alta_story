package com.alta.computator.calculator.facility;

import com.alta.computator.model.participant.TargetedParticipantSummary;

import java.awt.*;

/**
 * Provides the mediator to control facilities.
 */
public interface FacilityMediator {

    /**
     * Finds the facility that has given map coordinates.
     *
     * @param mapCoordinates - the map coordinates for searching.
     * @return the {@link TargetedParticipantSummary} instance of null if not found.
     */
    TargetedParticipantSummary findFacilityTargetByMapCoordinates(Point mapCoordinates);

}
