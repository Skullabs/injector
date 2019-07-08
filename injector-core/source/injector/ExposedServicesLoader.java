package injector;

/**
 * A loader of exposed services. An implementation of this interface
 * is automatically generated when classes are annotated with {@code ExposedAs}
 * annotation. Developers are encouraged to implements this interface
 * whenever they need to load multiple implementations of a particular
 * abstract class or interface.
 *
 * @param <T> The expected abstract class or interface that all loaded
 *           implementation should extend/implement.
 */
public interface ExposedServicesLoader<T> {

    /**
     * Loads all services for this specific {@code T} type.
     *
     * @param context The {@link Injector} instance that is calling this loader
     * @return The loaded instance.
     */
    Iterable<T> load(Injector context );

    /**
     * @return the expected {@code <T>} class of this loader.
     */
    Class<T> getExposedType();
}