import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.locks.LockSupport;

/**
 * A demo
 * @author Jak | Shadowrs (tardisfan121@gmail.com)
 */
public class ThreadSchedulerDemo {

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
            System.out.printf("\t[Task %s] %s%n", Misc.timestamp(), String.format(format, args));
        }
    }

    @FunctionalInterface
    interface ConsumerEx<T> {
        void accept(T t) throws Exception;
    }

    public static final ArrayList<Script> tasks = new ArrayList<>();
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

    public static void mainAppLoop() throws InterruptedException {
        int cycles = 0;
        while (true) {
            System.out.println("[Scheduler "+ Misc.timestamp()+"] loop "+cycles+" on main thread "+Thread.currentThread().getClass().getName());
            tasks.removeIf(s -> s.completed);
            for (Script task : tasks) {
                if (task.ticks == 0)
                    task.ready = true;
                if (task.ready) {
                    System.out.println("[Scheduler "+ Misc.timestamp()+"] >> resuming task with thread: "+task.thread);
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
