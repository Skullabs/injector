package injector.apt;

import injector.*;

public class NonSingletonWithConstructorInjectorFactory implements Factory<injector.apt.NonSingletonWithConstructor> {

    public NonSingletonWithConstructor create(Injector context, Class targetClass) {
    return new NonSingletonWithConstructor(

context.instanceOf( injector.apt.NonSingletonService.class, targetClass )

,
context.instanceOf( injector.apt.SingletonService.class, targetClass )

    );
    }

    public Class<NonSingletonWithConstructor> getExposedType() {
        return NonSingletonWithConstructor.class;
    }
}
