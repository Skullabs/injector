package injector.apt;

import injector.apt.example.IllegallyExposedClass;
import injector.apt.example.calc.Minus;
import injector.apt.example.calc.Sum;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.lang.model.SourceVersion;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceLoaderClassGeneratorTest {

    InjectorProcessor injector = new InjectorProcessor();

    @BeforeEach
    void ensureRunningOnJre9OrSuperior(){
        val current = SourceVersion.latestSupported().ordinal();
        val lastUnsupported = SourceVersion.RELEASE_8.ordinal();
        assertTrue(current > lastUnsupported, "Should be running on Jdk9 or superior");
    }

    @BeforeEach
    void cleanUpPreviouslyGeneratedResources() {
        APT.cleanUpPreviouslyGeneratedResources();
    }

    @SneakyThrows
    @DisplayName("Can generate exposed service loader")
    @Test
    void process6() {
        APT.runner().run(injector,
                APT.asSource( APT.testFile(Sum.class) ),
                APT.asSource( APT.testFile(Minus.class) ))
                .failInCaseOfError();

        val nonSingleton = APT.outputGeneratedClass( "injector.apt.example.calc.MathOperationExposedServicesLoader" );
        val nonSingletonAsString = APT.readFileAsString(nonSingleton);
        val expectedContent = APT.testResourceAsString( "expected-exposed-service-loader.java" );
        assertEquals( expectedContent, nonSingletonAsString );
    }

    @DisplayName("Should not allow expose classes which type belongs to 'java' package")
    @Test void process8() {
        APT.runner().run(injector,
            APT.asSource( APT.testFile(IllegallyExposedClass.class) ))
                .failInCaseOfError();

        try {
            val file = APT.outputGeneratedClass( "java.util.function.SupplierExposedServicesLoader" );
            assertFalse(file.exists(), "Should not allow expose classes which type belongs to 'java' package");
        } catch (Exception cause) {
            throw cause;
        }
    }
}
