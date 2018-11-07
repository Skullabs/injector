package injector.apt;

import injector.Factory;
import injector.InjectionContext;

public class SingletonWithConstructorFactory implements Factory<SingletonWithConstructor> {

    volatile private SingletonWithConstructor instance;

    @Override
    public SingletonWithConstructor create(InjectionContext context) {
        if ( instance == null )
            synchronized (this) {
                if ( instance == null )
                    instance = newInstance( context );
            }
        return instance;
    }

    @Override
    public Class<SingletonWithConstructor> getExposedType() {
        return SingletonWithConstructor.class;
    }

    private SingletonWithConstructor newInstance(InjectionContext context){
        return new SingletonWithConstructor(

            context.instanceOf( injector.apt.NonSingletonService.class )
,
            context.instanceOf( injector.apt.NonSingletonWithConstructor.class )
        );
    }
}