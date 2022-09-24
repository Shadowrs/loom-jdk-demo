package larx.jdemos;

import larx.Misc;
import larx.engine.ThreadSchedulerDemo;

public class JLambdaDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("["+ Misc.timestamp()+"] begin");
        ThreadSchedulerDemo.mainAppLoop();

        testJavaLammbdaHotswap();
    }

    // p.s test() method body hotswap will work, but standard Java does not support hotswap of Anonymous classes (lambda expressions: code within the run({}) block)
    public static void testJavaLammbdaHotswap() {
        ThreadSchedulerDemo.run(s -> {
            s.printf("start script");
            int i = 0;
           // s.wait(5);
            s.printf("well "+i++);
            s.wait(5);
            s.printf("well "+i++);
            s.wait(5);
            s.printf("well "+i++);
            s.wait(5);
            s.printf("well "+i++);
            s.wait(5);
            s.printf("well "+i++);
            s.wait(5);
            s.printf("well "+i++);
        });
    }
}
