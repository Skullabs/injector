package injector;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

public class DirectCyclicDependencyException extends RuntimeException {

    final List<Class> targetClasses;

    public DirectCyclicDependencyException(Class targetClass, Class causedByClass) {
        super(generateMessage(targetClass, causedByClass), null, false, false);
        this.targetClasses = Arrays.asList(targetClass, causedByClass);
    }

    static String generateMessage(Class targetClass, Class causedByClass){
        return "\nThere's a direct cyclic dependency between the following classes:\n"
             + " - " + targetClass + "\n"
             + " - " + causedByClass + "\n";
    }
}

@RequiredArgsConstructor
class FirstStackOverflowOccurrence extends RuntimeException {

    final Class currentTargetClass;

    @Override
    public String toString() {
        return "FirstStackOverflowOccurrence{" +
                "currentTargetClass=" + currentTargetClass +
                '}';
    }
}

@RequiredArgsConstructor
class SecondStackOverflowOccurrence extends RuntimeException {

    final Class targetClass;
    final Class currentTargetClass;
}
