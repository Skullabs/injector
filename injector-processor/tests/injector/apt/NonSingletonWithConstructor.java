package injector.apt;

import injector.New;

@New
public class NonSingletonWithConstructor {

    final NonSingletonService nonSingletonService;
    final SingletonService singletonService;

    public NonSingletonWithConstructor(NonSingletonService nonSingletonService, SingletonService singletonService) {
        this.nonSingletonService = nonSingletonService;
        this.singletonService = singletonService;
    }
}
