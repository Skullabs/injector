package injector;

import lombok.RequiredArgsConstructor;
import lombok.val;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public abstract class DefaultExposedServicesLoader<T> implements ExposedServicesLoader<T> {

    private Iterable<T> loadedServices;

    @Override
    public Iterable<T> load(InjectionContext context) {
        if ( loadedServices == null )
            synchronized (this){
                if ( loadedServices == null )
                    try {
                        loadedServices = loadAllImplementations(context, getExposedType());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
            }
        return loadedServices;
    }

    @SuppressWarnings("unchecked")
    private static <T> Iterable<T> loadAllImplementations( InjectionContext context, Class<T> clazz ) throws Exception {
        val instances = new ArrayList<T>();
        val classNames = readAllClassNames( clazz );

        for (val className : classNames) {
            val classExposedByFactory = Class.forName( className );
            val instance = (T)context.instanceOf( classExposedByFactory );
            instances.add( instance );
        }

        return instances;
    }

    private static <T> List<String> readAllClassNames( Class<T> clazz ) throws IOException, URISyntaxException {
        val classLoader = clazz.getClassLoader();
        val resources = classLoader.getResources( "META-INF/exposed/" + clazz.getCanonicalName() );
        val classNames = new ArrayList<String>();

        while ( resources.hasMoreElements() ) {
            val next = resources.nextElement();
            val lines = Files.readAllLines(Paths.get( next.toURI() ) );
            classNames.addAll( lines );
        }

        return classNames;
    }
}