package injector.generated.exposed;

import injector.Factory;
import injector.InjectionContext;

public class MinusFactory implements Factory<Minus> {

    @Override
    public Minus create(InjectionContext context) {
        return new Minus();
    }

    @Override
    public Class<Minus> getExposedType() {
        return Minus.class;
    }
}
