package com.alta.behaviorprocess.controller.localMap;

import com.alta.behaviorprocess.data.localMap.LocalMapRepository;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;

import java.awt.*;

/**
 * Provides the controller to manage local maps.
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class LocalMapControllerImpl implements LocalMapController {

    private final LocalMapRepository localMapRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public void jumpToMap(String mapName, Point mapStartCoordinate) {
        this.localMapRepository.makeJumping(mapName, mapStartCoordinate);
    }
}
