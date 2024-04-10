/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.evolution.executors


/**
 * Defines an interface for a constructor executor within the Keen framework.
 *
 * The `ConstructorExecutor` is a specialized type of executor used for constructing objects
 * or elements, typically in the context of initializing genetic populations in genetic algorithms.
 * It extends the `KeenExecutor` interface, emphasizing its role as a task-specific executor.
 *
 * ## Functionality:
 * - The primary function is the `invoke` operator, which takes a size and an initialization
 *   lambda function. It creates a list of objects of type [T], each initialized using the provided
 *   lambda function.
 *
 * ## Factory Class:
 * The nested `Factory` class is a specialized factory for creating instances of `ConstructorExecutor`.
 * It extends `KeenExecutor.Factory` and provides a default implementation using `SequentialConstructor`.
 *
 * ### Usage:
 * The `Factory` class is used to create instances of `ConstructorExecutor` without needing to specify
 * the details of the constructor logic. It allows for easy integration and instantiation within the
 * broader genetic algorithm framework.
 *
 * ### Example:
 * Implementing a custom constructor executor:
 * ```kotlin
 * class MyCustomConstructor<T> : ConstructorExecutor<T> {
 *     override fun invoke(size: Int, init: () -> T): List<T> {
 *         return List(size) { init() }
 *     }
 * }
 *
 * // Using the factory to create an instance of MyCustomConstructor
 * class MyCustomConstructorFactory<T> : ConstructorExecutor.Factory<T>() {
 *     override var creator: (Unit) -> ConstructorExecutor<T> = { MyCustomConstructor() }
 * }
 * val constructorExecutor = MyCustomConstructorFactory<Int>().creator(Unit)
 * ```
 *
 * In this example, `MyCustomConstructor` provides a custom implementation of the `invoke` method,
 * creating a list of elements of type [T]. The `MyCustomConstructorFactory` is a factory for
 * instantiating `MyCustomConstructor` instances.
 *
 * @param T The type of elements to be constructed by the executor.
 * @see KeenExecutor for the base executor interface.
 * @version 2.0.0
 * @since 2.0.0
 */
interface ConstructorExecutor<T> : KeenExecutor {

    /**
     * Operator function to create a list of objects of type [T].
     *
     * @param size The number of objects to create.
     * @param init A lambda function that provides the logic for initializing each object. It takes an index as a
     *   parameter and returns an object of type [T].
     * @return A list containing [size] number of objects of type [T], each initialized using [init].
     */
    operator fun invoke(size: Int, init: (index: Int) -> T): List<T>

    /**
     * A factory class for creating instances of [ConstructorExecutor].
     *
     * This class provides a default implementation of the constructor executor, which can be
     * overridden with custom logic as needed.
     *
     * @param T The type of elements to be constructed by the executor.
     */
    open class Factory<T> : KeenExecutor.Factory<Unit, ConstructorExecutor<T>> {
        override var creator: (Unit) -> ConstructorExecutor<T> = { SequentialConstructor() }
    }
}
