package injector.apt.exposed;

import injector.DefaultExposedServicesLoader;

public class SumExposedServicesLoader extends DefaultExposedServicesLoader<injector.apt.exposed.Sum> {

    @Override
    public Class<Sum> getExposedType() {
        return Sum.class;
    }
}
