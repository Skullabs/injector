package injector.apt.example;

import injector.Exposed;

import java.util.function.Supplier;

@Exposed
public class IllegallyExposedClass implements Supplier<Object> {

    @Override
    public Object get() {
        return null;
    }
}
