package injector.scenarios.cyclic;

import injector.Factory;
import injector.Injector;
import injector.scenarios.cyclic.Scenario2.Child;
import injector.scenarios.cyclic.Scenario2.Parent;
import lombok.Getter;

public interface Scenario2Loaders {

    @Getter
    class ParentFactory implements Factory<Parent> {

        final Class<Parent> exposedType = Parent.class;
        
        private Parent parent;

        @Override
        public Parent create(Injector context, Class target) {
            if (parent == null)
                parent = new Parent(
                    context.instanceOf(Child.class)
                );
            return parent;
        }
    }

    @Getter
    class ChildFactory implements Factory<Child> {

        final Class<Child> exposedType = Child.class;
        
        private Child child;

        @Override
        public Child create(Injector context, Class target) {
            if (child == null)
                child = new Child(
                    context.instanceOf(Parent.class)
                );
            return child;
        }
    }
}
