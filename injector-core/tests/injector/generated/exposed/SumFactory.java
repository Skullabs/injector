package injector.generated.exposed;

import injector.Factory;
import injector.InjectionContext;

public class SumFactory implements Factory<Sum> {

    @Override
    public Sum create(InjectionContext context) {
        return new Sum();
    }

    @Override
    public Class<Sum> getExposedType() {
        return Sum.class;
    }
}
