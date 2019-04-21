package com.alta.engine.model;

import com.alta.engine.model.interaction.InteractionEngineModel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Provides the data model that describes the interactions available on scene.
 */
@Getter
@Builder
public class InteractionDataModel {

    /**
     * Provides the list of interaction between participants.
     */
    private final List<InteractionEngineModel> interactions;

}
