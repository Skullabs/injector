package injector.apt;

import injector.apt.example.*;
import injector.apt.example.calc.*;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.lang.model.SourceVersion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InjectorFactoryClassGeneratorTest {

    InjectorProcessor injector = new InjectorProcessor();
    
    @BeforeEach void ensureRunningOnJre9OrSuperior(){
        val current = SourceVersion.latestSupported().ordinal();
        val lastUnsupported = SourceVersion.RELEASE_8.ordinal();
        assertTrue(current > lastUnsupported, "Should be running on Jdk9 or superior");
    }

    @BeforeEach
    void cleanUpPreviouslyGeneratedResources() {
        APT.cleanUpPreviouslyGeneratedResources();
    }

    @SneakyThrows
    @DisplayName("Can generate factory for a non-singleton class")
    @Test void process() {
        APT.runner()
            .run(injector, APT.asSource( APT.testFile(NonSingletonService.class) ))
            .failInCaseOfError();

        val nonSingleton = APT.outputGeneratedClass( "injector.apt.example.NonSingletonServiceInjectorFactory" );
        val nonSingletonAsString = APT.readFileAsString(nonSingleton);
        val expectedContent = APT.testResourceAsString( "expected-non-singleton-class-factory.java" );
        assertEquals( expectedContent, nonSingletonAsString );
    }

    @SneakyThrows
    @DisplayName("Can generate factory for a non-singleton with constructor class")
    @Test void process2() {
        APT.runner()
            .run(injector, APT.asSource( APT.testFile(NonSingletonWithConstructor.class) ))
            .failInCaseOfError();

        val nonSingleton = APT.outputGeneratedClass( "injector.apt.example.NonSingletonWithConstructorInjectorFactory" );
        val nonSingletonAsString = APT.readFileAsString(nonSingleton);
        val expectedContent = APT.testResourceAsString( "expected-non-singleton-with-constructor-class-factory.java" );
        assertEquals( expectedContent, nonSingletonAsString );
    }

    @SneakyThrows
    @DisplayName("Can generate factory for a singleton class")
    @Test void process3() {
        APT.runner()
            .run(injector, APT.asSource( APT.testFile(SingletonService.class) ))
            .failInCaseOfError();

        val nonSingleton = APT.outputGeneratedClass( "injector.apt.example.SingletonServiceInjectorFactory" );
        val nonSingletonAsString = APT.readFileAsString(nonSingleton);
        val expectedContent = APT.testResourceAsString( "expected-singleton-class-factory.java" );
        assertEquals( expectedContent, nonSingletonAsString );
    }

    @SneakyThrows
    @DisplayName("Can generate factory for a singleton with constructor class")
    @Test void process4() {
        APT.runner()
            .run(injector, APT.asSource( APT.testFile(SingletonWithConstructor.class) ))
            .failInCaseOfError();

        val nonSingleton = APT.outputGeneratedClass( "injector.apt.example.SingletonWithConstructorInjectorFactory" );
        val nonSingletonAsString = APT.readFileAsString(nonSingleton);
        val expectedContent = APT.testResourceAsString( "expected-singleton-with-constructor-class-factory.java" );
        assertEquals( expectedContent, nonSingletonAsString );
    }

    @SneakyThrows
    @DisplayName("Can generate factories from producer methods")
    @Test void process5() {
        APT.runner().run(injector,
            APT.asSource( APT.testFile(ProducerOfImportantServices.class) ),
            APT.asSource( APT.testFile(ImportantService.class) ),
            APT.asSource( APT.testFile(SecondImportantService.class) ))
                .failInCaseOfError();

        val nonSingleton = APT.outputGeneratedClass( "injector.apt.example.ImportantServiceInjectorFactory" );
        val nonSingletonAsString = APT.readFileAsString(nonSingleton);
        val expectedContent = APT.testResourceAsString( "expected-producer-class-factory.java" );
        assertEquals( expectedContent, nonSingletonAsString );
    }

    @SneakyThrows
    @DisplayName("Can inject all exposed objects from a given interface")
    @Test void process7() {
        APT.runner().run(injector,
            APT.asSource( APT.testFile(Sum.class) ),
            APT.asSource( APT.testFile(Minus.class) ),
            APT.asSource( APT.testFile(Calculator.class) ),
            APT.asSource( APT.testFile(CalculatorNonManaged.class) ),
            APT.asSource( APT.testFile(CalculatorNonSingleton.class) ),
            APT.asSource( APT.testFile(CalculatorNonManagerProducer.class) ))
                .failInCaseOfError();

        val nonSingleton = APT.outputGeneratedClass( "injector.apt.example.calc.CalculatorInjectorFactory" );
        val nonSingletonAsString = APT.readFileAsString(nonSingleton);
        val expectedContent = APT.testResourceAsString( "expected-factory-with-exposed-objects-injected.java" );
        assertEquals( expectedContent, nonSingletonAsString );
    }

    @SneakyThrows
    @DisplayName("Can generate all the factories above")
    @Test void process10() {
        APT.runner().run(injector,
            APT.asSource( APT.testFile(SingletonService.class) ),
            APT.asSource( APT.testFile(SingletonWithConstructor.class) ),
            APT.asSource( APT.testFile(NonSingletonService.class) ),
            APT.asSource( APT.testFile(NonSingletonWithConstructor.class) ),
            APT.asSource( APT.testFile(ProducerOfImportantServices.class) ),
            APT.asSource( APT.testFile(Sum.class) ),
            APT.asSource( APT.testFile(Minus.class) ),
            APT.asSource( APT.testFile(Calculator.class) ),
            APT.asSource( APT.testFile(CalculatorNonManaged.class) ),
            APT.asSource( APT.testFile(CalculatorNonSingleton.class) ),
            APT.asSource( APT.testFile(CalculatorNonManagerProducer.class) ))
                .failInCaseOfError();

        val nonSingleton = APT.outputGeneratedClass( "injector.apt.example.SingletonWithConstructorInjectorFactory" );
        val nonSingletonAsString = APT.readFileAsString(nonSingleton);
        val expectedContent = APT.testResourceAsString( "expected-singleton-with-constructor-class-factory.java" );
        assertEquals( expectedContent, nonSingletonAsString );
    }
}
