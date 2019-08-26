package com.alta.behaviorprocess.controller.globalEvent;

import com.alta.behaviorprocess.data.globalEvent.GlobalEventRepository;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;

import java.awt.*;

/**
 * Provides the controller handle global events.
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class GlobalEventControllerImpl implements GlobalEventController {

    private final GlobalEventRepository globalEventRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveGameState(String mapName, String actionCharacterSkin, Point actionCharacterMapCoordinate) {
        this.globalEventRepository.saveState(mapName, actionCharacterSkin, actionCharacterMapCoordinate);
    }
}
