package injector;

public interface Factory<T> {

    T create( InjectionContext context );

    Class<T> getExposedType();
}
