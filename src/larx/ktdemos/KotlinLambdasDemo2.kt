package larx.ktdemos
import larx.engine.ThreadSchedulerDemo
import larx.engine.ThreadSchedulerDemo.Script
import larx.Misc
import kotlin.system.exitProcess

fun main() {
    System.out.println("[App " + Misc.timestamp() + "] begin")
    KotlinLambdasDemo2.test() // kick off a kotlin written task
    ThreadSchedulerDemo.mainAppLoop()
}

/**
 * @author Jak Shadowrs (tardisfan121@gmail.com)
 */
object KotlinLambdasDemo2 {

    // p.s test() method body hotswap will work, but standard Java does not support hotswap of Anonymous classes (lambda expressions: code within the run({}) block)
    fun test() {

        onInteraction {
            printf("kotlin demo 2 start")
            wait(1)
            printf("mid 1")
            wait(1)
            printf("mid 2")
            wait(1)
            printf("mid 3")
            wait(1)
            printf("mid 4")
            wait(1)
            printf("mid 5")
            wait(1)
            printf("mid 6")
            wait(1)
            printf("mid 7")
            wait(1)
            printf("mid 8")
            wait(1)
            exitProcess(0)
        }

    }

    // method with context receiver so you can be even more lazy
    fun onInteraction(task: Script.() -> Unit) {
        ThreadSchedulerDemo.run(task)
    }
}