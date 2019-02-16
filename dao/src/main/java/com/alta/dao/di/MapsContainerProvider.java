package com.alta.dao.di;

import com.alta.dao.domain.map.MapService;
import com.alta.dao.domain.map.MapsContainer;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class MapsContainerProvider implements Provider<MapsContainer> {

    private MapsContainer mapsContainer;

    /**
     * Initialize new instance of {@link MapsContainerProvider}
     */
    @Inject
    public MapsContainerProvider(MapService mapService) {
        this.mapsContainer = new MapsContainer(mapService.getAvailableMaps());
    }

    /**
     * Provides an instance of {@code T}.
     */
    @Override
    public MapsContainer get() {
        return this.mapsContainer;
    }
}
