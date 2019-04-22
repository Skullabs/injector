package injector.apt;

import injector.*;

public class ImportantServiceFactory implements Factory<ImportantService> {

    @Override
    public ImportantService create(Injector context) {
        return context.instanceOf(injector.apt.ProducerOfImportantServices.class).produceImportantService(

context.instanceOf( injector.apt.NonSingletonWithConstructor.class )


        );
    }

    @Override
    public Class<ImportantService> getExposedType() {
        return ImportantService.class;
    }
}
