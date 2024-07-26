/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.evolution.executors


/**
 * Interface representing a general executor in the Keen evolutionary computation framework.
 *
 * The `KeenExecutor` interface defines the basic structure for executors used in the Keen framework. Executors are
 * responsible for performing specific tasks or processes within an evolutionary algorithm.
 */
interface KeenExecutor {

    /**
     * Factory interface for creating instances of executors.
     *
     * The `Factory` interface provides a mechanism for creating instances of executors in the Keen framework.
     * It allows for the configuration of a creator function that produces the required executor instances.
     *
     * ## Usage:
     * This interface is typically used to define factories for different types of executors within the Keen framework.
     * By implementing this interface, users can specify how executors should be created and configured.
     *
     * ### Example 1: Creating a Simple Executor Factory
     * ```kotlin
     * class SimpleExecutor : KeenExecutor {
     *     // Implementation of the executor
     * }
     *
     * val simpleExecutorFactory = object : KeenExecutor.Factory<Unit, SimpleExecutor> {
     *     override var creator: (Unit) -> SimpleExecutor = { SimpleExecutor() }
     * }
     *
     * val executor = simpleExecutorFactory.creator(Unit)
     * ```
     *
     * ### Example 2: Creating an Executor with Parameters
     * ```kotlin
     * class ParameterizedExecutor(val parameter: Int) : KeenExecutor {
     *     // Implementation of the executor
     * }
     *
     * val parameterizedExecutorFactory = object : KeenExecutor.Factory<Int, ParameterizedExecutor> {
     *     override var creator: (Int) -> ParameterizedExecutor = { param -> ParameterizedExecutor(param) }
     * }
     *
     * val executor = parameterizedExecutorFactory.creator(420)
     * ```
     *
     * @param I The input type used by the creator function to produce executor instances.
     * @param R The type of the executor, which must extend [KeenExecutor].
     */
    interface Factory<I, R> where R : KeenExecutor {

        /**
         * The creator function responsible for producing executor instances.
         *
         * This function takes an input of type [I] and returns an instance of type [R], which extends [KeenExecutor].
         */
        var creator: (I) -> R
    }
}
