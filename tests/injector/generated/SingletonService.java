package injector.generated;

import injector.Inject;

public class SingletonService {

    @Inject
    NonSingletonService repository;

    public int getIdentifier(){
        return repository.getIdentifier();
    }
}
