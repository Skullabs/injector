package injector.apt.exposed;

import injector.ExposedAs;
import injector.Singleton;

@Singleton
@ExposedAs(MathOperation.class)
public class Sum implements MathOperation {

    @Override
    public Integer apply(Integer integer, Integer integer2) {
        return integer + integer2;
    }
}