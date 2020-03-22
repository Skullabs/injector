package injector.apt.utils;

import generator.apt.SimplifiedAST;
import generator.apt.SimplifiedAST.Method;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.util.stream.Collectors;

@UtilityClass
public class DuplicatedConstructorRemover {

    public void remove(SimplifiedAST.Type type) {
        val constructors = type.getMethods().stream()
            .filter(Method::isConstructor)
            .sorted(ConstructorComparator::compare)
            .collect(Collectors.toList());

        if (constructors.size() > 1) {
            constructors.remove(0);
            type.getMethods().removeAll(constructors);
        }
    }
}
