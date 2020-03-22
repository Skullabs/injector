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

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.*;
import java.io.*;
import java.lang.annotation.*;
import java.util.*;

@Getter
public abstract class SPIGeneratorAnnotationProcessor extends SimplifiedAbstractProcessor {

    static final String EOL = "\n";

    private final JavaFileManager.Location outputLocation = StandardLocation.CLASS_OUTPUT;
    private final ServiceProviderImplementations spiClasses = new ServiceProviderImplementations();

    private final String spiClass;

    public SPIGeneratorAnnotationProcessor(
            Class<?> spiType,
            List<Class<? extends Annotation>> fieldAnnotations,
            List<Class<? extends Annotation>> methodAnnotations,
            List<Class<? extends Annotation>> typeAnnotations)
    {
        super(fieldAnnotations, methodAnnotations, typeAnnotations);
        spiClass = spiType.getCanonicalName();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        spiClasses.use(resourceLocator);
    }

    protected void cleanSPICache(){
        spiClasses.cleanCaches();
    }

    protected void memorizeSPIFor( String canonicalClassName ) {
        spiClasses.memorizeImplementationOf( spiClass, canonicalClassName );
    }

    protected void flush() throws IOException {
        spiClasses.flush();
    }
}
