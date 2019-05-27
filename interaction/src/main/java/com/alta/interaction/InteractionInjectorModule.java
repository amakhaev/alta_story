package com.alta.interaction;

import com.alta.interaction.scenario.Interaction;
import com.alta.interaction.scenario.EffectFactory;
import com.alta.interaction.scenario.ScenarioFactory;
import com.alta.interaction.scenario.Scenario;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class InteractionInjectorModule extends AbstractModule {

    @Override
    protected void configure() {
        this.configureFactories();
    }

    private void configureFactories() {
        install(new FactoryModuleBuilder().implement(Interaction.class, Interaction.class).build(EffectFactory.class));
        install(new FactoryModuleBuilder().implement(Scenario.class, Scenario.class).build(ScenarioFactory.class));
    }

}
