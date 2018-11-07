package injector.apt;

import generator.apt.SimplifiedAST;
import injector.New;
import injector.Producer;
import injector.Singleton;
import lombok.Value;
import lombok.val;

import java.util.List;
import java.util.stream.Collectors;

@Value
class InjectorTypes {
    InjectorType regular;
    List<InjectorType> producers;
}

class InjectorType extends SimplifiedAST.Type {

    public boolean isSingleton(){
        return getAnnotation(Singleton.class) != null;
    }

    public boolean isNew(){
        return getAnnotation(New.class) != null;
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
}