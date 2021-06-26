package injector.apt.example;

public class SecondImportantService {

    final NonSingletonWithConstructor nonSingletonWithConstructor;

    public SecondImportantService(NonSingletonWithConstructor nonSingletonWithConstructor) {
        this.nonSingletonWithConstructor = nonSingletonWithConstructor;
    }
}
