package cl.ravenhill.keen.util.concurrent

import java.util.concurrent.CancellationException
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executor
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.ForkJoinTask
import java.util.concurrent.Future

/**
 *
 */
sealed interface Concurrency : Executor, AutoCloseable {
    fun execute(runnables: List<Runnable>)

    class ForkJoinPoolConcurrency(private val executor: ForkJoinPool) : Concurrency {
        private val tasks = mutableListOf<ForkJoinTask<*>>()

        override fun execute(runnables: List<Runnable>) {
            if (runnables.isNotEmpty()) {
                tasks.add(executor.submit(RunnablesAction(runnables)))
            }
        }

        override fun execute(command: Runnable) {
            TODO("Not yet implemented")
        }

        override fun close() {
            Concurrency.join(tasks)
        }

    }

    companion object {
        fun with(executor: Executor) = when (executor) {
            is ForkJoinPool -> ForkJoinPoolConcurrency(executor)
            else -> TODO()
        }

        fun join(jobs: Iterable<Future<*>>) {
            lateinit var task: Future<*>
            lateinit var tasks: Iterator<Future<*>>
            try {
                tasks = jobs.iterator()
                while (tasks.hasNext()) {
                    task = tasks.next()
                    task.get()
                }
            } catch (e: ExecutionException) {
                throw e
            } catch (e: CancellationException) {
                TODO()
            } catch (e: InterruptedException) {
                TODO()
            }
        }
    }
}