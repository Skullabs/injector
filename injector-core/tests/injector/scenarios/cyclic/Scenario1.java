package injector.scenarios.cyclic;

/**
 * Cyclic Dependency Scenario 1: dependency between parent and child using Iterable.
 * Expected behaviour: allowed.
 * Reason: Iterable wraps dependencies and lazy-loads them.
 */
public interface Scenario1 {

    class Child {

        public final Parent parent;

        public Child(Parent parent) {
            this.parent = parent;
        }
    }

    class Parent {

        public final Iterable<Child> children;

        public Parent(Iterable<Child> children) {
            this.children = children;
        }
    }
}
