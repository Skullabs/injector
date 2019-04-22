package injector;

public interface Factory<T> {

    T create( Injector context );

    Class<T> getExposedType();
}
