package com.alta.dao;

import com.alta.dao.di.DatabaseConnectionProvider;
import com.alta.dao.domain.actor.ActorService;
import com.alta.dao.domain.actor.ActorServiceImpl;
import com.alta.dao.domain.facility.FacilityService;
import com.alta.dao.domain.facility.FacilityServiceImpl;
import com.alta.dao.domain.interaction.InteractionService;
import com.alta.dao.domain.interaction.InteractionServiceImpl;
import com.alta.dao.domain.map.MapService;
import com.alta.dao.domain.map.MapServiceImpl;
import com.alta.dao.domain.preservation.PreservationService;
import com.alta.dao.domain.preservation.PreservationServiceImpl;
import com.alta.dao.domain.preservation.TemporaryDataPreservationService;
import com.alta.dao.domain.preservation.TemporaryDataPreservationServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.j256.ormlite.support.ConnectionSource;

public class DaoInjectorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MapService.class).to(MapServiceImpl.class);
        bind(PreservationService.class).to(PreservationServiceImpl.class);
        bind(TemporaryDataPreservationService.class).to(TemporaryDataPreservationServiceImpl.class);
        bind(FacilityService.class).to(FacilityServiceImpl.class);
        bind(ActorService.class).to(ActorServiceImpl.class);
        bind(InteractionService.class).to(InteractionServiceImpl.class);

        bind(ConnectionSource.class).toProvider(DatabaseConnectionProvider.class).in(Singleton.class);
    }

}
