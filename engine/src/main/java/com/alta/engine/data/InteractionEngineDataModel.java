package com.alta.engine.data;

import com.alta.interaction.data.InteractionModel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Provides the data data that describes the interactions available on scene.
 */
@Getter
@Builder
public class InteractionEngineDataModel {

    /**
     * Provides the list of interaction between participants.
     */
    private final List<InteractionModel> interactions;

}
