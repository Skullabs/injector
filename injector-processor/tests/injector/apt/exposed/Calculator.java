package injector.apt.exposed;

import injector.AllOf;
import injector.Singleton;

@Singleton
public class Calculator {

    final Iterable<MathOperation> operations;

    public Calculator( @AllOf(MathOperation.class) Iterable<MathOperation> operations ){
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
