package injector.generated;

import injector.Factory;
import injector.Injector;

public class NonSingletonServiceFactory implements Factory<NonSingletonService> {

    @Override
    public NonSingletonService create(Injector context, Class targetClass) {
        return new NonSingletonService();
    }

    @Override
    public Class<NonSingletonService> getExposedType() {
        return NonSingletonService.class;
    }
}
