import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.locks.LockSupport;

/**
 * A demo
 * @author Jak | Shadowrs (tardisfan121@gmail.com)
 */
public class ThreadSchedulerDemo {

    static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("hh:ss");
    static String timestamp() {
        return DTF.format(LocalDateTime.now());
    }

    public static class Script {
        public boolean ready;
        public Thread thread;

        public int ticks;

        public final void wait(int ticks) {
            this.ticks = ticks;
            printf("%s waiting %s cycles until continue", Thread.currentThread(), ticks);
            LockSupport.park();
        }
        public boolean completed;

        public void printf(String format, Object... args) {
            System.out.printf("\t[Task %s] %s%n", timestamp(), String.format(format, args));
        }
    }

    @FunctionalInterface
    interface ConsumerEx<T> {
        void accept(T t) throws Exception;
    }

    private static final ArrayList<Script> tasks = new ArrayList<>();
    public static void run(ConsumerEx<Script> work) {
        Script script = new Script();
        tasks.add(script);

        script.thread = Thread.ofVirtual().unstarted(() -> {
            try {
                work.accept(script);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            script.printf("%s done!", script.thread);
            script.completed = true;
        });
        script.thread.start();
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("[App "+timestamp()+"] begin");
        test1();
        Demo.INSTANCE.test();// kick off a kotlin written task
        int cycles = 0;
        while (true) {
            System.out.println("[Scheduler "+timestamp()+"] loop "+cycles+" on main thread "+Thread.currentThread().getClass().getName());
            tasks.removeIf(s -> s.completed);
            for (Script task : tasks) {
                if (task.ticks == 0)
                    task.ready = true;
                if (task.ready) {
                    System.out.println("[Scheduler "+timestamp()+"] >> resuming task with thread: "+task.thread);
                    LockSupport.unpark(task.thread);
                }
                if (task.ticks > 0) {
                    task.ticks--;
                }
            }
            tasks.removeIf(s -> s.completed);
            if (tasks.size() == 0)
                System.exit(0);
            cycles++;
            Thread.sleep(Duration.ofMillis(1_000));
        }
    }

    // p.s test() method body hotswap will work, but standard Java does not support hotswap of Anonymous classes (lambda expressions: code within the run({}) block)
    private static void test1() {
        run(s -> {
            System.out.println("\t[Task "+timestamp()+"] start script");
            int i = 5;
            while (i-- > 0) {
                s.wait(2); // expect this to park
                s.printf("heyXX %s", i);
            }
            test1();
        });
    }
}
