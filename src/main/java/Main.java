import com.alta.mediator.Mediator;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new AppInjector());
        Mediator mediator = injector.getInstance(Mediator.class);
        mediator.loadSavedGameAndStart();

        /*EventStream<String> stream = new EventStream<>();
        stream.setHandleEvents(new EventHandler<GenericEvent<String>>() {

            @Override
            public void onEvent(GenericEvent<String> event, long sequence, boolean endOfBatch) throws Exception {
                System.out.println(event.getData());
            }
        });
        stream.start();

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                while (true) {
                    stream.publishEvent("test");
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });*/
    }

}
