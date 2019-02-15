import com.alta.mediator.Mediator;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class AppInjector extends AbstractModule {

    @Provides
    public Mediator createMediator() {
        return new Mediator();
    }
}
