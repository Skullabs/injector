package injector.apt;

import injector.*;

public class ImportantServiceFactory implements Factory<ImportantService> {

    @Override
    public ImportantService create(Injector context, Class targetClass) {
        return context.instanceOf(injector.apt.ProducerOfImportantServices.class, targetClass).produceImportantService(
        targetClass
,
context.instanceOf( injector.apt.NonSingletonWithConstructor.class, targetClass )


        );
    }

    @Override
    public Class<ImportantService> getExposedType() {
        return ImportantService.class;
    }
}
