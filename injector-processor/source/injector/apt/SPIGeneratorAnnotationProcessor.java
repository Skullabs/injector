/*
 * Copyright 2019 Skullabs Contributors (https://github.com/skullabs)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package injector.apt;

import generator.apt.*;
import lombok.*;

import javax.tools.*;
import java.io.*;
import java.lang.annotation.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

@Getter
public abstract class SPIGeneratorAnnotationProcessor extends SimplifiedAbstractProcessor {

    static final String EOL = "\n";

    private final JavaFileManager.Location outputLocation = StandardLocation.CLASS_OUTPUT;
    private final String spiLocation;
    private final String spiClass;
    private List<String> spiClasses;

    public SPIGeneratorAnnotationProcessor(
            Class<?> spiType,
            List<Class<? extends Annotation>> fieldAnnotations,
            List<Class<? extends Annotation>> methodAnnotations,
            List<Class<? extends Annotation>> typeAnnotations)
    {
        super(fieldAnnotations, methodAnnotations, typeAnnotations);
        spiClass = spiType.getCanonicalName();
        spiLocation = "META-INF/services/" + spiType.getCanonicalName();
    }

    protected void flushSPIClasses(){
        spiClasses = new ArrayList<>();
    }

    protected void memorizeSPIFor( String canonicalClassName ) {
        spiClasses.add( canonicalClassName );
    }

    protected void generateSPIFiles() throws IOException {
        val implementations = readResourceIfExists(spiLocation);
        implementations.addAll( this.spiClasses );

        info( "Running Dependency Injection Optimization for "+spiClass+"..." );
        try (val resource = createResource(spiLocation)) {
            for (val implementation : implementations){
                info( "  + " + implementation );
                resource.write(implementation + EOL);
            }
        }
    }

    private Set<String> readResourceIfExists( final String resourcePath ) throws IOException {
        val resourceContent = new HashSet<String>();
        val resource = processingEnv.getFiler().getResource( this.outputLocation, "", resourcePath );
        val file = new File( resource.toUri() );
        if ( file.exists() )
            resourceContent.addAll( Files.readAllLines( file.toPath() ) );
        return resourceContent;
    }

    private Writer createResource(final String resourcePath ) throws IOException {
        val resource = processingEnv.getFiler().getResource( this.outputLocation, "", resourcePath );
        val uri = resource.toUri();
        createNeededDirectoriesTo( uri );
        val file = createFile( uri );
        return new FileWriter( file );
    }

    private void createNeededDirectoriesTo(final URI uri) throws IOException {
        val dir = ( uri.isAbsolute() )
                ? new File( uri ).getParentFile()
                : new File( uri.toString() ).getParentFile();
        if ( !dir.exists() && !dir.mkdirs() )
            throw new IOException("Can't create " + dir.getAbsolutePath());
    }

    private File createFile(final URI uri) throws IOException {
        val file = new File( uri );
        if ( !file.exists() && !file.createNewFile() )
            throw new IOException("Can't create " + file.getAbsolutePath());
        return file;
    }

    protected void info(final String msg) {
        processingEnv.getMessager().printMessage( Diagnostic.Kind.NOTE, msg );
    }
}
