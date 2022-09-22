public class JavaLambdaDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("[App "+Misc.timestamp()+"] begin");
        testJavaLammbdaHotswap();
        KotlinLambdasDemo.INSTANCE.test();// kick off a kotlin written task
        ThreadSchedulerDemo.mainAppLoop();
    }

    // p.s test() method body hotswap will work, but standard Java does not support hotswap of Anonymous classes (lambda expressions: code within the run({}) block)
    public static void testJavaLammbdaHotswap() {
        ThreadSchedulerDemo.run(s -> {
            System.out.println("\t[Task "+Misc.timestamp()+"] start script");
            int i = 5;
            while (i-- > 0) {
                s.wait(2); // expect this to park
                s.printf("inside a while loop %s", i);
            }
            testJavaLammbdaHotswap();
        });
    }
}
