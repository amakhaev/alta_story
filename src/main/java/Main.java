import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class Main {

    @Inject
    public MessageService messageService;

    public static void main(String[] args) {
        new MyService().printMessage();

        // Injector injector = Guice.createInjector(new AppInjector());
        // MessageService app = injector.getInstance(MessageService.class);

        // System.out.println(new Main().messageService.sendMessage("my message"));
    }

}
