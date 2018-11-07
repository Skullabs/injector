package injector.generated;

import injector.Factory;
import injector.InjectionContext;
import lombok.val;

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
        val instance = new SingletonService();
        instance.repository = context.instanceOf( NonSingletonService.class );
        return instance;
    }
}
