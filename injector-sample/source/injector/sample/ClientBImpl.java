package injector.sample;

import injector.ExposedAs;
import injector.Singleton;

@ExposedAs(ClientB.class)
public class ClientBImpl implements ClientB {

    boolean called = false;
    boolean mainloopCalled = false;

    @Override
    public void callMe() {
        called = true;
        System.out.println( "Called ClientB" );
    }

    @injector.Mainloop
    void runMultipleTimes() throws InterruptedException {
        System.err.println("Running in backgroud " + this);
        mainloopCalled = true;
        Thread.sleep(100L);
    }
}
