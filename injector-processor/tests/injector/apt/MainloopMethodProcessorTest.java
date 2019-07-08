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

import lombok.*;
import org.junit.jupiter.api.*;

import javax.tools.*;
import java.io.*;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class MainloopMethodProcessorTest {

    String
        generatedClassName1 = MainloopAnnotatedClass.class.getCanonicalName() + "MainloopRunner0",
        generatedClassName2 = MainloopAnnotatedClass.class.getCanonicalName() + "MainloopRunner1"
    ;

    InjectorProcessor injectorProcessor = new InjectorProcessor();
    MainloopMethodProcessor processor = new MainloopMethodProcessor();

    @DisplayName("SHOULD generate class with default num of instances WHEN find a method properly annotated")
    @Test void generateClasses() throws IOException
    {
        val source = APT.asSource( APT.testFile(MainloopAnnotatedClass.class) );
        APT.runner().run( asList(source), asList(processor/*, injectorProcessor*/) ).printErrorsIfAny( d -> {
            if ( d.getKind() == Diagnostic.Kind.ERROR )
                throw new RuntimeException( d.toString() );
            else
                System.out.println( d );
        });

        val generatedClass = APT.readFileAsString(APT.outputGeneratedClass(generatedClassName1));
        val expectedClass = APT.testResourceAsString("expected-generated-mainloop.txt");
        assertEquals(expectedClass, generatedClass);
    }

    @DisplayName("SHOULD generate class respecting number of instances WHEN find a method properly annotated")
    @Test void generateClasses1() throws IOException
    {
        val source = APT.asSource( APT.testFile(MainloopAnnotatedClass.class) );
        APT.runner().run( asList(source), asList(processor, injectorProcessor) ).printErrorsIfAny( d -> {
            if ( d.getKind() == Diagnostic.Kind.ERROR )
                throw new RuntimeException( d.toString() );
        });

        val generatedClass = APT.readFileAsString(APT.outputGeneratedClass(generatedClassName2));
        val expectedClass = APT.testResourceAsString("expected-generated-mainloop2.txt");
        assertEquals(expectedClass, generatedClass);
    }

    @DisplayName("SHOULD expose the generated class as 'SPI' WHEN find classes/methods properly annotated")
    @Test void generateClasses2() throws IOException
    {
        val source = APT.asSource( APT.testFile(MainloopAnnotatedClass.class) );
        APT.runner().run( asList(source), asList(processor, injectorProcessor) ).printErrorsIfAny();

        val spiFileLocation = APT.outputGeneratedFile(processor.getSpiLocation());
        val spiFile = APT.readFileAsString( spiFileLocation );

        val expected = generatedClassName1 + MainloopMethodProcessor.EOL;
        assertTrue(spiFile.contains(expected));
    }
}
