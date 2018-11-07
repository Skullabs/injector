package injector.apt;

import injector.New;
import lombok.Getter;

import java.util.concurrent.ThreadLocalRandom;

@New
public class NonSingletonService {

    @Getter
    final int identifier = ThreadLocalRandom.current().nextInt();
}
