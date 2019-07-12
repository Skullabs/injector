# Overview
Injector is lightweight and zero-overhead dependency injection library for JVM developers.
It was carefully designed to make no-use of reflection by having all required meta-information
computed at compile time. At runtime it performs only the necessary tasks required to instantiate
classes and have its dependencies injected.

## Why Injector is different?
"Often in discussions about dependency injection, people mix the pattern with how it is
supported by containers. There is plenty to criticize when it comes to many of these containers,
but the core pattern is easy to appreciate: _instead of letting objects assemble their own
dependencies, classes state what dependencies they require from clients in order to be
constructed and used_."
[[1](https://engineering.snagajob.com/dont-like-dependency-injection-898de93dc8d3#a541)]

Injector would be better described as small library with a single responsibility: inject
dependencies on your objects. Despite of its small footprint (<7kb), it optimizes everything
it requires to perform its tasks at compilation time, providing a zero-overhead runtime
for your projects.

## Dependency injection in action
```kotlin tab="Kotlin"
import injector.*;

data class User(
    val id: UUID = UUID.randomUUID()
    val name: String,
    val isPayingUser: Boolean
)

@Singleton class UserPersistence {
    private val users = mutableMapOf<UUID, User>()

    fun save( user: User ) {
        users.put( user.id, user );
    }

    fun loadAll(): Map<UUID, User> = users
}

@Singleton class UserService( val persistence: UserPersistence ) {

    fun save( user: User ){
        if (user.isPayingUser)
            persistence.save( user )
    }
}

fun main() {
    val service = Injector.create().instanceOf( UserService.class )
    val user = User("joe@dinner.com", true)
    service.save( user )
}
```

```kotlin tab="Java"
import injector.*;
import java.util.*;

class User {
    final UUID id = UUID.randomUUID();
    final String name;
    final Boolean isPayingUser;

    User( String name, Boolean isPayingUser ){
        this.name = name;
        this.isPayingUser = isPayingUser;
    }
}

@Singleton
class UserPersistence {
    private final Map<UUID,User> users = new HashMap<>();

    void save( User user ) {
        users.put( user.id, user );
    }
}

@Singleton
class UserService {

    final UserPersistence persistence;

    UserService( UserPersistence persistence ){
        this.persistence = persistence;
    }

    void save( User user ){
        if (user.isPayingUser)
            persistence.save( user );
    }
}

fun main() {
    UserService service = Injector.create().instanceOf(UserService.class);
    User user = new User("joe@dinner.com", true);
    service.save( user );
}
```


## License
Kos is release under Apache License 2 terms.
