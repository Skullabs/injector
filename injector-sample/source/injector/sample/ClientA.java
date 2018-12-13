package injector.sample;

import lombok.Value;

@Value
public class ClientA {

    final String host;
    final int port;

    public void notifyMe() {
        System.out.println("Notified!");
    }
}
