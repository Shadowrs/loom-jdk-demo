
import ThreadSchedulerDemo.Script

fun main() {
    System.out.println("[App " + Misc.timestamp() + "] begin")
    KotlinLambdasDemo3.test() // kick off a kotlin written task
    ThreadSchedulerDemo.mainAppLoop()
}

/**
 * @author Jak Shadowrs (tardisfan121@gmail.com)
 */
object KotlinLambdasDemo3 {

    // p.s test() method body hotswap will work, but standard Java does not support hotswap of Anonymous classes (lambda expressions: code within the run({}) block)
    fun test() {
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