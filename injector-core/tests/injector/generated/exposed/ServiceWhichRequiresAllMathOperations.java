package injector.generated.exposed;

import lombok.*;

@Getter
public class ServiceWhichRequiresAllMathOperations {

    int total = -2;

    public ServiceWhichRequiresAllMathOperations(
        Iterable<MathOperation> mathOperations
    ) {
        for (MathOperation operation : mathOperations) {
            total = operation.apply( total, 1 );
        }
        total+= 2;
    }
}
