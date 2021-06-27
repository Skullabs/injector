package injector.apt.example.calc;

import injector.AllOf;
import injector.Producer;
import injector.Singleton;

@Singleton
public class CalculatorNonManagerProducer {

    @Producer
    public CalculatorNonManaged create(
        @AllOf(MathOperation.class) Iterable<MathOperation> operations
    ){
        return new CalculatorNonManaged(operations);
    }
}
