package injector;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a given constructor is the preferred one
 * to be used when instantiated by Injector. Ideally only
 * one constructor should annotated with this annotation,
 * as Injector uses it for disambiguation. In case of more
 * than one is found, it will randomly pick one.
 *
 * @since 1.5.0
 */
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
public @interface Constructor {
}
