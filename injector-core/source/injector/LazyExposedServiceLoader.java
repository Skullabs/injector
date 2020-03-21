package injector;

import java.util.Iterator;

public class LazyExposedServiceLoader<T> implements Iterable<T> {

    private ExposedServicesLoader<T> original;
    private Injector injector;
    private Iterable<T> loaded;

    public LazyExposedServiceLoader(ExposedServicesLoader<T> original, Injector injector) {
        this.original = original;
        this.injector = injector;
    }

    @Override
    public Iterator<T> iterator() {
        return getLoaded().iterator();
    }

    private Iterable<T> getLoaded(){
        if (loaded == null)
            loaded = loadLoaderFromInjector();
        return loaded;
    }

    private Iterable<T> loadLoaderFromInjector() {
        try {
            return original.load(injector);
        } finally {
            injector = null;
            original = null;
        }
    }
}
