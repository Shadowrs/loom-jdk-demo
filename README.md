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
