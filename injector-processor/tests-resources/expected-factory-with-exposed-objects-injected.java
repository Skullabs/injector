package injector.apt.exposed;

import injector.*;

public class CalculatorFactory implements Factory<Calculator> {

    volatile private Calculator instance;

    @Override
    public Calculator create(Injector context) {
        if ( instance == null )
            synchronized (this) {
                if ( instance == null )
                    instance = newInstance( context );
            }
        return instance;
    }

    @Override
    public Class<Calculator> getExposedType() {
        return Calculator.class;
    }

    private Calculator newInstance(Injector context){
        return new Calculator(


context.instancesExposedAs( injector.apt.exposed.MathOperation.class )
        );
    }
}