package injector;

import lombok.Setter;
import lombok.val;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public interface Injector {

    <T> Factory<T> factoryOf(Class<T> clazz);

    <T> T instanceOf(Class<T> clazz);

    <T> Injector registerFactoryOf(Class<T> type, Factory<T> factory);

    <T> Iterable<T> instancesExposedAs(Class<T> clazz);

    static Injector create() {
        return new DefaultInjector();
    }

    class DefaultInjector implements Injector {
        final Map<Class, Factory> cache = new HashMap<>();
        final Map<Class, Iterable> exposed = new HashMap<>();

        @Setter
        Consumer<String> logger = new StdOutErrorPrinter();

        public DefaultInjector() {
            val factories = ServiceLoader.load(Factory.class);
            for (val factory : factories)
                registerFactoryOf(factory.getExposedType(), factory);

            val loaders = ServiceLoader.load(ExposedServicesLoader.class);
            for (val loader : loaders) {
                exposed.put(loader.getExposedType(), loader.load(this));
            }
        }

        public <T> Factory<T> factoryOf(Class<T> clazz) {
            return cache.get(clazz);
        }

        public <T> T instanceOf(Class<T> clazz) {
            val t = factoryOf(clazz);
            if (t != null)
                return t.create(this);

            throw new IllegalArgumentException("No factory defined for " + clazz.getCanonicalName());
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
            val ts = exposed.get(clazz);
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
}
