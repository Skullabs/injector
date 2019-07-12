# Injecting Dependencies

## Injector Context
Every project should have at least one Injector Context instantiated. It
is responsible for holding objects references so you can ask for them anytime later.

```kotlin tab="Kotlin"
val injector = Injector.creator()
```

```kotlin tab="Java"
Injector injector = Injector.creator();
```

## Constructor Injection
In order to keep dependency injection experience as close as possible to _vanilla java_,
Injector opted for [constructor injection](https://en.wikipedia.org/wiki/Dependency_injection#Constructor_injection_comparison)
as its main (and only) injection mechanism. That's a good strategy towards immutability
and other [clean-code](https://en.wikipedia.org/wiki/SOLID) good practices.

## Making classes injectable
Injector will allow developers to expose objects in two ways:

- [Singleton](https://en.wikipedia.org/wiki/Singleton_pattern) - classes exposed like this will
have only one instance of its type registered in the Injection Context. To expose singleton classes
you should annotated them with the `injector.Singleton` annotation.
- Regular classes - as the name states this isn't a special type but a class in which a new
instance will be created every time it should be injected. To expose normal classes you
should annotated them with the `injector.New` annotation.

```kotlin tab="Kotlin"
@Singleton class SingletonServiceA
@New class RegularServiceB

@Singleton class SingletonServiceB(
    val serviceA: SingletonServiceA,
    val serviceB: RegularServiceB
)

@Singleton class SingletonServiceC(
    val serviceA: SingletonServiceA,
    val serviceB: RegularServiceB
)
```

```java tab="Java"
@Singleton class SingletonServiceA {}
@New class RegularServiceB {}

@Singleton class SingletonServiceB {

    final SingletonServiceA serviceA;
    final RegularServiceB serviceB;

    SingletonServiceB( SingletonServiceA serviceA, RegularServiceB serviceB ) {
        this.serviceA = serviceA;
        this.serviceB = serviceB;
    }
}

@Singleton class SingletonServiceC {

    final SingletonServiceA serviceA;
    final RegularServiceB serviceB;

    SingletonServiceC( SingletonServiceA serviceA, RegularServiceB serviceB ) {
        this.serviceA = serviceA;
        this.serviceB = serviceB;
    }
}
```

If we instantiate an Injector Context, we're going to have one instance of SingletonServiceA,
SingletonServiceB and SingletonServiceC - as they are singletons - and two instances of
RegularServiceB - as it is a regular class.

## Retrieving your injectable service
Once you expose your services you'll be able to ask the Injector Context for instances and
it will take care of instantiate them and return the instance as you need.

```kotlin tab="Kotlin"
val injector = Injector.creator()
val serviceC = injector.instanceOf( SingletonServiceC.class )
```

```kotlin tab="Java"
Injector injector = Injector.creator();
SingletonServiceC serviceC = injector.instanceOf( SingletonServiceC.class );
```

Injector will respect the aforementioned rules regarding singletons, thus anytime you ask
for an instance of `SingletonServiceC` you're going to receive the same instance.
Note though that you're going to receive different instances if you ask for an instance of
`RegularServiceB`.
