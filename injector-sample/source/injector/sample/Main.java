package injector.sample;

import injector.InjectionContext;

public class Main {

    public static void main( String[] args) {
        new InjectionContext()
            .instanceOf( ServiceA.class )
            .notifyServiceB()
        ;
    }
}
