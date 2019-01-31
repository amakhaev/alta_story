import com.alta.eventStream.EventStream;
import com.alta.eventStream.GenericEvent;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.lmax.disruptor.EventHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        System.out.println("Running");
        /*Injector injector = Guice.createInjector(new AppInjector());
        AnotherClass app = injector.getInstance(AnotherClass.class);

        System.out.println(app.messageService.sendMessage("my message"));
        app.messageService.test.sendMessage();*/

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
