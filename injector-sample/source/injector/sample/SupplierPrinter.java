package injector.sample;

import injector.AllOf;
import injector.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Singleton
@RequiredArgsConstructor
public class SupplierPrinter {
    
    @AllOf(Supplier.class)
    final Iterable<Supplier> suppliers;

    public void printAllOfThem(){
        for (val supplier : suppliers) {
            System.out.println(supplier.get());
        }
    }
}
