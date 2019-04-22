package injector.generated.exposed;

import injector.*;
import injector.Injector;

public class SumFactory implements Factory<Sum> {

    @Override
    public Sum create(Injector context, Class targetClass) {
        return new Sum();
    }

    @Override
    public Class<Sum> getExposedType() {
        return Sum.class;
    }
}
