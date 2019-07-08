package injector;

import java.lang.annotation.*;

/**
 * Marks a method to keep it running in background as an infinite loop (e.g. {@code while(true)}).
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Mainloop {

    /**
     * Defines how many instances of this mainloop job will be running at the same time.
     * This is specially useful when you need intend to implement queue consumers.
     */
    int instances() default 1;

    /**
     * The maximum amount of time (seconds) that should be wait for tasks being finished.
     */
    int gracefulShutdownTime() default 120;
}