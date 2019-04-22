package injector.apt;

import injector.*;

public class NonSingletonServiceFactory implements Factory<injector.apt.NonSingletonService> {

    @Override
    public NonSingletonService create(Injector context) {
    return new NonSingletonService(
    );
    }

    @Override
    public Class<NonSingletonService> getExposedType() {
        return NonSingletonService.class;
    }
}
