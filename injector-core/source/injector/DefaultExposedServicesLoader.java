package injector;

import lombok.RequiredArgsConstructor;
import lombok.val;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public abstract class DefaultExposedServicesLoader<T> implements ExposedServicesLoader<T> {

    @SuppressWarnings("unchecked")
    protected Iterable<T> loadAllImplementations( InjectionContext context ) {
        val instances = new ArrayList<T>();

        for ( val classExposedByFactory : getExposedClasses() ) {
            val instance = (T)context.instanceOf( classExposedByFactory );
            instances.add( instance );
        }

        return instances;
    }

    protected abstract List<Class> getExposedClasses();

    protected static <T> List<Class> readAllClassNames( Class<T> clazz ) {
        try {
            val classLoader = clazz.getClassLoader();
            val resources = classLoader.getResources("META-INF/services/" + clazz.getCanonicalName());
            val classNames = new ArrayList<Class>();

            while (resources.hasMoreElements()) {
                val next = resources.nextElement();
                val location = next.openStream();

                val reader = new BufferedReader( new InputStreamReader( location ) );
                reader.lines().forEach( line -> {
                    try {
                        classNames.add(Class.forName(line));
                    } catch ( Throwable e ) {
                        throw new RuntimeException( e );
                    }
                });
            }

            return classNames;
        } catch ( Throwable cause ) {
            throw new RuntimeException( cause );
        }
    }
}
