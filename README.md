# Demo of Loom on the JDK

```
[App 12:20] begin
	[Task 12:20] start script
	[Task 12:20] VirtualThread[#25]/runnable@ForkJoinPool-1-worker-1 waiting 2 cycles until continue
[Scheduler 12:20] loop 0 on main thread java.lang.Thread
[Scheduler 12:21] loop 1 on main thread java.lang.Thread
[Scheduler 12:22] loop 2 on main thread java.lang.Thread
[Scheduler 12:22] >> resuming task with thread: VirtualThread[#25]/waiting
	[Task 12:22] hi!
	[Task 12:22] VirtualThread[#25]/runnable@ForkJoinPool-1-worker-1 done!
[Scheduler 12:23] loop 3 on main thread java.lang.Thread
```

### Hotswap support

In standard Java, hotswap is limited to in-body code changes. 

Since this Demo design uses Anonymous Classes (in the form of Lambda expressions), hotswapped changes only run when a method is called again, rather than taking effect during the for-loop below:
```
private static void test1() {
    run(s -> {
        for (int i=0; i<10; i)) {
            s.printf("hi "+i);
             // change this text and hotswap
             s.delay(1);
        }
        
        test1();
         // new code appears when test1 
         // is called, after the first 10
         // loops complete
    });
}
```


#### DCEVM & Hotswap Agent Support
DCEVM provides support for hotswapping anonymous classes, add/removing/modifying fields, annotations, member classes and enums. 

Since Loom is in Early Access built on JDK-19, we can't get support for DCEVM+HotswapAgent only avaiable on [Jdks 8, 11 and 17](http://hotswapagent.org/mydoc_quickstart-jdk17.html). 

Jetbrains runtimes have only been released based on LTS-jdks (long term support) which is jdk 11 & 17. The next LTS release is [Jdk-21 due September 2023](https://www.java.com/releases/). Here is hoping Loom is delivered by then!