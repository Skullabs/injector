package injector.sample;

import injector.AllOf;
import injector.Singleton;
import lombok.val;

@Singleton
public class ServiceB {

    final ClientA clientA;
    final Iterable<ClientB> clientBs;

    public ServiceB(
            ClientA clientA,
            @AllOf(ClientB.class) Iterable<ClientB> clientBs) {
        this.clientA = clientA;
        this.clientBs = clientBs;
    }

    public void notifyClientA(){
        clientA.notifyMe();
        for ( val clientB : clientBs) {
            clientB.callMe();
        }
    }
}
