package injector.apt.example.calc;

import injector.ExposedAs;
import injector.Singleton;

@Singleton @ExposedAs(MathOperation.class)
public class Minus implements MathOperation {

    @Override
    public Integer apply(Integer integer, Integer integer2) {
        return integer - integer2;
    }
}