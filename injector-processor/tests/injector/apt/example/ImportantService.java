package injector.apt.example;

public class ImportantService {

    final NonSingletonService nonSingletonService;

    public ImportantService(NonSingletonService nonSingletonService) {
        this.nonSingletonService = nonSingletonService;
    }
}
