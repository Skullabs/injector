package injector.scenarios.cyclic;

public interface Scenario2 {

    class Child {

        public final Parent parent;

        public Child(Parent parent) {
            this.parent = parent;
        }
    }

    class Parent {

        public final Child child;

        public Parent(Child child) {
            this.child = child;
        }
    }
}
