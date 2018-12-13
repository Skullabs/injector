package injector.sample;

import injector.Singleton;

@Singleton
public class ServiceA {

    private final ServiceB serviceB;

    public ServiceA(ServiceB serviceB) {
        this.serviceB = serviceB;
    }

    public void notifyServiceB(){
        serviceB.notifyClientA();
    }
}
