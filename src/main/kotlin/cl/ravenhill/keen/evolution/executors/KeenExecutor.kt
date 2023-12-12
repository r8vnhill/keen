/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.executors

/**
 * An interface that defines a contract for an executor that can be configured with a
 * [Factory] instance.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface KeenExecutor {

    /**
     * A factory for creating [KeenExecutor] instances.
     *
     * @param I The input type of the executor.
     * @param R The type of the executor.
     * @property creator A lambda that takes an input and returns a [KeenExecutor] instance.
     */
    interface Factory<I, R : KeenExecutor> {
        var creator: (I) -> R
    }
}
