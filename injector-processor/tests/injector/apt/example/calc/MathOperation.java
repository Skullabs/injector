package injector.apt.example.calc;

import java.util.function.BiFunction;

public interface MathOperation extends BiFunction<Integer, Integer, Integer> {}

