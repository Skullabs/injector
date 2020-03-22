package injector.apt;

import generator.apt.ResourceLocator;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
class CustomResourceLocator {

    final ResourceLocator resourceLocator;

    Set<String> readResourceIfExists(final String resourcePath) throws IOException {
        final Set<String> resourceContent = new HashSet<>();
        final URI resource = resourceLocator.locate(resourcePath);
        final File file = new File( resource );
        if ( file.exists() )
            resourceContent.addAll( Files.readAllLines( file.toPath() ) );
        return resourceContent;
    }

    Writer createResource(final String resourcePath) throws IOException {
        final URI uri = resourceLocator.locate(resourcePath);
        createNeededDirectoriesFor( uri );
        final File file = createFile( uri );
        return new FileWriter( file );
    }

    private void createNeededDirectoriesFor(final URI uri) throws IOException {
        File dir = null;
        if ( uri.isAbsolute() )
            dir = new File( uri ).getParentFile();
        else
            dir = new File( uri.toString() ).getParentFile();
        if (!dir.exists() && !dir.mkdirs())
            throw new IOException("Could not create dir " + uri);
    }

    private File createFile(final URI uri) throws IOException {
        final File file = new File( uri );
        if ( !file.exists() && !file.createNewFile())
                throw new IOException("Could not create file " + uri);
        return file;
    }
}
