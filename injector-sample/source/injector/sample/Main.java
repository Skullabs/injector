package injector.sample;

import injector.*;
import lombok.*;

public class Main {

    public static void main( String[] args) {
        val injector = Injector.create();

        val serviceA = injector.instanceOf( ServiceA.class );
        serviceA.notifyServiceB();

        val printer = injector.instanceOf(SupplierPrinter.class);
        printer.printAllOfThem();
    }
}
