package injector.apt.example;

import injector.Singleton;

@Singleton
public class SingletonService {

    int identifier = 12345;

    public int getIdentifier(){
        return identifier;
    }
}
