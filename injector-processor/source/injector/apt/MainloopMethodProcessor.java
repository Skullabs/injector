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

import com.github.mustachejava.*;
import generator.apt.*;
import injector.*;
import lombok.*;

import javax.annotation.processing.*;
import java.io.*;
import java.util.*;

@SupportedAnnotationTypes( { "injector.*" } )
public class MainloopMethodProcessor extends SPIGeneratorAnnotationProcessor {

    private static final String ROUTE_CLASS_TEMPLATE = "mainloop-runner-java.mustache";
    private final MustacheFactory mf = new DefaultMustacheFactory();

    public MainloopMethodProcessor() {
        super(
            Job.class,
            Collections.emptyList(),
            Collections.singletonList(Mainloop.class),
            Collections.emptyList()
        );
    }

    @Override
    protected void process(Collection<SimplifiedAST.Type> types) {
        cleanSPICache();

        try {
            for (val type : types)
                generateClasses(MainloopClass.from(type));
            flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateClasses(Iterable<MainloopClass> classes) throws IOException {
        for ( val type : classes ) {
            val filer = processingEnv.getFiler();
            val source = filer.createSourceFile( type.getClassCanonicalName() );
            try ( val writer = source.openWriter() ) {
                val mustache = mf.compile(ROUTE_CLASS_TEMPLATE);
                mustache.execute( writer, type );
            }

            memorizeSPIFor( type.getClassCanonicalName() );
        }
    }
}
