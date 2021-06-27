package injector.apt.example;

import injector.*;

@javax.annotation.processing.Generated("injector.apt.InjectorProcessor")
public class NonSingletonWithConstructorInjectorFactory implements Factory<injector.apt.example.NonSingletonWithConstructor> {

    public NonSingletonWithConstructor create(Injector context, Class targetClass) {
    return new NonSingletonWithConstructor(

context.instanceOf( injector.apt.example.NonSingletonService.class, getExposedType() )

,
context.instanceOf( injector.apt.example.SingletonService.class, getExposedType() )

    );
    }

    public Class<NonSingletonWithConstructor> getExposedType() {
        return NonSingletonWithConstructor.class;
    }
}
