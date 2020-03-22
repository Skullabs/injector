package injector.apt;

import generator.apt.SimplifiedAST;
import injector.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.val;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Value
class InjectorTypes {
    InjectorType regular;
    Set<InjectorType> producers;
}

@EqualsAndHashCode(callSuper = true)
class InjectorType extends SimplifiedAST.Type {

    List<SimplifiedAST.Method> fixedMethods;
    String exposedClass;
    List<String> exposedInterfaces;

    public boolean isSingleton(){
        return !isNew();
    }

    public boolean isNew(){
        return getAnnotation(New.class) != null;
    }

    public List<String> getExposedInterfaces() {
        if ( exposedInterfaces == null ) {
            exposedInterfaces = new ArrayList<>();

            val exposed = getAnnotation(Exposed.class);
            if ( exposed != null )
                exposedInterfaces.addAll( loadAllImplementingInterfaces() );
        }
        
        return exposedInterfaces;
    }

    private Collection<? extends String> loadAllImplementingInterfaces() {
        return this.getInheritedInterfaces().stream()
            .map(SimplifiedAST.Type::getCanonicalName)
                .collect(Collectors.toList());
    }

    public String getExposedClass(){
        if ( exposedClass == null ) {
            val exposedAs = getAnnotation(ExposedAs.class);
            if (exposedAs != null)
                exposedClass = exposedAs.getValue().toString()
                    .replaceAll(".class$", "");
        }
        return exposedClass;
    }

    public boolean isAnnotatedWith(Class<? extends Annotation> annotationClass) {
        return getAnnotation(annotationClass) != null;
    }

    @Override
    public String toString() {
        val ann = String.join( " ", getAnnotations().stream().map(i -> i.toString() ).collect(Collectors.toList() ) );
        return ann + ":" + super.toString();
    }
}

class InjectorMethod extends SimplifiedAST.Method {

    public boolean isProducer(){
        return getAnnotation(Producer.class) != null;
    }

    static InjectorMethod from(SimplifiedAST.Method element) {
        return (InjectorMethod) new InjectorMethod()
                .setParameters( InjectorParameter.from(element.getParameters()) )
                .setConstructor(element.isConstructor())
                .setAnnotations( element.getAnnotations() )
                .setType( element.getType() )
                .setName( element.getName() );
    }
}

@ToString(callSuper = true)
class InjectorParameter extends SimplifiedAST.Element {

    public String getAllOf(){
        val allOf = getAnnotation(AllOf.class);
        if ( allOf != null )
            return allOf.getValue().toString();
        return null;
    }

    public boolean isTargetClass(){
        return getType().equals(Class.class.getCanonicalName());
    }

    static SimplifiedAST.Element from(SimplifiedAST.Element element) {
        return new InjectorParameter()
            .setAnnotations( element.getAnnotations() )
            .setType( element.getType() )
            .setName( element.getName() );
    }

    static List<SimplifiedAST.Element> from( List<SimplifiedAST.Element> parameters ) {
        val newParams = new ArrayList<SimplifiedAST.Element>();
        parameters.forEach( p -> newParams.add( from(p) ) );
        return newParams;
    }
}
