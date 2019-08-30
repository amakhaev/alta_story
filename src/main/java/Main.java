import com.alta.behaviorprocess.BehaviorProcessInjectorModule;
import com.alta.dao.DaoInjectorModule;
import com.alta.engine.EngineInjectorModule;
import com.alta.mediator.Mediator;
import com.alta.mediator.MediatorInjectorModule;
import com.alta.scene.SceneInjectorModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new MediatorInjectorModule(),
                new DaoInjectorModule(),
                new EngineInjectorModule(),
                new SceneInjectorModule(),
                new BehaviorProcessInjectorModule()
        );
        injector.getInstance(Mediator.class).loadSavedGameAndStart();
    }
}
