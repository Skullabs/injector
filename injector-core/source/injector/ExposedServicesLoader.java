package injector;

/**
 * A loader of exposed services.
 *
 * @param <T>
 */
public interface ExposedServicesLoader<T> {

    /**
     * Loads all services for this specific {@code T} type.
     *
     * @param context
     * @return
     */
    Iterable<T> load(InjectionContext context );

    Class<T> getExposedType();
}