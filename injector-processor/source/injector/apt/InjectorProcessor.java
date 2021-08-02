package injector.apt;

import generator.apt.ClassGenerator;
import generator.apt.SimplifiedAST;
import generator.apt.SimplifiedAbstractProcessor;
import injector.*;
import lombok.NonNull;
import lombok.val;

import javax.annotation.processing.FilerException;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.tools.JavaFileManager;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.*;

@SupportedAnnotationTypes( { "injector.*" } )
public class InjectorProcessor extends SimplifiedAbstractProcessor {

    static final String FACTORY = Factory.class.getCanonicalName();
    static final String LOADER = ExposedServicesLoader.class.getCanonicalName();

    final ClassGenerator nonSingletonFactory = ClassGenerator.with( "non-singleton-class-factory.mustache" );
    final ClassGenerator singletonFactory = ClassGenerator.with( "singleton-class-factory.mustache" );
    final ClassGenerator producerClassFactory = ClassGenerator.with( "producer-class-factory.mustache" );
    final ClassGenerator exposedServiceLoader = ClassGenerator.with( "exposed-service-loader.mustache" );
    
    final FactoryClassesGenerator classesGenerator = new FactoryClassesGenerator();
    final ExposedServiceGenerator exposedServiceGenerator = new ExposedServiceGenerator();
    final ServiceProviderImplementations spiClasses = new ServiceProviderImplementations();
    final ServiceProviderImplementations loaderClasses = new ServiceProviderImplementations();

    final JavaFileManager.Location outputLocation;

    Map<String, Object> exposedClasses;

    public InjectorProcessor(){
        this( StandardLocation.CLASS_OUTPUT );
    }

    InjectorProcessor(JavaFileManager.Location outputLocation) {
        super(
            Collections.emptyList(),
            Collections.singletonList(Producer.class),
            Arrays.asList(Singleton.class, New.class, ExposedAs.class, Exposed.class)
        );
        this.outputLocation = outputLocation;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        spiClasses.use(resourceLocator);
        loaderClasses.use(resourceLocator);
    }

    @Override
    protected void process(Collection<SimplifiedAST.Type> types) {
        try {
            initializeClassCaches();
            generateClasses(types);
            spiClasses.flush();
            loaderClasses.flush();
        } catch ( Exception cause ){
            val msg = cause.getMessage() != null ? cause.getMessage() : "<null error msg>";
            error( msg );
            cause.printStackTrace();
        }
    }

    private void initializeClassCaches() {
        spiClasses.cleanCaches();
        loaderClasses.cleanCaches();
        exposedClasses = new HashMap<>();
    }

    private void generateClasses(Collection<SimplifiedAST.Type> types) throws IOException {
        for (val type : types) {
            val injectorTypes = InjectorTypesFactory.from(type);
            classesGenerator.generateRegularFactory(injectorTypes.getRegular());
            classesGenerator.generateFactoryForProducers(injectorTypes.getProducers());
            exposedServiceGenerator.generate(injectorTypes.getRegular());
        }
    }

    /**
     * Generates {@link Factory} class implementations.
     */
    class FactoryClassesGenerator {

        void generateRegularFactory(InjectorType type) throws IOException {
            val generator = type.isSingleton() ? singletonFactory : nonSingletonFactory;
            generateFactory( generator, type );
        }

        void generateFactoryForProducers(Collection<InjectorType> producers) throws IOException {
            for (val producer : producers) {
                generateFactory( producerClassFactory, producer );
            }
        }

        private void generateFactory(ClassGenerator generator, InjectorType factory ) throws IOException {
            val className = removeGenericsFromClassName(factory.getCanonicalName() + "InjectorFactory" + factory.getUniqueIdentifier());
            if (!factoryHasBeenProcessedBefore( className ) ) {
                createSourceFile(generator, factory, className);
                memorizeProcessedFactory(className);
            }
        }

        private boolean factoryHasBeenProcessedBefore(String className) {
            return spiClasses.getSpiClassesFor(FACTORY).contains(className);
        }

        private void memorizeProcessedFactory(String className) {
            spiClasses.getSpiClassesFor(FACTORY).add(className);
        }

        private void createSourceFile(ClassGenerator generator, InjectorType factory, String className) throws IOException {
            try {
                val filer = processingEnv.getFiler();
                val source = filer.createSourceFile(className);
                info("Generating " + factory.getCanonicalName() + "InjectorFactory"+ factory.getUniqueIdentifier() +" (singleton=" + factory.isSingleton() + ")");
                try (val writer = source.openWriter()) {
                    generator.write(writer, factory);
                }
            } catch ( FilerException cause ) {
                handleFailureWhenCreatingFile(cause, className);
            }
        }
    }

    /**
     * Generates {@link ExposedServicesLoader} class implementations.
     */
    class ExposedServiceGenerator {

        void generate(InjectorType type) throws IOException {
            val exposedAs = type.getExposedClass();
            if ( exposedAs != null )
                generateExposedClassFactory(type, exposedAs);

            if ( type.isAnnotatedWith(Exposed.class) ) {
                val exposedInterfaces = type.getExposedInterfaces();
                for (String exposedInterface : exposedInterfaces)
                    generateExposedClassFactory(type, exposedInterface);
            }
        }

        private void generateExposedClassFactory(InjectorType type, String exposedAs) throws IOException {
            if (exposedAs.startsWith("java.")) {
                warn("Prevented to generate exposed classes for " + exposedAs + ": " +
                    "cannot generate classes within 'java' package.");
                return;
            }

            exposedAs = removeGenericsFromClassName(exposedAs);
            val className = exposedAs + "ExposedServicesLoader" + type.getUniqueIdentifier();
            val canonicalName = type.getCanonicalName();

            if ( !exposedClasses.containsKey( exposedAs ) ) {
                info( "Generating " + className );
                createSourceFile(type, className, exposedAs);
                exposedClasses.put(exposedAs, type);
            }

            loaderClasses.memorizeImplementationOf( exposedAs, canonicalName );
            spiClasses.memorizeImplementationOf( LOADER, className );
        }

        private void createSourceFile(InjectorType type, String className, String exposedAs) throws IOException {
            try {
                val filer = processingEnv.getFiler();
                val source = filer.createSourceFile(className);
                try (val writer = source.openWriter()) {
                    type.setCanonicalName(exposedAs);
                    exposedServiceLoader.write(writer, type);
                }
            } catch ( FilerException cause ) {
                handleFailureWhenCreatingFile(cause, type.getCanonicalName());
            }
        }
    }

    void handleFailureWhenCreatingFile(FilerException cause, String fileName) throws FilerException {
        if (!cause.getMessage().contains("recreate") && !cause.getMessage().contains("reopen") ){
            throw cause;
        }

        info("Ignoring already created " + fileName );
    }

    @NonNull
    String removeGenericsFromClassName(@NonNull String className) {
        return className.replaceAll("<[^>]+>", "");
    }

    @Override
    protected void info(String msg) {
        if (System.getenv("INJECTOR_VERBOSE") != null)
            super.info(msg);
    }
}
