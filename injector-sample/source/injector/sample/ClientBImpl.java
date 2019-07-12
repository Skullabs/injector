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
    }

    @injector.Mainloop
    void runMultipleTimes() throws InterruptedException {
        mainloopCalled = true;
        Thread.sleep(100L);
    }
}
