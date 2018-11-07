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

@SupportedAnnotationTypes( { "javax.inject.*", "injector.*" } )
public class InjectorProcessor extends SimplifiedAbstractProcessor {

    static final String EOL = "\n";
    static final String FACTORY_LOCATION = "META-INF/services/" + Factory.class.getCanonicalName();

    final ClassGenerator nonSingletonFactory = ClassGenerator.with( "non-singleton-class-factory.mustache" );
    final ClassGenerator singletonFactory = ClassGenerator.with( "singleton-class-factory.mustache" );
    final ClassGenerator producerClassFactory = ClassGenerator.with( "producer-class-factory.mustache" );

    final JavaFileManager.Location outputLocation;
    List<String> exposedServices;

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
            exposedServices = new ArrayList<>();
            generateFactories(types);
            generateSPIFiles();
        } catch ( Exception cause ){
            error( cause.getMessage() );
            cause.printStackTrace();
        }
    }

    private void generateFactories(Collection<SimplifiedAST.Type> types) throws IOException {
        for (SimplifiedAST.Type type : types) {
            val injectorTypes = splitInjectorTypes(type);
            generateRegularFactory(injectorTypes.getRegular());
            generateProducers(injectorTypes.getProducers());
        }
    }

    private InjectorTypes splitInjectorTypes(SimplifiedAST.Type type) {
        val regular = createNewType(type);
        val listOfProducers = new ArrayList<InjectorType>();

        for (val method : type.getMethods()) {
            val injectorMethod = createInjectorMethod( method );
            if ( injectorMethod.isProducer() )
                listOfProducers.add( createTypeWithSingleMethod( type, injectorMethod ) );
            else
                regular.getMethods().add( injectorMethod );
        }

        return new InjectorTypes( regular, listOfProducers );
    }

    private InjectorMethod createInjectorMethod(SimplifiedAST.Method method) {
        return (InjectorMethod)new InjectorMethod()
                .setParameters( method.getParameters() )
                .setConstructor( method.isConstructor() )
                .setAnnotations( method.getAnnotations() )
                .setName( method.getName() )
                .setType( method.getType() );
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

    private void generateProducers(List<InjectorType> producers) throws IOException {
        for (val producer : producers) {
            generateFactory( producerClassFactory, producer );
        }
    }

    private void generateFactory( ClassGenerator generator, InjectorType factory ) throws IOException {
        if ( exposedServices.contains( factory.getCanonicalName() ) )
            return;

        val filer = processingEnv.getFiler();
        val source = filer.createSourceFile( factory.getCanonicalName() + "Factory" );
        info( "Generating " + factory.getCanonicalName() + "Factory (singleton=" + factory.isSingleton() + ")" );
        try ( val writer = source.openWriter() ) {
            generator.write( writer, factory );
            exposedServices.add( factory.getCanonicalName() );
        }
    }

    private void generateSPIFiles() throws IOException {
        info( "Running dependency injection optimization..." );
        final Set<String> implementations = readResourceIfExists(FACTORY_LOCATION);
        implementations.addAll( this.exposedServices);
        try ( final Writer resource = createResource( FACTORY_LOCATION ) ) {
            for (final String implementation : implementations)
                resource.write(implementation + "Factory" + EOL);
        }
        info( "Done!" );
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