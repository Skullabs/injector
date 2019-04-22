package injector.apt;

import generator.apt.SimplifiedAPTRunner;
import injector.apt.exposed.*;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InjectorProcessorTest {

    InjectorProcessor injector = new InjectorProcessor();

    @SneakyThrows
    @DisplayName("Can generate factory for a non-singleton class")
    @Test void process() {
        val result = APT.runner().run(injector, APT.asSource( APT.testFile(NonSingletonService.class) ));
        result.printErrorsIfAny();

        val nonSingleton = APT.outputGeneratedClass( NonSingletonService.class.getCanonicalName() + "InjectorFactory" );
        val nonSingletonAsString = APT.readFileAsString(nonSingleton);
        val expectedContent = APT.testResourceAsString( "expected-non-singleton-class-factory.java" );
        assertEquals( expectedContent, nonSingletonAsString );
    }

    @SneakyThrows
    @DisplayName("Can generate factory for a non-singleton with constructor class")
    @Test void process2() {
        val result = APT.runner().run(injector, APT.asSource( APT.testFile(NonSingletonWithConstructor.class) ));
        result.printErrorsIfAny();

        val nonSingleton = APT.outputGeneratedClass( NonSingletonWithConstructor.class.getCanonicalName() + "InjectorFactory" );
        val nonSingletonAsString = APT.readFileAsString(nonSingleton);
        val expectedContent = APT.testResourceAsString( "expected-non-singleton-with-constructor-class-factory.java" );
        assertEquals( expectedContent, nonSingletonAsString );
    }

    @SneakyThrows
    @DisplayName("Can generate factory for a singleton class")
    @Test void process3() {
        val result = APT.runner().run(injector, APT.asSource( APT.testFile(SingletonService.class) ));
        result.printErrorsIfAny();

        val nonSingleton = APT.outputGeneratedClass( SingletonService.class.getCanonicalName() + "InjectorFactory" );
        val nonSingletonAsString = APT.readFileAsString(nonSingleton);
        val expectedContent = APT.testResourceAsString( "expected-singleton-class-factory.java" );
        assertEquals( expectedContent, nonSingletonAsString );
    }

    @SneakyThrows
    @DisplayName("Can generate factory for a singleton with constructor class")
    @Test void process4() {
        val result = APT.runner().run(injector, APT.asSource( APT.testFile(SingletonWithConstructor.class) ));
        result.printErrorsIfAny();

        val nonSingleton = APT.outputGeneratedClass( SingletonWithConstructor.class.getCanonicalName() + "InjectorFactory" );
        val nonSingletonAsString = APT.readFileAsString(nonSingleton);
        val expectedContent = APT.testResourceAsString( "expected-singleton-with-constructor-class-factory.java" );
        assertEquals( expectedContent, nonSingletonAsString );
    }

    @SneakyThrows
    @DisplayName("Can generate factories from producer methods")
    @Test void process5() {
        val result = APT.runner().run(injector,
                APT.asSource( APT.testFile(ProducerOfImportantServices.class) ),
                APT.asSource( APT.testFile(ImportantService.class) ),
                APT.asSource( APT.testFile(SecondImportantService.class) ));

        result.printErrorsIfAny();

        val nonSingleton = APT.outputGeneratedClass( ImportantService.class.getCanonicalName() + "InjectorFactory" );
        val nonSingletonAsString = APT.readFileAsString(nonSingleton);
        val expectedContent = APT.testResourceAsString( "expected-producer-class-factory.java" );
        assertEquals( expectedContent, nonSingletonAsString );
    }

    @SneakyThrows
    @DisplayName("Can generate exposed service loader")
    @Test void process6() {
        val result = APT.runner().run(injector,
                APT.asSource( APT.testFile(Sum.class) ),
                APT.asSource( APT.testFile(Minus.class) ));
        result.printErrorsIfAny();

        val nonSingleton = APT.outputGeneratedClass( MathOperation.class.getCanonicalName() + "ExposedServicesLoader" );
        val nonSingletonAsString = APT.readFileAsString(nonSingleton);
        val expectedContent = APT.testResourceAsString( "expected-exposed-service-loader.java" );
        assertEquals( expectedContent, nonSingletonAsString );
    }

    @SneakyThrows
    @DisplayName("Can inject all exposed objects from a given interface")
    @Test void process7() {
        val result = APT.runner().run(injector,
                APT.asSource( APT.testFile(Sum.class) ),
                APT.asSource( APT.testFile(Minus.class) ),
                APT.asSource( APT.testFile(Calculator.class) ),
                APT.asSource( APT.testFile(CalculatorNonManaged.class) ),
                APT.asSource( APT.testFile(CalculatorNonSingleton.class) ),
                APT.asSource( APT.testFile(CalculatorNonManagerProducer.class) ));
        result.printErrorsIfAny();

        val nonSingleton = APT.outputGeneratedClass( Calculator.class.getCanonicalName() + "InjectorFactory" );
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
                .printErrorsIfAny();

        val nonSingleton = APT.outputGeneratedClass( SingletonWithConstructor.class.getCanonicalName() + "InjectorFactory" );
        val nonSingletonAsString = APT.readFileAsString(nonSingleton);
        val expectedContent = APT.testResourceAsString( "expected-singleton-with-constructor-class-factory.java" );
        assertEquals( expectedContent, nonSingletonAsString );
    }
}
