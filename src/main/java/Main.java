import com.alta.mediator.Mediator;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new AppInjector());
        Mediator mediator = injector.getInstance(Mediator.class);
        mediator.loadSavedGameAndStart();
    }

}
