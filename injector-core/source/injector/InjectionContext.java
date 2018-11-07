package injector;

import lombok.Setter;
import lombok.val;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public class InjectionContext {

    final Map<Class, Factory> cache = new HashMap<>();

    @Setter Consumer<String> logger = new StdOutErrorPrinter();

    public InjectionContext(){
        val factories = ServiceLoader.load( Factory.class );
        for ( val factory : factories )
            registerFactoryOf(factory.getExposedType(), factory);
    }

    public <T> Factory<T> factoryOf(Class<T> clazz ) {
        return cache.get( clazz );
    }

    public <T> T instanceOf( Class<T> clazz ) {
        val t = factoryOf(clazz);
        if ( t != null )
            return t.create( this );

        throw new IllegalArgumentException("No factory defined for " + clazz.getCanonicalName());
    }

    public <T> void registerFactoryOf( Class<T> type, Factory<T> factory ) {
        val oldValue = cache.put( type, factory );
        if ( oldValue != null )
            logger.accept(
                "More than one Factory defined for " + type.getCanonicalName() + ". " + String.join( ",",
                        oldValue.getClass().getCanonicalName(), factory.getClass().getCanonicalName() ) );
    }

    private class StdOutErrorPrinter implements Consumer<String> {

        @Override
        public void accept(String s) {
            System.out.println( s );
        }
    }
}
