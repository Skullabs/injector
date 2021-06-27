package injector.apt.example;

import injector.*;

@javax.annotation.processing.Generated("injector.apt.InjectorProcessor")
public class ImportantServiceInjectorFactory implements Factory<ImportantService> {

    public ImportantService create(Injector context, Class targetClass) {
        return context.instanceOf(injector.apt.example.ProducerOfImportantServices.class, targetClass).produceImportantService(

targetClass
,

context.instanceOf( injector.apt.example.NonSingletonWithConstructor.class, getExposedType() )


        );
    }

    public Class<ImportantService> getExposedType() {
        return ImportantService.class;
    }
}
