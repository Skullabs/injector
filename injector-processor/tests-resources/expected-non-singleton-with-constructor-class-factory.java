package injector.apt;

import injector.*;

public class NonSingletonWithConstructorInjectorFactory implements Factory<injector.apt.NonSingletonWithConstructor> {

    public NonSingletonWithConstructor create(Injector context, Class targetClass) {
    return new NonSingletonWithConstructor(

context.instanceOf( injector.apt.NonSingletonService.class, getExposedType() )

,
context.instanceOf( injector.apt.SingletonService.class, getExposedType() )

    );
    }

    public Class<NonSingletonWithConstructor> getExposedType() {
        return NonSingletonWithConstructor.class;
    }
}
