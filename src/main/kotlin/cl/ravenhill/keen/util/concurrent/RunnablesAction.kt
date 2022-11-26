package cl.ravenhill.keen.util.concurrent

import java.util.concurrent.RecursiveAction


class RunnablesAction(
    private val runnables: List<Runnable>,
    private val low: Int,
    private val high: Int
) : RecursiveAction() {

    constructor(runnables: List<Runnable>) : this(runnables, 0, runnables.size)

    override fun compute() {
        if ((high - low) <= 5 || getSurplusQueuedTaskCount() > 3) {
            runnables.forEach { it.run() }
        } else {
            val mid =(low + high) ushr 1
            invokeAll(
                RunnablesAction(runnables, low, mid),
                RunnablesAction(runnables, mid, high)
            )
        }
    }
}