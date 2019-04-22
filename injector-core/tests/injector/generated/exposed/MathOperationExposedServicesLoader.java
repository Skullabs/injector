package injector.generated.exposed;

import injector.DefaultExposedServicesLoader;
import injector.Injector;

import java.util.List;

public class MathOperationExposedServicesLoader extends DefaultExposedServicesLoader<MathOperation> {

    static final List<Class> exposedClasses = readAllClassNames(MathOperation.class);

    @Override
    public Iterable<MathOperation> load(Injector context) {
        return loadAllImplementations( context );
    }

    @Override
    public Class<MathOperation> getExposedType() {
        return MathOperation.class;
    }

    @Override
    protected List<Class> getExposedClasses() {
        return exposedClasses;
    }
}
