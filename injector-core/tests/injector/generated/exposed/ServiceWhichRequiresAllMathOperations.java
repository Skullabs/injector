package injector.generated.exposed;

import injector.AllOf;
import injector.Singleton;

@Singleton
public class ServiceWhichRequiresAllMathOperations {

    int total;

    public ServiceWhichRequiresAllMathOperations(
            @AllOf( MathOperation.class ) Iterable<MathOperation> mathOperations
    ) {
        total = 0;
        for (MathOperation operation : mathOperations) {
            total = operation.apply( total, 1 );
        }
    }
}
