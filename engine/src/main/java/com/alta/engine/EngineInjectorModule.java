package com.alta.engine;

import com.alta.computator.model.event.ComputatorEvent;
import com.alta.engine.core.asyncTask.AsyncTaskManager;
import com.alta.engine.facade.EffectListenerImpl;
import com.alta.engine.facade.FrameStageFacade;
import com.alta.engine.facade.FrameStageListener;
import com.alta.engine.facade.InteractionFacade;
import com.alta.engine.presenter.FrameStagePresenter;
import com.alta.engine.view.FrameStageView;
import com.alta.engine.view.ViewFactory;
import com.alta.eventStream.EventProducer;
import com.alta.interaction.interactionOnMap.InteractionOnMapManager;
import com.alta.interaction.scenario.EffectListener;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

public class EngineInjectorModule extends AbstractModule {

    @Override
    protected void configure() {
        this.configureInstances();
        this.configureFactories();
    }

    private void configureInstances() {
        bind(FrameStageFacade.class).in(Singleton.class);
        bind(InteractionFacade.class).in(Singleton.class);

        bind(FrameStagePresenter.class).in(Singleton.class);
        bind(FrameStagePresenter.class).in(Singleton.class);

        bind(FrameStageListener.class).in(Singleton.class);

        bind(AsyncTaskManager.class).in(Singleton.class);
        bind(InteractionOnMapManager.class).in(Singleton.class);

        bind(new TypeLiteral<EventProducer<ComputatorEvent>>(){})
                .annotatedWith(Names.named("computatorActionProducer"))
                .toInstance(new EventProducer<>());

        bind(EffectListener.class).to(EffectListenerImpl.class);
    }

    private void configureFactories() {
        install(new FactoryModuleBuilder().implement(FrameStageView.class, FrameStageView.class).build(ViewFactory.class));
    }

}
