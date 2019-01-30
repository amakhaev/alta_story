import com.google.inject.Singleton;

@Singleton
public class MessageServiceImpl implements MessageService {
    @Override
    public boolean sendMessage(String msg) {
        System.out.println(msg);
        return false;
    }
}
