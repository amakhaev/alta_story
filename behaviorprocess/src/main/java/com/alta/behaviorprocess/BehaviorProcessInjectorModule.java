package com.alta.behaviorprocess;

import com.alta.behaviorprocess.behaviorAction.Behavior;
import com.alta.behaviorprocess.behaviorAction.interaction.InteractionBehavior;
import com.alta.behaviorprocess.behaviorAction.interaction.InteractionScenarioData;
import com.alta.behaviorprocess.core.DataStorage;
import com.alta.behaviorprocess.shared.scenario.ScenarioFactory;
import com.alta.behaviorprocess.shared.scenario.senarioEffects.EffectFactory;
import com.alta.behaviorprocess.sync.DataSynchronizer;
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
        bind(DataSynchronizer.class).in(Singleton.class);

        install(new FactoryModuleBuilder().build(EffectFactory.class));
        install(new FactoryModuleBuilder().build(ScenarioFactory.class));

        bind(new TypeLiteral<Behavior<InteractionScenarioData>>(){})
                .annotatedWith(Names.named("interactionBehavior"))
                .to(InteractionBehavior.class);
    }

}
