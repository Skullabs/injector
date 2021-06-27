package injector;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
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

    <T> Injector registerExposedServiceLoaderOf(Class<T> type, ExposedServicesLoader<T> loader);

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
        
        private Map<Class, Iterable> exposed = new HashMap<>();
        private boolean exposedClassesLoaded = false;

        /**
         * If strict mode is turned on, it won't be tolerant to
         * any sort of failure, which might cause the service discovery
         * to stop working altogether in such cases.
         *
         * Turning strict mode off will allow Injector to completely
         * ignore such problems, allowing one to use dependency injection
         * even though something went wrong with one of its dependencies.
         *
         * This is usually the expected behaviour, as Injector will most
         * likely fail only due to security-protection exception (e.g.
         * trying to generate classes for {@code java.lang} package).
         *
         * By default, strict mode is off.
         */
        @Getter @Setter
        private boolean useStrictMode = false;

        @Setter
        Consumer<String> logger = new StdErrorPrinter();

        public DefaultInjector() {
            registerFactoryOf( Injector.class, new DefaultInjectorFactory() );
            val factories = ServiceLoader.load(Factory.class);
            for (val factory : factories)
                registerFactoryOf(factory.getExposedType(), factory);
        }

        private Map<Class, Iterable> getExposed(){
            if ( !exposedClassesLoaded )
                synchronized (this){
                    if ( !exposedClassesLoaded ) {
                        exposedClassesLoaded = true;
                        exposed.putAll(readExposedClasses());
                    }
                }
            return exposed;
        }

        private Map<Class, Iterable> readExposedClasses(){
            val exposed = new HashMap<Class, Iterable>();
            val loaders = ServiceLoader.load(ExposedServicesLoader.class);
            val iterator = loaders.iterator();
            while (iterator.hasNext())
                try {
                    val loader = iterator.next();
                    registerExposedServiceLoaderOf(loader.getExposedType(), loader);
                } catch (Exception cause) {
                    logger.accept("Could not read one of the exposed classes of " + ExposedServicesLoader.class);
                    if (useStrictMode)
                        throw cause;
                    else
                        cause.printStackTrace();
                }
            return exposed;
        }

        public <T> Factory<T> factoryOf(Class<T> clazz) {
            return cache.get(clazz);
        }

        public <T> T instanceOf(Class<T> clazz, Class targetClass) {
            val t = factoryOf(clazz);
            if (t != null)
                return tryCreateInstanceOf(t, clazz, targetClass);

            val msg = "No implementation available for " + clazz.getCanonicalName()
                    + "\n This might be the case that the class exists but is not managed by Injector."
                    + "\n Hint: try add @injector.Singleton or @injector.New in the class.\n";
            throw new IllegalArgumentException(msg);
        }

        private <T> T tryCreateInstanceOf(Factory<T> factory, Class<T> loadedClazz, Class targetClass) {
            try {
                return factory.create(this, targetClass);
            } catch (StackOverflowError cause) {
                throw new FirstStackOverflowOccurrence(loadedClazz);
            } catch (FirstStackOverflowOccurrence cause) {
                throw new DirectCyclicDependencyException(loadedClazz, cause.currentTargetClass);
            }
        }

        @Override
        public <T> Injector registerExposedServiceLoaderOf(Class<T> type, ExposedServicesLoader<T> loader) {
            val lazyLoader = new LazyExposedServiceLoader<T>( loader, this );
            getExposed().put(loader.getExposedType(), lazyLoader);
            return this;
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

        private static class StdErrorPrinter implements Consumer<String> {

            @Override
            public void accept(String s) {
                System.err.println( s );
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
