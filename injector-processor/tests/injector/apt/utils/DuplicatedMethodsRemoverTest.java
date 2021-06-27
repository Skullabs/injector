package injector.apt.utils;

import generator.apt.SimplifiedAST;
import generator.apt.SimplifiedAST.Type;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DuplicatedMethodsRemoverTest {

    SimplifiedAST.Method m1 = (SimplifiedAST.Method) new SimplifiedAST.Method()
            .setParameters(singletonList(new SimplifiedAST.Element()))
            .setName("setSurname");

    SimplifiedAST.Method m2 = (SimplifiedAST.Method)new SimplifiedAST.Method()
            .setParameters(asList(new SimplifiedAST.Element(), new SimplifiedAST.Element()))
            .setName("setName");

    @DisplayName("Should remove duplicated methods from a given Type")
    @Test void remove() {
        Type typeWithDuplicatedMethods = createTypeWith(m1, m2, m2, m1);

        DuplicatedMethodsRemover.removeFrom(typeWithDuplicatedMethods);

        Type expectedType = createTypeWith(m2, m1);
        assertEquals(expectedType, typeWithDuplicatedMethods);
    }

    static Type createTypeWith(SimplifiedAST.Method...methods) {
        val mutableListOfMethods = new ArrayList<>(asList(methods));
        return new Type().setMethods(mutableListOfMethods);
    }
}