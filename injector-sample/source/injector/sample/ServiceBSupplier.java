package injector.sample;

import injector.Exposed;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Exposed
@RequiredArgsConstructor
public class ServiceBSupplier implements Supplier<ServiceB> {

    final ServiceB serviceB;
    
    @Override
    public ServiceB get() {
        return serviceB;
    }
}
