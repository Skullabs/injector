package injector.apt.exposed;

public class CalculatorNonManaged {

    final Iterable<MathOperation> operations;

    public CalculatorNonManaged( Iterable<MathOperation> operations ){
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
