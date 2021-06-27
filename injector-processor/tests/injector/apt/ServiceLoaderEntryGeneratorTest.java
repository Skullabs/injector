package injector.apt;

import injector.ExposedServicesLoader;
import injector.Factory;
import injector.apt.example.*;
import injector.apt.example.calc.MathOperation;
import injector.apt.example.calc.Minus;
import injector.apt.example.calc.Sum;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.lang.model.SourceVersion;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServiceLoaderEntryGeneratorTest {

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
    @DisplayName("Should register SPI for a factory of a non-singleton class")
    @Test
    void process() {
        APT.runner()
            .run(injector, APT.asSource( APT.testFile(NonSingletonService.class) ))
            .failInCaseOfError();

        val generatedFactory = "injector.apt.example.NonSingletonServiceInjectorFactory";
        assertTrue( APT.isSpiExposedFor(generatedFactory, Factory.class) );
    }

    @SneakyThrows
    @DisplayName("Should register SPI for a factory of a non-singleton with constructor class")
    @Test void process2() {
        APT.runner()
                .run(injector, APT.asSource( APT.testFile(NonSingletonWithConstructor.class) ))
                .failInCaseOfError();

        val generatedFactory = "injector.apt.example.NonSingletonWithConstructorInjectorFactory";
        assertTrue( APT.isSpiExposedFor(generatedFactory, Factory.class) );
    }

    @SneakyThrows
    @DisplayName("Should register SPI for a factory of a singleton class")
    @Test void process3() {
        APT.runner()
                .run(injector, APT.asSource( APT.testFile(SingletonService.class) ))
                .failInCaseOfError();

        val generatedFactory = "injector.apt.example.SingletonServiceInjectorFactory";
        assertTrue( APT.isSpiExposedFor(generatedFactory, Factory.class) );
    }

    @SneakyThrows
    @DisplayName("Can generate factory for a singleton with constructor class")
    @Test void process4() {
        APT.runner()
                .run(injector, APT.asSource( APT.testFile(SingletonWithConstructor.class) ))
                .failInCaseOfError();

        val generatedFactory = "injector.apt.example.SingletonWithConstructorInjectorFactory";
        assertTrue( APT.isSpiExposedFor(generatedFactory, Factory.class) );
    }

    @Nested class GenerateFactoriesFromProducerMethods {

        @Nested class SingletonProducer {

            @DisplayName("Should generate factories for the singleton producer itself")
            @Test void process1() {
                APT.runner().run(injector,
                        APT.asSource(APT.testFile(ProducerOfImportantServices.class)),
                        APT.asSource(APT.testFile(ImportantService.class)),
                        APT.asSource(APT.testFile(SecondImportantService.class)))
                        .failInCaseOfError();

                val generatedFactory = "injector.apt.example.ProducerOfImportantServicesInjectorFactory";
                assertTrue(APT.isSpiExposedFor(generatedFactory, Factory.class));
            }

            @DisplayName("Should generate factories for each of the producers")
            @Test void process2() {
                APT.runner().run(injector,
                        APT.asSource(APT.testFile(ProducerOfImportantServices.class)),
                        APT.asSource(APT.testFile(ImportantService.class)),
                        APT.asSource(APT.testFile(SecondImportantService.class)))
                        .failInCaseOfError();

                val generatedFactory = "injector.apt.example.ImportantServiceInjectorFactory";
                assertTrue(APT.isSpiExposedFor(generatedFactory, Factory.class));

                val secondGeneratedFactory = "injector.apt.example.SecondImportantServiceInjectorFactory";
                assertTrue(APT.isSpiExposedFor(secondGeneratedFactory, Factory.class));
            }

        }

        @Nested class NonSingletonProducer {

            @DisplayName("Should generate factories for the non-singleton producer itself")
            @Test void process1() {
                APT.runner().run(injector,
                        APT.asSource(APT.testFile(NonSingletonProducerOfImportantServices.class)),
                        APT.asSource(APT.testFile(ImportantService.class)),
                        APT.asSource(APT.testFile(SecondImportantService.class)))
                        .failInCaseOfError();

                val generatedFactory = "injector.apt.example.NonSingletonProducerOfImportantServicesInjectorFactory";
                assertTrue(APT.isSpiExposedFor(generatedFactory, Factory.class));
            }

            @DisplayName("Should generate factories for each of the producers")
            @Test void process2() {
                APT.runner().run(injector,
                    APT.asSource(APT.testFile(NonSingletonProducerOfImportantServices.class)),
                    APT.asSource(APT.testFile(ImportantService.class)),
                    APT.asSource(APT.testFile(SecondImportantService.class)))
                        .failInCaseOfError();

                val generatedFactory = "injector.apt.example.ImportantServiceInjectorFactory";
                assertTrue(APT.isSpiExposedFor(generatedFactory, Factory.class));

                val secondGeneratedFactory = "injector.apt.example.SecondImportantServiceInjectorFactory";
                assertTrue(APT.isSpiExposedFor(secondGeneratedFactory, Factory.class));
            }
        }
    }

    @DisplayName("Exposed classes from the same interface")
    @Nested class ExposedClassesFromTheSameInterface {

        @DisplayName("Classes exposed using @ExposedAs")
        @Nested class ClassesUsingExposedAs {

            @DisplayName("Should have a custom generated ExposedServicesLoader implementation exposed")
            @Test void process1() {
                APT.runner().run(injector,
                        APT.asSource(APT.testFile(Minus.class)))
                        .failInCaseOfError();

                val generatedFactory = "injector.apt.example.calc.MathOperationExposedServicesLoader";
                assertTrue(APT.isSpiExposedFor(generatedFactory, ExposedServicesLoader.class));
            }

            @DisplayName("Should have the defined interface exposed as SPI")
            @Test void process2() {
                APT.runner().run(injector,
                    APT.asSource(APT.testFile(Minus.class)))
                        .failInCaseOfError();

                val generatedFactory = "injector.apt.example.calc.Minus";
                assertTrue(APT.isSpiExposedFor(generatedFactory, MathOperation.class));
            }
        }

        @DisplayName("Classes exposed using @Exposed")
        @Nested class ClassesUsingExposed {

            @DisplayName("Should have a custom generated ExposedServicesLoader implementation exposed")
            @Test void process1() {
                APT.runner().run(injector,
                    APT.asSource(APT.testFile(Sum.class)))
                        .failInCaseOfError();

                val generatedFactory = "injector.apt.example.calc.MathOperationExposedServicesLoader";
                assertTrue(APT.isSpiExposedFor(generatedFactory, ExposedServicesLoader.class));
            }

            @DisplayName("Should have the defined interface exposed as SPI")
            @Test void process2() {
                APT.runner().run(injector,
                    APT.asSource(APT.testFile(Sum.class)))
                        .failInCaseOfError();

                val generatedFactory = "injector.apt.example.calc.Sum";
                assertTrue(APT.isSpiExposedFor(generatedFactory, MathOperation.class));
            }
        }

        @DisplayName("When there is one class @ExposedAs and another using @Exposed but both for the same interface.")
        @Nested class WhenThereAreClassesUsingBoth {

            @DisplayName("Should have the defined interface exposed as SPI for both classes")
            @Test void process2() {
                APT.runner().run(injector,
                        APT.asSource(APT.testFile(Sum.class)),
                        APT.asSource(APT.testFile(Minus.class)))
                        .failInCaseOfError();

                val exposedClass = "injector.apt.example.calc.Minus";
                assertTrue(APT.isSpiExposedFor(exposedClass, MathOperation.class));

                val secondExposedClass = "injector.apt.example.calc.Sum";
                assertTrue(APT.isSpiExposedFor(secondExposedClass, MathOperation.class));
            }
        }
    }
}
