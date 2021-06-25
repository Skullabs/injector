package injector.apt.exposed;

import java.util.*;
import injector.*;

@javax.annotation.processing.Generated("injector.apt.InjectorProcessor")
public class MathOperationExposedServicesLoader543024351 extends DefaultExposedServicesLoader<MathOperation> {

    private final static List<Class> exposedClasses = readAllClassNames( MathOperation.class );

    private Iterable<MathOperation> loadedServices;

    @Override
    public Iterable<MathOperation> load(Injector context) {
        if ( loadedServices == null )
            try {
                loadedServices = loadAllImplementations(context);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        return loadedServices;
    }

    @Override
    public Class<MathOperation> getExposedType() {
        return MathOperation.class;
    }

    @Override
    public List<Class> getExposedClasses() {
        return exposedClasses;
    }
}
