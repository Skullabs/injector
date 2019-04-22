package injector.generated.exposed;

import injector.*;

public class ServiceWhichRequiresAllMathOperationsFactory
    implements Factory<ServiceWhichRequiresAllMathOperations> {

    @Override
    public ServiceWhichRequiresAllMathOperations create(Injector context, Class targetClass) {
        return new ServiceWhichRequiresAllMathOperations(
            context.instancesExposedAs( MathOperation.class )
        );
    }

    @Override
    public Class<ServiceWhichRequiresAllMathOperations> getExposedType() {
        return ServiceWhichRequiresAllMathOperations.class;
    }
}
