package injector.sample;

import lombok.*;

@Data
public class ClientA {

    final String host;
    final int port;

    public void notifyMe() {
        System.out.println("Notified!");
    }
}
