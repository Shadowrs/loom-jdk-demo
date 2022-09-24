package larx.jdemos;

import larx.Misc;
import larx.engine.ThreadSchedulerDemo;

public class JLoopDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("["+ Misc.timestamp()+"] begin");
        ThreadSchedulerDemo.mainAppLoop();

        testJavaLammbdaHotswap();
    }

    // p.s test() method body hotswap will work, but standard Java does not support hotswap of Anonymous classes (lambda expressions: code within the run({}) block)
    public static void testJavaLammbdaHotswap() {
        ThreadSchedulerDemo.run(s -> {
            System.out.println("\t["+ Misc.timestamp()+"] start script");
            int i = 30;
            while (i-- > 0) {
                s.wait(1); // expect this to park
                s.printf("A inside a while loop %s", i);
            }
            //testJavaLammbdaHotswap();
        });
    }
}
