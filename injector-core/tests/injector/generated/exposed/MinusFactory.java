package injector.generated.exposed;

import injector.Factory;
import injector.Injector;

public class MinusFactory implements Factory<Minus> {

    @Override
    public Minus create(Injector context, Class targetClass) {
        return new Minus();
    }

    @Override
    public Class<Minus> getExposedType() {
        return Minus.class;
    }
}
