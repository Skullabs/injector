package injector.apt.example.calc;

import injector.*;

@javax.annotation.processing.Generated("injector.apt.InjectorProcessor")
public class CalculatorInjectorFactory implements Factory<Calculator> {

    volatile private Calculator instance;

    public Calculator create(Injector context, Class targetClass) {
        if ( instance == null )
            synchronized (this) {
                if ( instance == null )
                    instance = newInstance( context );
            }
        return instance;
    }

    public Class<Calculator> getExposedType() {
        return Calculator.class;
    }

    private Calculator newInstance(Injector context){
        return new Calculator(


context.instancesExposedAs( injector.apt.example.calc.MathOperation.class )
        );
    }
}