import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.IntStream;

/**
 * -source 19 --enable-preview
 * with javac (in IDEA settings)
 * DISABLE "preview" tickbox -> idea doesnt like it
 *
 * expect in IDEA cmd: "java: User-specified option "-source" for "loomdemo" may conflict with the corresponding option calculated automatically according to project settings."
 *
 * Then app VM args --enable-preview
 */
public class Main {
//https://openjdk.java.net/jeps/425
    public static void main(String[] args) {
        System.out.println("begin");
        Runnable runnable = () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.printf("hi%n");
        };
        // Start a virtual thread to run a task.
        Thread thread = Thread.ofVirtual().start(runnable);
        // A ThreadFactory that creates virtual threads
        ThreadFactory factory = Thread.ofVirtual().factory();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 2).forEach(i -> {
                executor.submit(() -> {
                    Thread.sleep(Duration.ofSeconds(2));
                    System.out.println("aye");
                    return i;
                });
            });
        }  // executor.close() is called implicitly, and waits
    }
}
