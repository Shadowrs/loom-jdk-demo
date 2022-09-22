# Demo of Project Loom & Continuations on the JDK

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


#### DCEVM & Loom
The default hotswap capability in Java is limited. 

The [DCEVM](https://dcevm.github.io/) Project "allows unlimited redefinition of loaded classes at runtime" - including hotswapping anonymous classes, add/removing/modifying fields, annotations, member classes and enums. 

When a project makes large use of Lambdas - the most important part is the ability to hotswap code anonymous classes, which is what a lambda is.

Virtual Threads were added in JDK-19 as a preview feature (requires --enable-preview VM args) as part of Loom. 

DCEVM, as of September 2022, is only available on openJDK 7-11, and as of march 2022 Jetbrains hired one of the DCEVM project developers and has contributed to adding DCEVM into [Jetbrain's Runtime JDK 17](https://github.com/JetBrains/JetBrainsRuntime/releases) which is awesome. 

Since the goal is to get DCEVM and Virtual Threads together, I exchanged emails and inquired about Jetbrain's intentions of releasing their Runtime with DCEVM on-top of JDK 19 or later. They are discussing it but no decisions have been made! After all, Jetbrains runtimes have only been released based on LTS-jdks (long term support) which is jdk 11 & 17. The next LTS release is [Jdk-21 due September 2023](https://www.java.com/releases/). 
