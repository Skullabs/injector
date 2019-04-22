package injector.apt;

import injector.*;

public class NonSingletonWithConstructorFactory implements Factory<injector.apt.NonSingletonWithConstructor> {

    @Override
    public NonSingletonWithConstructor create(Injector context, Class targetClass) {
    return new NonSingletonWithConstructor(

context.instanceOf( injector.apt.NonSingletonService.class, targetClass )

,
context.instanceOf( injector.apt.SingletonService.class, targetClass )

    );
    }

    @Override
    public Class<NonSingletonWithConstructor> getExposedType() {
        return NonSingletonWithConstructor.class;
    }
}
