package larx.jdemos;

import larx.Misc;
import larx.engine.ThreadSchedulerDemo;

public class JLambdaDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("[App "+ Misc.timestamp()+"] begin");
        testJavaLammbdaHotswap();
        ThreadSchedulerDemo.mainAppLoop();
    }

    // p.s test() method body hotswap will work, but standard Java does not support hotswap of Anonymous classes (lambda expressions: code within the run({}) block)
    public static void testJavaLammbdaHotswap() {
        ThreadSchedulerDemo.run(s -> {
            System.out.println("\t[Task "+ Misc.timestamp()+"] start script");
            int i = 0;
            s.wait(5);
            System.out.println("well "+i);
            s.wait(5);
            System.out.println("well "+i);
            s.wait(5);
            System.out.println("well "+i);
            s.wait(5);
            System.out.println("well "+i);
            s.wait(5);
            System.out.println("well "+i);
            s.wait(5);
            System.out.println("well "+i);
        });
    }
}
