package injector.apt.example.calc;

import injector.AllOf;
import injector.New;

@New
public class CalculatorNonSingleton {

    final Iterable<MathOperation> operations;

    public CalculatorNonSingleton(@AllOf(MathOperation.class) Iterable<MathOperation> operations ){
        this.operations = operations;
    }

    public int calc( int i1, int i2 ) {
        int total = 0;
        for (MathOperation operation : operations) {
            total+= operation.apply( i1, i2 );
        }
        return total;
    }
}
