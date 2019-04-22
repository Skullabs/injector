package injector.apt;

import generator.apt.ClassGenerator;
import generator.apt.SimplifiedAST;
import generator.apt.SimplifiedAbstractProcessor;
import injector.*;
import lombok.val;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.StandardLocation;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.nio.file.Files;
import java.util.*;

@SupportedAnnotationTypes( { "injector.*" } )
public class InjectorProcessor extends SimplifiedAbstractProcessor {

    static final String EOL = "\n";
    static final String SPI_LOCATION = "META-INF/services/";
    static final String FACTORY = Factory.class.getCanonicalName();

    static final String LOADER = ExposedServicesLoader.class.getCanonicalName();

    final ClassGenerator nonSingletonFactory = ClassGenerator.with( "non-singleton-class-factory.mustache" );
    final ClassGenerator singletonFactory = ClassGenerator.with( "singleton-class-factory.mustache" );
    final ClassGenerator producerClassFactory = ClassGenerator.with( "producer-class-factory.mustache" );
    final ClassGenerator exposedServiceLoader = ClassGenerator.with( "exposed-service-loader.mustache" );

    final JavaFileManager.Location outputLocation;

    Map<String, List<String>> spiClasses;
    Map<String, List<String>> loaderClasses;
    Map<String, Object> exposedClasses;

    public InjectorProcessor(){
        this( StandardLocation.CLASS_OUTPUT );
    }

    InjectorProcessor(JavaFileManager.Location outputLocation) {
        super(
            Collections.emptyList(),
            Collections.singletonList(Producer.class),
            Arrays.asList(Singleton.class, New.class)
        );
        this.outputLocation = outputLocation;
    }

    @Override
    protected void process(Collection<SimplifiedAST.Type> types) {
        try {
            info( "Running Dependency Injection Optimization..." );
            initializeClassCaches();
            generateClasses(types);
            generateSPIFiles();
            memorizeLoaders();
            info( "Done!" );
        } catch ( Exception cause ){
            val msg = cause.getMessage() != null ? cause.getMessage() : "NullPointerException";
            error( msg );
            cause.printStackTrace();
        }
    }

    private void initializeClassCaches() {
        spiClasses = new HashMap<>();
        loaderClasses = new HashMap<>();
        exposedClasses = new HashMap<>();
    }

    private void generateClasses(Collection<SimplifiedAST.Type> types) throws IOException {
        for (SimplifiedAST.Type type : types) {
            val injectorTypes = splitInjectorTypes(type);
            generateRegularFactory(injectorTypes.getRegular());
            generateProducers(injectorTypes.getProducers());
            generateExposedService(injectorTypes.getRegular());
        }
    }

    private InjectorTypes splitInjectorTypes(SimplifiedAST.Type type) {
        val regular = createNewType(type);
        val listOfProducers = new HashSet<InjectorType>();

        for (val method : type.getMethods()) {
            val injectorMethod = InjectorMethod.from( method );
            if ( injectorMethod.isProducer() )
                listOfProducers.add( createTypeWithSingleMethod( type, injectorMethod ) );
            else
                regular.getMethods().add( injectorMethod );
        }

        return new InjectorTypes( regular, listOfProducers );
    }

    private InjectorType createTypeWithSingleMethod( SimplifiedAST.Type type, SimplifiedAST.Method method ) {
        val iType = (InjectorType)new InjectorType()
                .setCanonicalName( method.getType() )
                .setType( type.getCanonicalName() )
                .setAnnotations( type.getAnnotations() )
                .setName( type.getName() );
        iType.getMethods().add( method );
        return iType;
    }

    private InjectorType createNewType( SimplifiedAST.Type type ) {
        return (InjectorType)new InjectorType()
                .setCanonicalName( type.getCanonicalName() )
                .setFields( type.getFields() )
                .setType( type.getType() )
                .setAnnotations( type.getAnnotations() )
                .setName( type.getName() );
    }

    private void generateRegularFactory(InjectorType type) throws IOException {
        val generator = type.isSingleton() ? singletonFactory : nonSingletonFactory;
        generateFactory( generator, type );
    }

