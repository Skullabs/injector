package injector.apt;

import generator.apt.SimplifiedAST;
import lombok.val;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainloopMethodTest {

    @Nested class WhenReadingParsedValuesFromAnnotation {

        SimplifiedAST.Annotation annotation = createAnnotationParameters();
        MainloopMethod method = new MainloopMethod("method", Optional.of(annotation));

        @Test void numberOfInstances()
        {
            val result = method.numberOfInstances();
            assertEquals(1000, result);
        }

        @Test void gracefulShutdownTime()
        {
            val result = method.gracefulShutdownTime();
            assertEquals(1000000, result);
        }

        @Test void intervalWaitTime()
        {
            val result = method.intervalWaitTime();
            assertEquals(1000L, result);
        }

        SimplifiedAST.Annotation createAnnotationParameters()
        {
            val map = new HashMap<String, Object>();
            map.put("instances", "1000L");
            map.put("gracefulShutdownTime", "1_000_000L");
            map.put("intervalWaitTime", "1000L");

            return new SimplifiedAST.Annotation().setParameters(map);
        }
    }
}