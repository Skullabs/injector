package injector.apt;

import injector.*;

public class SingletonWithConstructorInjectorFactory implements Factory<SingletonWithConstructor> {

    volatile private SingletonWithConstructor instance;

    public SingletonWithConstructor create(Injector context, Class targetClass) {
        if ( instance == null )
            synchronized (this) {
                if ( instance == null )
                    instance = newInstance( context, targetClass );
            }
        return instance;
    }

    public Class<SingletonWithConstructor> getExposedType() {
        return SingletonWithConstructor.class;
    }

    private SingletonWithConstructor newInstance(Injector context, Class targetClass){
        return new SingletonWithConstructor(

context.instanceOf( injector.apt.NonSingletonService.class, targetClass )

,
context.instanceOf( injector.apt.NonSingletonWithConstructor.class, targetClass )

        );
    }
}