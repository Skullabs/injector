package injector.scenarios.cyclic;

import injector.ExposedServicesLoader;
import injector.Factory;
import injector.Injector;
import injector.scenarios.cyclic.Scenario1.Child;
import injector.scenarios.cyclic.Scenario1.Parent;
import lombok.Getter;
import lombok.val;

import java.util.Collections;

public interface Scenario1Loaders {

    @Getter
    class ParentFactory implements Factory<Parent> {

        final Class<Parent> exposedType = Parent.class;
        
        private Parent parent;

        @Override
        public Parent create(Injector context, Class target) {
            if (parent == null)
                parent = new Parent(
                    context.instancesExposedAs(Child.class)
                );
            return parent;
        }
    }

    @Getter
    class ChildFactory implements ExposedServicesLoader<Child> {

        final Class<Child> exposedType = Child.class;

        @Override
        public Iterable<Child> load(Injector context) {
            val child = new Child(context.instanceOf(Parent.class));
            return Collections.singletonList(child);
        }
    }
}
