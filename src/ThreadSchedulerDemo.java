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
            System.out.printf("\t[Task %s] %s waiting %s cycles until continue%n", timestamp(), Thread.currentThread(), ticks);
            LockSupport.park();
        }
        public boolean completed;
    }

    @FunctionalInterface
    interface ConsumerEx<T> {
        void accept(T t) throws Exception;
    }

    static final ArrayList<Script> tasks = new ArrayList<>();
    static void run(ConsumerEx<Script> script) {
        Script placeholder = new Script();
        tasks.add(placeholder);

        placeholder.thread = Thread.ofVirtual().unstarted(() -> {
            try {
                script.accept(placeholder);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.out.printf("\t[Task %s] %s done!%n", timestamp(), placeholder.thread);
            placeholder.completed = true;
        });
        placeholder.thread.start();
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("[App "+timestamp()+"] begin");
        run(s -> {
            System.out.println("\t[Task "+timestamp()+"] start script");
            s.wait(2); // expect this to park
            System.out.println("\t[Task "+timestamp()+"] hi!");
        });
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
}
