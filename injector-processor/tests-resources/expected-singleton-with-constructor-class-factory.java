package injector.apt.example;

import injector.*;

@javax.annotation.processing.Generated("injector.apt.InjectorProcessor")
public class SingletonWithConstructorInjectorFactory implements Factory<SingletonWithConstructor> {

    volatile private SingletonWithConstructor instance;

    public SingletonWithConstructor create(Injector context, Class targetClass) {
        if ( instance == null )
            synchronized (this) {
                if ( instance == null )
                    instance = newInstance( context );
            }
        return instance;
    }

    public Class<SingletonWithConstructor> getExposedType() {
        return SingletonWithConstructor.class;
    }

    private SingletonWithConstructor newInstance(Injector context){
        return new SingletonWithConstructor(

context.instanceOf( injector.apt.example.NonSingletonService.class, getExposedType() )

,
context.instanceOf( injector.apt.example.NonSingletonWithConstructor.class, getExposedType() )

        );
    }
}