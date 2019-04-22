package injector.apt.exposed;

import injector.*;

public class CalculatorInjectorFactory implements Factory<Calculator> {

    volatile private Calculator instance;

    public Calculator create(Injector context, Class targetClass) {
        if ( instance == null )
            synchronized (this) {
                if ( instance == null )
                    instance = newInstance( context, targetClass );
            }
        return instance;
    }

    public Class<Calculator> getExposedType() {
        return Calculator.class;
    }

    private Calculator newInstance(Injector context, Class targetClass){
        return new Calculator(


context.instancesExposedAs( injector.apt.exposed.MathOperation.class )
        );
    }
}