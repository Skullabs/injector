package injector.generated.exposed;

public class Sum implements MathOperation {

    @Override
    public Integer apply(Integer integer, Integer integer2) {
        return integer + integer2;
    }
}