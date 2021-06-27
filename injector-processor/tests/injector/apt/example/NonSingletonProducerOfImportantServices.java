package injector.apt.example;

import injector.Producer;

public class NonSingletonProducerOfImportantServices {

    @Producer
    ImportantService produceImportantService( Class targetClass, NonSingletonWithConstructor nonSingletonWithConstructor ){
        return new ImportantService( nonSingletonWithConstructor.nonSingletonService );
    }

    @Producer
    SecondImportantService produceSecondImportantService( NonSingletonWithConstructor nonSingletonWithConstructor ){
        return new SecondImportantService( nonSingletonWithConstructor );
    }
}
