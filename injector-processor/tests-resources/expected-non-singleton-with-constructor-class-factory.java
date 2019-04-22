package injector.apt;

import injector.*;

public class NonSingletonWithConstructorFactory implements Factory<injector.apt.NonSingletonWithConstructor> {

    @Override
    public NonSingletonWithConstructor create(Injector context) {
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
