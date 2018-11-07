package injector.apt;

import com.google.common.collect.ImmutableList;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import generator.apt.SimplifiedAPTRunner;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;

import javax.annotation.processing.Processor;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Locale;

import static com.google.testing.compile.Compiler.javac;
import static java.util.Arrays.asList;

@SuppressWarnings("unchecked")
@UtilityClass
public class APT {

    public SimplifiedAPTRunner runner(){
        return new SimplifiedAPTRunner( APT.createDefaultConfiguration(), ToolProvider.getSystemJavaCompiler() );
    }

    public SimplifiedAPTRunner.Config createDefaultConfiguration(){
        final SimplifiedAPTRunner.Config config = new SimplifiedAPTRunner.Config();
        config.sourceDir = asList( new File( "tests" ), new File( "tests-resources" ) );
        config.outputDir = asList( new File( "output/apt" ) );
        config.classOutputDir = asList( new File( "output/apt" ) );
        return config;
    }

    public File testFile(Class clazz ) {
        val path = clazz.getCanonicalName().replace('.', '/');
        return new File("tests/" + path + ".java");
    }

    public String testResourceAsString(String path ) throws IOException {
        return readFileAsString( new File("tests-resources/" + path) );
    }

    public File outputGeneratedClass(String clazz ) {
        val path = clazz.replace('.', '/');
        return new File("output/apt/" + path + ".java");
    }

    public String readFileAsString( File file ) throws IOException {
        val bytes = Files.readAllBytes(file.toPath());
        return new String( bytes, StandardCharsets.UTF_8);
    }
}