package injector.apt;

import injector.Producer;
import injector.Singleton;

@Singleton
public class ProducerOfImportantServices {

    @Producer
    ImportantService produceImportantService( NonSingletonWithConstructor nonSingletonWithConstructor ){
        return new ImportantService( nonSingletonWithConstructor.nonSingletonService );
    }

    @Producer
    SecondImportantService produceSecondImportantService( NonSingletonWithConstructor nonSingletonWithConstructor ){
        return new SecondImportantService( nonSingletonWithConstructor );
    }
}
