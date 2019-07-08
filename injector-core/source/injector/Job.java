package injector;

/**
 * A simple representation of a job. It was primarily designed for the Mainloop
 * annotation, allowing background jobs to be triggered transparently. Developers
 * are encouraged to implement their own whenever the default one is does not
 * fits their requirements. It's expected though that Jobs will take care of
 * its lifecycle.
 *
 * <pre><code>
 *     // Custom Job
 *     &#64;Exposed(Job.class)
 *     class MyCustomJob implements Job {
 *
 *          public void execute(){
 *              System.out.println("This job will be run once!");
 *          }
 *     }
 *
 *     // Mainloop job
 *     class MyJob {
 *
 *          &#64;Mainloop void run(){
 *              System.out.println("This method will be run infinitely!");
 *          }
 *     }
 *
 *     // Triggering both jobs
 *     Injector.create().instancesExposedAs( Job.class ).execute();
 * </code></pre>
 */
public interface Job {

    void execute() throws Exception;
}
