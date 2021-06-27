package injector.apt.example.calc;

import injector.Exposed;
import injector.Singleton;

@Singleton
@Exposed
public class Sum implements MathOperation {

    @Override
    public Integer apply(Integer integer, Integer integer2) {
        return integer + integer2;
    }
}