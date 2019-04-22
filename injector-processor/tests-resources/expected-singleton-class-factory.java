package injector.apt;

import injector.*;

public class SingletonServiceFactory implements Factory<SingletonService> {

    volatile private SingletonService instance;

    @Override
    public SingletonService create(Injector context) {
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

    private SingletonService newInstance(Injector context){
        return new SingletonService(
        );
    }
}