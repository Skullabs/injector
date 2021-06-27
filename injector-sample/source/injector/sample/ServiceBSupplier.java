package injector.sample;

import injector.Exposed;
import lombok.RequiredArgsConstructor;

@Exposed
@RequiredArgsConstructor
public class ServiceBSupplier implements Supplier<ServiceB> {

    final ServiceB serviceB;
    
    @Override
    public ServiceB get() {
        return serviceB;
    }
}
