package injector.apt;

import injector.*;

@javax.annotation.processing.Generated("injector.apt.InjectorProcessor")
public class SingletonServiceInjectorFactory implements Factory<SingletonService> {

    volatile private SingletonService instance;

    public SingletonService create(Injector context, Class targetClass) {
        if ( instance == null )
            synchronized (this) {
                if ( instance == null )
                    instance = newInstance( context );
            }
        return instance;
    }

    public Class<SingletonService> getExposedType() {
        return SingletonService.class;
    }

    private SingletonService newInstance(Injector context){
        return new SingletonService(
        );
    }
}