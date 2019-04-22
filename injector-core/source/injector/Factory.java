package injector;

public interface Factory<T> {

    T create( Injector context, Class target );

    Class<T> getExposedType();
}
