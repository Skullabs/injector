package injector.apt.example;

import injector.Singleton;

@Singleton
public class SingletonWithConstructor {

    final NonSingletonService singletonService;
    final NonSingletonWithConstructor nonSingletonWithConstructor;

    public SingletonWithConstructor(NonSingletonService singletonService, NonSingletonWithConstructor nonSingletonWithConstructor) {
        this.singletonService = singletonService;
        this.nonSingletonWithConstructor = nonSingletonWithConstructor;
    }

    public int getIdentifier(){
        return singletonService.getIdentifier();
    }
}
