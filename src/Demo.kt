import ThreadSchedulerDemo.Script

/**
 * @author Jak Shadowrs (tardisfan121@gmail.com)
 */
object Demo {

    // p.s test() method body hotswap will work, but standard Java does not support hotswap of Anonymous classes (lambda expressions: code within the run({}) block)
    fun test() {

        ThreadSchedulerDemo.run {
            it.printf("kotlin demo 1 start")
            it.wait(2)
            it.printf("wow lol")
        }

        onInteraction {
            printf("kotlin demo 2 start")
            wait(1)
            printf("mid 1")
            wait(1)
            printf("mid 2")
            wait(1)
            printf("mid 3")
            wait(1)
        }

        onInteraction {
            printf("kotlin demo 3 start")
            var i = 10
            wait(1)
            while (i-- > 0) {
                wait(1)
                printf("demo 3- part $i")
            }
        }
    }

    // method with context receiver so you can be even more lazy
    fun onInteraction(task: Script.() -> Unit) {
        ThreadSchedulerDemo.run(task)
    }
}