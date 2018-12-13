package injector.sample;

import injector.ExposedAs;
import injector.Singleton;

@Singleton @ExposedAs(ClientB.class)
public class ClientBImpl implements ClientB {

    @Override
    public void callMe() {
        System.out.println( "Called ClientB" );
    }
}
