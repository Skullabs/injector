package injector.sample;

import injector.Producer;
import injector.Singleton;

@Singleton
public class ClientAProducer {

    @Producer public ClientA newClientA(Class targetClass){
        return new ClientA("localhost", 8080);
    }
}
