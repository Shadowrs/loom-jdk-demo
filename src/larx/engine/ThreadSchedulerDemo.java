package larx.engine;

import larx.Misc;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

/**
 * A demo
 * @author Jak | Shadowrs (tardisfan121@gmail.com)
 */
public class ThreadSchedulerDemo {

    public static class Script {
        public Thread thread;

        public int ticks;

        public final void wait(int ticks) {
            this.ticks = ticks;
            printf("sleeping %s cycles until continue. i am %s", ticks, Thread.currentThread());
            this.ticks--; // TODO schedule/oss check cycle before reducing instead of here
            LockSupport.park();
        }
        public boolean completed;

        public void printf(String format, Object... args) {
            System.out.printf("[%s] %s%n", Misc.timestamp(), String.format(format, args));
        }
    }

    @FunctionalInterface
    public interface ConsumerEx<T> {
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
            script.printf("script reached end. i am %s", script.thread);
            script.completed = true;
        });
        script.thread.start();
        // since virtualThreads start independently, and we actually want it to exec instantly
        // before continuing game logic, we wait
       /* try {
            Thread.currentThread().wait(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/
        // TODO virtual threads run INDEPENDENTLY of the current thread
        // TODO we want to run the work on the CURRENT thread
        // TODO we are managing a STATE MACHINE!
    }

    static Thread gameThread;
    static ScheduledExecutorService scheduledExecutorService;
    static int cycles;

    public static void mainAppLoop() {
        gameThread = Thread.currentThread();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("cya");
            scheduledExecutorService.shutdown();
        }));

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            //System.out.println("[Scheduler "+ Misc.timestamp()+"] loop "+cycles+" on main thread "+Thread.currentThread().getClass().getName());
            tasks.removeIf(s -> s.completed);
            for (Script task : tasks) {

                if (task.ticks == 0) {
                    System.out.println("["+ Misc.timestamp()+"] resuming task. i am "+task.thread);
                    LockSupport.unpark(task.thread);
                }
                else if (task.ticks > 0) {
                    task.printf("sleeping %s more ticks... i am %s", task.ticks, task.thread);
                    task.ticks--;
                }
            }
            tasks.removeIf(s -> s.completed);
           /* if (tasks.size() == 0) {
                System.out.println("fuck off");
                System.exit(0);
            }*/
            cycles++;
        }, 0, 1, TimeUnit.SECONDS);

    }

}
