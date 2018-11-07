package injector;

import injector.generated.SingletonService;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InjectionContextTest {

    final InjectionContext context = new InjectionContext();

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

}
