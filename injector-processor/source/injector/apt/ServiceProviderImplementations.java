package injector.apt;

import generator.apt.ResourceLocator;
import lombok.val;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ServiceProviderImplementations {

    static final String EOL = "\n";
    static final String SPI_LOCATION = "META-INF/services/";

    private Map<String, List<String>> spiClasses;
    private CustomResourceLocator resourceLocator;

    void flush() throws IOException {
        for ( val entry : this.spiClasses.entrySet()) {
            generateSPIFile(entry.getKey(), entry.getValue());
        }
    }

    void generateSPIFile(String canonicalName, List<String> implementations) throws IOException {
        val location = SPI_LOCATION + canonicalName;
        val existingImplementations = resourceLocator.readResourceIfExists( location );
        existingImplementations.addAll( implementations );
        try ( final Writer resource = resourceLocator.createResource( location ) ) {
            for (final String implementation : existingImplementations)
                resource.write(implementation + EOL);
        }
    }

    void memorizeImplementationOf(String className, String spiImplementation){
        spiClasses.computeIfAbsent( className, t -> new ArrayList<>() ).add( spiImplementation );
    }

    List<String> getSpiClassesFor( String className ){
        return spiClasses.computeIfAbsent(className, k -> new ArrayList<>() );
    }

    void cleanCaches(){
        spiClasses = new HashMap<>();
    }

    void use(ResourceLocator resourceLocator) {
        this.resourceLocator = new CustomResourceLocator(resourceLocator);
    }
}