    private void generateProducers(Collection<InjectorType> producers) throws IOException {
        for (val producer : producers) {
            generateFactory( producerClassFactory, producer );
        }
    }

    private void generateFactory( ClassGenerator generator, InjectorType factory ) throws IOException {
        val className = factory.getCanonicalName() + "InjectorFactory";
        if ( getSpiClassesForFactory().contains( className ) )
            return;

        val filer = processingEnv.getFiler();
        val source = filer.createSourceFile( className );
        info( "  + " + factory.getCanonicalName() + "InjectorFactory (singleton=" + factory.isSingleton() + ")" );
        try ( val writer = source.openWriter() ) {
            generator.write( writer, factory );
            getSpiClassesForFactory().add( className );
        }
    }

    private void generateSPIFiles() throws IOException {
        for ( val entry : this.spiClasses.entrySet()) {
            val location = SPI_LOCATION + entry.getKey();
            val implementations = readResourceIfExists( location );
            implementations.addAll( entry.getValue() );
            try ( final Writer resource = createResource( location ) ) {
                for (final String implementation : implementations)
                    resource.write(implementation + EOL);
            }
        }
    }

    private List<String> getSpiClassesForFactory(){
        return spiClasses.computeIfAbsent( FACTORY, k -> new ArrayList<>() );
    }

    private void generateExposedService(InjectorType type) throws IOException {
        val exposedAs = type.getExposedClass();
        val canonicalName = type.getCanonicalName();
        if ( exposedAs != null ) {
            val className = exposedAs + "ExposedServicesLoader";
            info( "  + " + canonicalName + " (exposedClass=" + exposedAs + ")" );

            if ( !exposedClasses.containsKey( exposedAs ) ) {
                val filer = processingEnv.getFiler();
                val source = filer.createSourceFile(className);
                try (val writer = source.openWriter()) {
                    type.setCanonicalName(exposedAs);
                    exposedServiceLoader.write(writer, type);
                    exposedClasses.put(exposedAs, type);
                }
            }

            loaderClasses.computeIfAbsent( exposedAs, t -> new ArrayList<>() ).add( canonicalName );
            spiClasses.computeIfAbsent( LOADER, t -> new ArrayList<>() ).add( className );
        }
    }

    private void memorizeLoaders() throws IOException {
        for ( val entry : loaderClasses.entrySet()) {
            val location = SPI_LOCATION + entry.getKey();
            val implementations = readResourceIfExists( location );
            implementations.addAll( entry.getValue() );
            try ( final Writer resource = createResource( location ) ) {
                for (final String implementation : implementations)
                    resource.write(implementation + EOL);
            }
        }
    }

    private Set<String> readResourceIfExists(final String resourcePath) throws IOException {
        final Set<String> resourceContent = new HashSet<>();
        final FileObject resource = processingEnv.getFiler().getResource( this.outputLocation, "", resourcePath );
        final File file = new File( resource.toUri() );
        if ( file.exists() )
            resourceContent.addAll( Files.readAllLines( file.toPath() ) );
        return resourceContent;
    }

    private Writer createResource(final String resourcePath) throws IOException {
        final FileObject resource = processingEnv.getFiler().getResource( this.outputLocation, "", resourcePath );
        final URI uri = resource.toUri();
        createNeededDirectoriesTo( uri );
        final File file = createFile( uri );
        return new FileWriter( file );
    }

    private void createNeededDirectoriesTo(final URI uri) {
        File dir = null;
        if ( uri.isAbsolute() )
            dir = new File( uri ).getParentFile();
        else
            dir = new File( uri.toString() ).getParentFile();
        dir.mkdirs();
    }

    private File createFile(final URI uri) throws IOException {
        final File file = new File( uri );
        if ( !file.exists() )
            file.createNewFile();
        return file;
    }

    private void info(final String msg) {
        processingEnv.getMessager().printMessage( Diagnostic.Kind.NOTE, msg );
    }

    private void error(final String msg) {
        processingEnv.getMessager().printMessage( Diagnostic.Kind.ERROR, msg );
    }
}
