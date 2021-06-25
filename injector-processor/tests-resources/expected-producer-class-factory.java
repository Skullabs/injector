package injector.apt;

import injector.*;

@javax.annotation.processing.Generated("injector.apt.InjectorProcessor")
public class ImportantServiceInjectorFactory78289948 implements Factory<ImportantService> {

    public ImportantService create(Injector context, Class targetClass) {
        return context.instanceOf(injector.apt.ProducerOfImportantServices.class, targetClass).produceImportantService(

targetClass
,

context.instanceOf( injector.apt.NonSingletonWithConstructor.class, getExposedType() )


        );
    }

    public Class<ImportantService> getExposedType() {
        return ImportantService.class;
    }
}
