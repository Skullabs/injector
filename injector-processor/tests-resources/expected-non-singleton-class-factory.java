package injector.apt;

import injector.*;

@javax.annotation.processing.Generated("injector.apt.InjectorProcessor")
public class NonSingletonServiceInjectorFactory implements Factory<injector.apt.NonSingletonService> {

    public NonSingletonService create(Injector context, Class targetClass) {
    return new NonSingletonService(
    );
    }

    public Class<NonSingletonService> getExposedType() {
        return NonSingletonService.class;
    }
}
