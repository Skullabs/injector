# Configuring your project
Injector makes heavy use of annotation processor, so make sure you have
properly configured you project to support it. All assets generated are
compatible with Java version 8 or superior.

!!! important
    Kotlin users should have `kapt` properly configured in order to have
    injector working effectively.

## Import the libraries
Injector is made by two small libraries:

- **injector-core** - [_required at runtime_] contains everything required
to run an injector-based application.
- **injector-processor** - [_optional_] the annotation processor library responsible
for generating factories and metadata required to perform the dependence injection.

All of them are available in Maven Central and you can import on your Gradle/Maven
project as below described.

```groovy tab="Gradle"
repositories {
    mavenCentral()
}

dependencies {
    compile     'io.skullabs.injector:injector-core:1.1.0.Final'
    compileOnly 'io.skullabs.injector:injector-processor:1.1.0.Final'
}
```

```yaml tab="Maven (yaml)"
dependencies:
- { groupId: io.skullabs.injector, artifactId: injector-core, version: 1.1.0.Final }
- { groupId: io.skullabs.injector, artifactId: injector-processor, version: 1.1.0.Final, scope: provided }
```

```xml tab="Maven (xml)"
<dependency>
    <groupId>io.skullabs.injector</groupId>
    <artifactId>injector-core</artifactId>
    <version>1.1.0.Final</version>
</dependency>
<dependency>
    <groupId>io.skullabs.injector</groupId>
    <artifactId>injector-processor</artifactId>
    <version>1.1.0.Final</version>
    <scope>provided</scope>
</dependency>
```

