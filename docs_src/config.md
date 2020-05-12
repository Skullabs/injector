# Configuring your project
Injector makes heavy use of annotation processor, so make sure you have
properly configured you project to support it. All generated assets are
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

```groovy tab="Gradle (Kotlin)"
repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("io.skullabs.injector:injector-bom:1.4.0"))

    implementation("io.skullabs.injector:injector-core")
    compileOnly("io.skullabs.injector:injector-processor")
}
```

```yaml tab="Maven (pom.kts)"
dependencies {
    compile("io.skullabs.injector:injector-core")
    provided("io.skullabs.injector:injector-processor")
}

dependencyManagement {
    dependencies {
        import("io.skullabs.injector:injector-bom:1.4.0")
    }
}
```

```xml tab="Maven (pom.xml)"
<dependencies>
    <dependency>
        <groupId>io.skullabs.injector</groupId>
        <artifactId>injector-core</artifactId>
    </dependency>
    <dependency>
        <groupId>io.skullabs.injector</groupId>
        <artifactId>injector-processor</artifactId>
    </dependency>
</dependencies>

<dependencyManagement>
    <dependency>
        <groupId>io.skullabs.injector</groupId>
        <artifactId>injector-bom</artifactId>
        <version>1.4.0</version>
        <scope>import</scope>
        <type>pom</type>
    </dependency>
</dependencyManagement>
```
