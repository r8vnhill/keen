/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.evolution.executors


/**
 * Defines an interface for executors in the Keen framework.
 *
 * This interface is part of the Keen genetic algorithm framework and is used to define a standard
 * for executors. Executors are objects responsible for executing specific tasks or operations, such
 * as running a genetic algorithm, evaluating fitness, performing selections, etc.
 *
 * ## Factory Interface:
 * The `Factory` interface nested within `KeenExecutor` is a generic factory pattern for creating
 * executor instances. It provides flexibility in creating various types of executors tailored to
 * different needs and inputs.
 *
 * ### Usage:
 * The `Factory` interface is particularly useful in scenarios where executors need to be dynamically
 * created based on different input parameters or configurations. By defining a `creator` function,
 * users can specify how an executor should be instantiated.
 *
 * ### Example:
 * Implementing a factory for a custom executor:
 * ```kotlin
 * class MyCustomExecutor : KeenExecutor {
 *     // Executor implementation
 * }
 *
 * class MyCustomExecutorFactory : KeenExecutor.Factory<MyInputType, MyCustomExecutor> {
 *     override var creator: (MyInputType) -> MyCustomExecutor = { input ->
 *         // Logic for creating MyCustomExecutor with the given input
 *     }
 * }
 *
 * // Creating an executor instance
 * val factory = MyCustomExecutorFactory()
 * val executor = factory.creator(myInput)
 * ```
 *
 * In this example, `MyCustomExecutor` implements the `KeenExecutor` interface. The factory,
 * `MyCustomExecutorFactory`, specifies how to create instances of `MyCustomExecutor` based on
 * a specific input type, `MyInputType`.
 *
 * @author [Ignacio Slater M.](https://www.github.com/r8vnhill)
 * @version 2.0.0
 * @since 2.0.0
 */
interface KeenExecutor {

    /**
     * A factory interface for creating instances of [KeenExecutor].
     *
     * This interface allows for defining a custom method to create executors. It is particularly
     * useful in scenarios where executors need to be instantiated with specific configurations or
     * parameters.
     *
     * @param I The type of the input used to create the executor.
     * @param R The type of the executor created by the factory.
     * @property creator A lambda function that takes an input of type [I] and produces an executor
     *   of type [R]. This function defines how the executor is created based on the provided input.
     */
    interface Factory<I, R> where R : KeenExecutor {
        var creator: (I) -> R
    }
}
