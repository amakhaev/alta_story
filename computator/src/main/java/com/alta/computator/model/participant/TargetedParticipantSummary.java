package com.alta.computator.model.participant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.*;

/**
 * Provides the summary information about participant to which acting character is aimed
 */
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class TargetedParticipantSummary {

    private final String uuid;
    private final Point mapCoordinates;
    private final ParticipatType participatType;
    private Point relatedMapCoordinates;

}
