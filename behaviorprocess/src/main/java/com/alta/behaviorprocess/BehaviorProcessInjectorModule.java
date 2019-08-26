package com.alta.behaviorprocess;

import com.alta.behaviorprocess.controller.globalEvent.GlobalEventController;
import com.alta.behaviorprocess.controller.globalEvent.GlobalEventControllerImpl;
import com.alta.behaviorprocess.controller.localMap.LocalMapController;
import com.alta.behaviorprocess.controller.localMap.LocalMapControllerImpl;
import com.alta.behaviorprocess.controller.scenario.ScenarioController;
import com.alta.behaviorprocess.controller.scenario.ScenarioControllerImpl;
import com.alta.behaviorprocess.service.Behavior;
import com.alta.behaviorprocess.service.interaction.InteractionBehavior;
import com.alta.behaviorprocess.service.interaction.InteractionScenarioData;
import com.alta.behaviorprocess.sync.DataStorage;
import com.alta.behaviorprocess.service.quest.MainQuestBehavior;
import com.alta.behaviorprocess.service.quest.QuestScenarioData;
import com.alta.behaviorprocess.shared.scenario.ScenarioFactory;
import com.alta.behaviorprocess.shared.scenario.senarioEffects.EffectFactory;
import com.alta.behaviorprocess.sync.SynchronizationManager;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

public class BehaviorProcessInjectorModule extends AbstractModule {

    @Override
    protected void configure() {
        this.configureFactories();
    }

    private void configureFactories() {
        bind(DataStorage.class).in(Singleton.class);
        bind(SynchronizationManager.class).in(Singleton.class);

        bind(ScenarioController.class).to(ScenarioControllerImpl.class).in(Singleton.class);
        bind(LocalMapController.class).to(LocalMapControllerImpl.class).in(Singleton.class);
        bind(GlobalEventController.class).to(GlobalEventControllerImpl.class).in(Singleton.class);

        install(new FactoryModuleBuilder().build(EffectFactory.class));
        install(new FactoryModuleBuilder().build(ScenarioFactory.class));

        bind(new TypeLiteral<Behavior<InteractionScenarioData>>(){})
                .annotatedWith(Names.named("interactionBehavior"))
                .to(InteractionBehavior.class);

        bind(new TypeLiteral<Behavior<QuestScenarioData>>(){})
                .annotatedWith(Names.named("mainQuestBehavior"))
                .to(MainQuestBehavior.class);
    }

}
