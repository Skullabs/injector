package injector.sample;

import injector.*;
import lombok.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This unit test just glues all pieces together and
 * tries to run the code defined in the "source" folder.
 */
class GeneratedSourcesTest {

    /**
     * In order to ensure that all services has been properly
     * injected by the APT, and that we can still manually interact
     * with {@link Injector}, this test will inject an extra factory
     * for {@link ClientB}.
     */
    @Test void integrationTest() throws Exception {
        val injector = Injector.create();
        val extraClient = new ExtraClientA("127.0.0.1", 80);

        injector
            .registerFactoryOf( ClientA.class, new ExtraClientAFactory(extraClient) )
            .instanceOf( ServiceA.class )
            .notifyServiceB()
        ;

        Thread.sleep( 500L );

        assertTrue( extraClient.called );
        assertTrue( injector.instanceOf(ClientBImpl.class).called );
        assertTrue( injector.instanceOf(ClientBImpl.class).mainloopCalled );
    }
}

@RequiredArgsConstructor
class ExtraClientAFactory implements Factory<ClientA> {

    final ExtraClientA instance;

    @Override
    public ExtraClientA create(Injector context, Class targetClass) {
        return instance;
    }

    @Override
    public Class<ClientA> getExposedType() {
        return ClientA.class;
    }
}

class ExtraClientA extends ClientA {

    boolean called = false;

    public ExtraClientA(String host, int port) {
        super(host, port);
    }

    @Override
    public void notifyMe() {
        called = true;
        super.notifyMe();
    }
}