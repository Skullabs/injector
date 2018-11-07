package injector.apt;

import generator.apt.SimplifiedAPTRunner;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileManager;
import javax.tools.StandardLocation;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InjectorProcessorTest {

    InjectorProcessor injector = new InjectorProcessor();

    @SneakyThrows
    @DisplayName("Can generate factory for a non-singleton class")
    @Test void process() {
        val result = APT.runner().run(injector, new SimplifiedAPTRunner.LocalJavaSource( APT.testFile(NonSingletonService.class) ));
        result.printErrorsIfAny();

        val nonSingleton = APT.outputGeneratedClass( NonSingletonService.class.getCanonicalName() + "Factory" );
        val nonSingletonAsString = APT.readFileAsString(nonSingleton);
        val expectedContent = APT.testResourceAsString( "expected-non-singleton-class-factory.java" );
        assertEquals( expectedContent, nonSingletonAsString );
    }

    @SneakyThrows
    @DisplayName("Can generate factory for a non-singleton with constructor class")
    @Test void process2() {
        val result = APT.runner().run(injector, new SimplifiedAPTRunner.LocalJavaSource( APT.testFile(NonSingletonWithConstructor.class) ));
        result.printErrorsIfAny();

        val nonSingleton = APT.outputGeneratedClass( NonSingletonWithConstructor.class.getCanonicalName() + "Factory" );
        val nonSingletonAsString = APT.readFileAsString(nonSingleton);
        val expectedContent = APT.testResourceAsString( "expected-non-singleton-with-constructor-class-factory.java" );
        assertEquals( expectedContent, nonSingletonAsString );
    }

    @SneakyThrows
    @DisplayName("Can generate factory for a singleton class")
    @Test void process3() {
        val result = APT.runner().run(injector, new SimplifiedAPTRunner.LocalJavaSource( APT.testFile(SingletonService.class) ));
        result.printErrorsIfAny();

        val nonSingleton = APT.outputGeneratedClass( SingletonService.class.getCanonicalName() + "Factory" );
        val nonSingletonAsString = APT.readFileAsString(nonSingleton);
        val expectedContent = APT.testResourceAsString( "expected-singleton-class-factory.java" );
        assertEquals( expectedContent, nonSingletonAsString );
    }

    @SneakyThrows
    @DisplayName("Can generate factory for a singleton with constructor class")
    @Test void process4() {
        val result = APT.runner().run(injector, new SimplifiedAPTRunner.LocalJavaSource( APT.testFile(SingletonWithConstructor.class) ));
        result.printErrorsIfAny();

        val nonSingleton = APT.outputGeneratedClass( SingletonWithConstructor.class.getCanonicalName() + "Factory" );
        val nonSingletonAsString = APT.readFileAsString(nonSingleton);
        val expectedContent = APT.testResourceAsString( "expected-singleton-with-constructor-class-factory.java" );
        assertEquals( expectedContent, nonSingletonAsString );
    }

    @SneakyThrows
    @DisplayName("Can generate factories from producer methods")
    @Test void process5() {
        val result = APT.runner().run(injector, new SimplifiedAPTRunner.LocalJavaSource( APT.testFile(ProducerOfImportantServices.class) ));
        result.printErrorsIfAny();

        val nonSingleton = APT.outputGeneratedClass( ImportantService.class.getCanonicalName() + "Factory" );
        val nonSingletonAsString = APT.readFileAsString(nonSingleton);
        val expectedContent = APT.testResourceAsString( "expected-producer-class-factory.java" );
        assertEquals( expectedContent, nonSingletonAsString );
    }

    @SneakyThrows
    @DisplayName("Can generate all the factories above")
    @Test void process6() {
        APT.runner().run(injector,
            new SimplifiedAPTRunner.LocalJavaSource( APT.testFile(SingletonService.class) ),
            new SimplifiedAPTRunner.LocalJavaSource( APT.testFile(SingletonWithConstructor.class) ),
            new SimplifiedAPTRunner.LocalJavaSource( APT.testFile(NonSingletonService.class) ),
            new SimplifiedAPTRunner.LocalJavaSource( APT.testFile(NonSingletonWithConstructor.class) ),
            new SimplifiedAPTRunner.LocalJavaSource( APT.testFile(ProducerOfImportantServices.class) ))
                .printErrorsIfAny();

        val nonSingleton = APT.outputGeneratedClass( SingletonWithConstructor.class.getCanonicalName() + "Factory" );
        val nonSingletonAsString = APT.readFileAsString(nonSingleton);
        val expectedContent = APT.testResourceAsString( "expected-singleton-with-constructor-class-factory.java" );
        assertEquals( expectedContent, nonSingletonAsString );
    }
}
