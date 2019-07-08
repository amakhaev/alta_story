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
import com.alta.dao.domain.preservation.character.CharacterPreservationService;
import com.alta.dao.domain.preservation.character.CharacterPreservationServiceImpl;
import com.alta.dao.domain.preservation.interaction.InteractionPreservationService;
import com.alta.dao.domain.preservation.interaction.InteractionPreservationServiceImpl;
import com.alta.dao.domain.preservation.map.MapPreservationService;
import com.alta.dao.domain.preservation.map.MapPreservationServiceImpl;
import com.alta.dao.domain.preservation.quest.QuestPreservationService;
import com.alta.dao.domain.preservation.quest.QuestPreservationServiceImpl;
import com.alta.dao.domain.quest.QuestListService;
import com.alta.dao.domain.quest.QuestListServiceImpl;
import com.alta.dao.domain.quest.QuestService;
import com.alta.dao.domain.quest.QuestServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.j256.ormlite.support.ConnectionSource;

public class DaoInjectorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CharacterPreservationService.class).to(CharacterPreservationServiceImpl.class);
        bind(InteractionPreservationService.class).to(InteractionPreservationServiceImpl.class);
        bind(MapPreservationService.class).to(MapPreservationServiceImpl.class);
        bind(QuestPreservationService.class).to(QuestPreservationServiceImpl.class);
        bind(PreservationService.class).to(PreservationServiceImpl.class);

        bind(MapService.class).to(MapServiceImpl.class);
        bind(FacilityService.class).to(FacilityServiceImpl.class);
        bind(ActorService.class).to(ActorServiceImpl.class);
        bind(InteractionService.class).to(InteractionServiceImpl.class);
        bind(QuestService.class).to(QuestServiceImpl.class);
        bind(QuestListService.class).to(QuestListServiceImpl.class);

        bind(ConnectionSource.class).toProvider(DatabaseConnectionProvider.class).in(Singleton.class);
    }

}
