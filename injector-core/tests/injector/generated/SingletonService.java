package injector.generated;

public class SingletonService {

    NonSingletonService repository;

    public int getIdentifier(){
        return repository.getIdentifier();
    }
}
