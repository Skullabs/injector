package injector.apt.utils;

import com.google.common.collect.ImmutableList;
import generator.apt.SimplifiedAST;
import generator.apt.SimplifiedAST.Annotation;
import generator.apt.SimplifiedAST.Element;
import generator.apt.SimplifiedAST.Method;
import generator.apt.SimplifiedAST.Type;
import injector.Exposed;
import lombok.val;
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
            new Annotation().setType(Exposed.class.getCanonicalName())
        ));

    Method m2 = new Method()
        .setConstructor(true)
        .setParameters(asList(new Element(), new Element()));

    Type type = new Type().setMethods(new ArrayList<>(asList(m1, m2)));

    @Test
    void remove() {
        val expected = singletonList(m1);
        DuplicatedConstructorRemover.remove(type);
        assertEquals(expected, type.getMethods());
    }
}