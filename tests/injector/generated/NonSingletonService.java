package injector.generated;

import lombok.Getter;

import java.util.concurrent.ThreadLocalRandom;

public class NonSingletonService {

    @Getter
    final int identifier = ThreadLocalRandom.current().nextInt();
}
