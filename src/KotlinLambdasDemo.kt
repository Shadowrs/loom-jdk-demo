
import ThreadSchedulerDemo.Script

fun main() {
    System.out.println("[App " + Misc.timestamp() + "] begin")
    KotlinLambdasDemo.test() // kick off a kotlin written task
    ThreadSchedulerDemo.mainAppLoop()
}

/**
 * @author Jak Shadowrs (tardisfan121@gmail.com)
 */
object KotlinLambdasDemo {

    // p.s test() method body hotswap will work, but standard Java does not support hotswap of Anonymous classes (lambda expressions: code within the run({}) block)
    fun test() {

        ThreadSchedulerDemo.run {
            it.printf("kotlin demo 1 start")
            it.wait(2)
            it.printf("wow lol")
        }
    }

    // method with context receiver so you can be even more lazy
    fun onInteraction(task: Script.() -> Unit) {
        ThreadSchedulerDemo.run(task)
    }
}