package injector;

import injector.generated.SingletonService;
import injector.generated.exposed.*;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InjectionContextTest {

    final Injector context = Injector.create();

    @DisplayName( "Cannot retrieve instance of a non-registered object" )
    @Test void instanceOf(){
        assertThrows( IllegalArgumentException.class,
            () -> context.instanceOf(Integer.class),
            "No factory defined for " + Integer.class.getCanonicalName() );
    }

    @DisplayName( "Can find registered factories from SPI and can instantiate types" )
    @Test void registerFactoryOf(){
        val instance = context.instanceOf( SingletonService.class );
        assertNotNull( instance );
    }

    @DisplayName( "Singleton factories should always return the same instance" )
    @Test void instanceOf1(){
        val instance = context.instanceOf( SingletonService.class );
        assertNotNull( instance );
        assertSame( instance, context.instanceOf( SingletonService.class ) );
    }

    @DisplayName("A Singleton should be able to inject of implementation of a given type")
    @Test void instanceOf2(){
        val instance = context.instanceOf( ServiceWhichRequiresAllMathOperations.class );
        assertEquals(0, instance.getTotal(),
        "Calculated total using all injected dependencies failed" );
    }

    @DisplayName("Is able to load all implementations of a given type")
    @Test void instancesExposedAs(){
        val instances = context.instancesExposedAs(MathOperation.class);
        val iterator = instances.iterator();
        assertTrue( iterator.hasNext() );
        assertTrue( iterator.next() instanceof Sum);
        assertTrue( iterator.next() instanceof Minus);
    }

    @DisplayName("SHOULD be able to retrieve the first available instance from exposed interface")
    @Test void instanceOf3(){
        val instance = context.instanceOf( MathOperation.class );
        assertNotNull( instance );
        assertTrue( instance instanceof Sum );
    }
}
