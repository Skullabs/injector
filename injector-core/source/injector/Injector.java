package injector;

import lombok.Setter;
import lombok.experimental.*;
import lombok.val;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public interface Injector {

    <T> Factory<T> factoryOf(Class<T> clazz);

    default <T> T instanceOf(Class<T> clazz) {
        return instanceOf(clazz, null);
    }

    <T> T instanceOf(Class<T> clazz, Class targetClass);

    <T> Injector registerFactoryOf(Class<T> type, Factory<T> factory);

    <T> Iterable<T> instancesExposedAs(Class<T> clazz);

    Injector setLogger(Consumer<String> loggerListener);

    static Injector create() {
        return create(true);
    }

    static Injector create( boolean runJobs ) {
        val injector = new DefaultInjector();

        if ( runJobs ) {
            val jobs = injector.instancesExposedAs( Job.class );
            for ( val job : jobs ) {
                try {
                    job.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return injector;
    }

    @Accessors(chain = true)
    class DefaultInjector implements Injector {
        private final Map<Class, Factory> cache = new HashMap<>();
        private Map<Class, Iterable> exposed;

        @Setter
        Consumer<String> logger = new StdOutErrorPrinter();

        public DefaultInjector() {
            registerFactoryOf( Injector.class, new DefaultInjectorFactory() );
            val factories = ServiceLoader.load(Factory.class);
            for (val factory : factories)
                registerFactoryOf(factory.getExposedType(), factory);
        }

        private Map<Class, Iterable> getExposed(){
            if ( exposed == null )
                synchronized (this){
                    if ( exposed == null )
                        exposed = readExposedClasses();
                }
            return exposed;
        }

        private Map<Class, Iterable> readExposedClasses(){
            val exposed = new HashMap<Class, Iterable>();
            val loaders = ServiceLoader.load(ExposedServicesLoader.class);
            for (val loader : loaders) {
                exposed.put(loader.getExposedType(), loader.load(this));
            }
            return exposed;
        }

        public <T> Factory<T> factoryOf(Class<T> clazz) {
            return cache.get(clazz);
        }

        public <T> T instanceOf(Class<T> clazz, Class targetClass) {
            val t = factoryOf(clazz);
            if (t != null)
                return t.create(this, targetClass);

            throw new IllegalArgumentException("No implementation available for " + clazz.getCanonicalName());
        }

        public <T> Injector registerFactoryOf(Class<T> type, Factory<T> factory) {
            val oldValue = cache.put(type, factory);
            if (oldValue != null)
                logger.accept(
                        "More than one Factory defined for " + type.getCanonicalName() + ". " + String.join(",",
                                oldValue.getClass().getCanonicalName(), factory.getClass().getCanonicalName()));
            return this;
        }

        public <T> Iterable<T> instancesExposedAs(Class<T> clazz) {
            val ts = getExposed().get(clazz);
            if (ts != null)
                return ts;
            return Collections.emptyList();
        }

        private class StdOutErrorPrinter implements Consumer<String> {

            @Override
            public void accept(String s) {
                System.out.println( s );
            }
        }
    }

    class DefaultInjectorFactory implements Factory<Injector> {

        @Override
        public Injector create(Injector context, Class target) {
            return context;
        }

        @Override
        public Class<Injector> getExposedType() {
            return Injector.class;
        }
    }
}
