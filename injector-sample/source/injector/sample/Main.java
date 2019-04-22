package injector.sample;

import injector.*;

public class Main {

    public static void main( String[] args) {
        Injector.create()
            .instanceOf( ServiceA.class )
            .notifyServiceB()
        ;
    }
}
