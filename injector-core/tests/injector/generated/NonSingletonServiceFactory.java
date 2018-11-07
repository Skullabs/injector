package injector.generated;

import injector.Factory;
import injector.InjectionContext;

public class NonSingletonServiceFactory implements Factory<NonSingletonService> {

    @Override
    public NonSingletonService create(InjectionContext context) {
        return new NonSingletonService();
    }

    @Override
    public Class<NonSingletonService> getExposedType() {
        return NonSingletonService.class;
    }
}
