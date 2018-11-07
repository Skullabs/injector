package injector.apt;

import injector.Factory;
import injector.InjectionContext;

public class SingletonServiceFactory implements Factory<SingletonService> {

    volatile private SingletonService instance;

    @Override
    public SingletonService create(InjectionContext context) {
        if ( instance == null )
            synchronized (this) {
                if ( instance == null )
                    instance = newInstance( context );
            }
        return instance;
    }

    @Override
    public Class<SingletonService> getExposedType() {
        return SingletonService.class;
    }

    private SingletonService newInstance(InjectionContext context){
        return new SingletonService(
        );
    }
}