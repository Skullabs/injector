package injector;

import injector.scenarios.cyclic.Scenario1;
import injector.scenarios.cyclic.Scenario1Loaders;
import injector.scenarios.cyclic.Scenario2;
import injector.scenarios.cyclic.Scenario2Loaders;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

public class CyclicDependencyTest {
    
    final Injector injector = Injector.create();
    
    @BeforeEach
    void configFactoriesForTheTests(){
        injector.registerFactoryOf(Scenario1.Parent.class, new Scenario1Loaders.ParentFactory());
        injector.registerExposedServiceLoaderOf(Scenario1.Child.class, new Scenario1Loaders.ChildFactory());

        injector.registerFactoryOf(Scenario2.Parent.class, new Scenario2Loaders.ParentFactory());
        injector.registerFactoryOf(Scenario2.Child.class, new Scenario2Loaders.ChildFactory());
    }

    @DisplayName("SHOULD detect allow Graph Cyclic Dependency between Parent and Child")
    @Test void instanceOf1(){
        val parent = injector.instanceOf(Scenario1.Parent.class);
        val children = toList(parent.children);
        assertEquals(1, children.size());
        
        val child = children.get(0);
        assertNotNull(child);
        assertEquals(parent, child.parent);
    }

    <T> List<T> toList(Iterable<T> data) {
        val list = new ArrayList<T>();
        for (T datum : data)
            list.add(datum);
        return list;
    }

    @DisplayName("SHOULD detect Direct Cyclic Dependency between Parent and Child")
    @Test void instanceOf2(){
        try {
            injector.instanceOf(Scenario2.Parent.class);
            fail("SHOULD detect Direct Cyclic Dependency between Parent and Child");
        } catch (DirectCyclicDependencyException cause) {
            cause.printStackTrace();
            cause.targetClasses.sort(Comparator.comparing(Class::getCanonicalName));

            val expectedInvolvedClasses = asList(Scenario2.Child.class, Scenario2.Parent.class);
            assertEquals(expectedInvolvedClasses, cause.targetClasses);
        }
    }
}
