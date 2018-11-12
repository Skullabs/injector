package injector.apt;

import injector.Factory;
import injector.InjectionContext;

public class NonSingletonWithConstructorFactory implements Factory<injector.apt.NonSingletonWithConstructor> {

    @Override
    public NonSingletonWithConstructor create(InjectionContext context) {
    return new NonSingletonWithConstructor(

context.instanceOf( injector.apt.NonSingletonService.class )

,
context.instanceOf( injector.apt.SingletonService.class )

    );
    }

    @Override
    public Class<NonSingletonWithConstructor> getExposedType() {
        return NonSingletonWithConstructor.class;
    }
}
