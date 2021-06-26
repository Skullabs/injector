package injector.apt.utils;

import generator.apt.SimplifiedAST.Annotation;
import generator.apt.SimplifiedAST.Element;
import generator.apt.SimplifiedAST.Method;
import generator.apt.SimplifiedAST.Type;
import injector.Constructor;
import injector.Exposed;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

class DuplicatedConstructorRemoverTest {

    Method m1 = (Method) new Method()
        .setParameters(singletonList(new Element()))
        .setConstructor(true)
        .setAnnotations(asList(
            new Annotation().setType(Constructor.class.getCanonicalName())
        ));

    Method m2 = new Method()
        .setConstructor(true)
        .setParameters(asList(new Element(), new Element()));


    @DisplayName("Should keep only the first constructor annotated with @Constructor")
    @Test void remove() {
        Type type = createTypeWith(m1, m2);
        DuplicatedConstructorRemover.removeFrom(type);

        val expected = singletonList(m1);
        assertEquals(expected, type.getMethods());
    }

    @DisplayName("Should always keep existing constructor when it is the only one")
    @Test void remove2() {
        Type type = createTypeWith(m2);
        DuplicatedConstructorRemover.removeFrom(type);

        val expected = singletonList(m2);
        assertEquals(expected, type.getMethods());
    }

    static Type createTypeWith(Method...methods) {
        val mutableListOfMethods = new ArrayList<>(asList(methods));
        return new Type().setMethods(mutableListOfMethods);
    }
}