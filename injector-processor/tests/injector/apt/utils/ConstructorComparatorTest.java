package injector.apt.utils;

import generator.apt.SimplifiedAST;
import generator.apt.SimplifiedAST.Annotation;
import generator.apt.SimplifiedAST.Element;
import generator.apt.SimplifiedAST.Method;
import injector.Exposed;
import injector.apt.utils.ConstructorComparator;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

class ConstructorComparatorTest {

    Method m1 = (Method) new Method()
        .setParameters(singletonList(new Element()))
        .setAnnotations(singletonList(
            new Annotation().setType(Exposed.class.getCanonicalName())
        ));
    
    Method m2 = new Method()
        .setParameters(asList(new Element(), new Element()));

    @Test
    void compare() {
        val result = ConstructorComparator.compare(m1, m2);
        assertEquals(-1, result);
    }

    @Test
    void compareByAnnotation() {
        val result = ConstructorComparator.compareByAnnotation(m1, m2);
        assertEquals(-1, result);
    }

    @Test
    void compareByNumberOfParameters() {
        val result = ConstructorComparator.compareByNumberOfParameters(m1, m2);
        assertEquals(1, result);
    }
}