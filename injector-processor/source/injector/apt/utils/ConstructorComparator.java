package injector.apt.utils;

import generator.apt.SimplifiedAST;
import generator.apt.SimplifiedAST.Method;
import injector.Exposed;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ConstructorComparator {

    int compare(Method m1, Method m2) {
        int result = compareByAnnotation(m1, m2);
        if (result == 0)
            result = compareByNumberOfParameters(m1, m2);
        return result;
    }

    int compareByAnnotation(Method m1, Method m2){
        return Boolean.compare(
            m2.getAnnotation(Exposed.class) != null,
            m1.getAnnotation(Exposed.class) != null
        );
    }
    
    int compareByNumberOfParameters(Method m1, Method m2){
        return Integer.compare(
            m2.getParameters().size(),
            m1.getParameters().size()
        );
    }
}
