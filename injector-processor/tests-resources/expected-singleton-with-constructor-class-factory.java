package injector.apt;

import injector.*;

public class SingletonWithConstructorFactory implements Factory<SingletonWithConstructor> {

    volatile private SingletonWithConstructor instance;

    @Override
    public SingletonWithConstructor create(Injector context) {
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

    private SingletonWithConstructor newInstance(Injector context){
        return new SingletonWithConstructor(

context.instanceOf( injector.apt.NonSingletonService.class )

,
context.instanceOf( injector.apt.NonSingletonWithConstructor.class )

        );
    }
}