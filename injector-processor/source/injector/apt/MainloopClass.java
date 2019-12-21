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

import generator.apt.SimplifiedAST;
import injector.Mainloop;
import lombok.Value;
import lombok.val;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@Value
class MainloopClass {

    final String jdkGeneratedAnnotation;
    final String packageName;
    final String simpleName;
    final String className;
    final String method;
    final int numberOfInstances;
    final int gracefulShutdownTime;

    static Iterable<MainloopClass> from( SimplifiedAST.Type type ) {
        val counter = new AtomicInteger(0);
        return Extensions.convert(
            extractMethodName(type),
            method -> new MainloopClass(
                type.getJdkGeneratedAnnotation(),
                type.getPackageName(),
                type.getSimpleName(),
                type.getSimpleName() + "MainloopRunner" + (counter.getAndIncrement()),
                method.getName(),
                method.numberOfInstances(),
                method.gracefulShutdownTime()
            )
        );
    }

    private static Iterable<MainloopMethod> extractMethodName(SimplifiedAST.Type type) {
        return Extensions.convert(
            Extensions.filter(
                type.getMethods(),
                m -> Extensions.first(
                    m.getAnnotations(),
                    ann -> ann.getType().equals( Mainloop.class.getCanonicalName() )
                ).isPresent()
            ),
            MainloopMethod::from
        );
    }

    String getClassCanonicalName(){
        return packageName + "." + className;
    }
}

@Value
class MainloopMethod {
    final String name;
    final Optional<SimplifiedAST.Annotation> annotation;

    int numberOfInstances(){
        val ann = annotation.get();
        val value = ann.getParameters().getOrDefault("instances", "1").toString();
        return Integer.parseInt( value );
    }

    int gracefulShutdownTime(){
        val ann = annotation.get();
        val value = ann.getParameters().getOrDefault("gracefulShutdownTime", "120").toString();
        return Integer.parseInt( value );
    }

    static MainloopMethod from(SimplifiedAST.Method method) {
        if ( !method.getParameters().isEmpty() )
            throw new RuntimeException("Methods annotated with @Mainloop should have no parameters.");

        return new MainloopMethod(
            method.getName(),
            Extensions.first(
                method.getAnnotations(),
                a -> a.getType().equals(Mainloop.class.getCanonicalName())
            )
        );
    }
}

interface Extensions {

    static <T> Optional<T> first( Iterable<T> data, Function<T, Boolean> matcher ) {
        for (val item: data){
            if ( matcher.apply(item) )
                return Optional.of( item );
        }
        return Optional.empty();
    }

    static <T> Iterable<T> filter( Collection<T> data, Function<T, Boolean> matcher ) {
        val buffer = new ArrayList<T>();
        for ( val item : data )
            if (matcher.apply(item))
                buffer.add(item);
        return buffer;
    }

    static <T,N> List<N> convert(Iterable<T> data, Function<T,N> converter) {
        val buffer = new ArrayList<N>();
        for ( val item : data )
            buffer.add( converter.apply(item) );
        return buffer;
    }
}