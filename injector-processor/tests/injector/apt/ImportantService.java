package injector.apt;

public class ImportantService {

    final NonSingletonService nonSingletonService;

    public ImportantService(NonSingletonService nonSingletonService) {
        this.nonSingletonService = nonSingletonService;
    }
}
