/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.repr

import cl.ravenhill.keen.mixins.Verifiable

/**
 * Represents a fundamental component in an evolutionary algorithm.
 *
 * The `Feature` interface defines the basic structure and behavior of a feature, which is an atomic unit within an
 * evolutionary algorithm. A feature typically represents a single element in a genetic structure, such as a gene in a
 * chromosome. This interface provides core functionality for duplicating a feature with a new value and binding
 * transformations to the feature's value.
 *
 * ## Usage:
 * Implement this interface to create concrete classes that represent specific types of features in evolutionary
 * algorithms. The `bind` method facilitates chaining operations on the feature's value, enabling complex
 * transformations and computations.
 *
 * ### Example 1: Implementing a Simple Gene Feature
 * ```kotlin
 * data class IntGene(override val value: Int) : Feature<Int, IntGene> {
 *     override fun copyWithValue(value: Int) = IntGene(value)
 * }
 * ```
 *
 * ### Example 2: Binding a Transformation
 * ```kotlin
 * val gene = IntGene(10)
 * val transformedGene = gene.bind { IntGene(it * 2) }
 * println(transformedGene.value) // Output: 20
 * ```
 *
 * ## Recommendation for Functional Programmers:
 * If you are familiar with functional programming and the concept of monads, you can implement subclasses of `Feature`
 * as monads. This involves ensuring that your subclass respects the monad laws (left identity, right identity, and
 * associativity) and providing a `pure` (or `unit`) function to wrap values into your monadic structure.
 *
 * ### How to Implement Subclasses as Monads:
 * 1. **Define a `pure` Function**: This function should take a raw value of type `T` and return an instance of your
 *    subclass. This allows you to wrap values in the monadic context.
 *    ```kotlin
 *    companion object {
 *        fun pure(value: Int): IntGene = IntGene(value)
 *    }
 *    ```
 *
 * 2. **Respect the Monad Laws**: Ensure that your subclass's implementation of `bind` respects the monad laws:
 *    - **Left Identity**: `pure(a).flatMap(f)` should be equivalent to `f(a)`.
 *    - **Right Identity**: `m.flatMap(::pure)` should be equivalent to `m`.
 *    - **Associativity**: `(m.flatMap(f)).flatMap(g)` should be equivalent to `m.flatMap { x -> f(x).flatMap(g) }`.
 *
 * ### Benefits of Implementing Subclasses as Monads:
 * - **Composability**: Monads allow you to chain operations in a clean and consistent way, enabling the composition
 *    of complex behaviors from simple functions.
 * - **Error Handling**: Monads provide a structured way to handle errors, missing values, or other computational
 *    contexts (e.g., `Option`, `Either`).
 * - **Consistency**: By adhering to the monad laws, you ensure that your code behaves predictably and consistently,
 *    making it easier to reason about.
 *
 * ## Recommendation to Use Data Classes for Subclasses:
 * It is recommended to implement subclasses of `Feature` as data classes. Kotlin data classes provide several benefits
 * that align well with the goals of functional programming and evolutionary algorithms:
 *
 * 1. **Automatic Implementations**: Data classes automatically provide implementations for `equals()`, `hashCode()`,
 *    `toString()`, and `copy()`. This reduces boilerplate and ensures that your feature instances are easily comparable
 *    and printable.
 *
 * 2. **Immutability**: Data classes are typically used with immutable properties, which is a key principle in
 *    functional programming. Immutability ensures that once a feature is created, it cannot be modified, leading to
 *    more predictable and safer code.
 *
 * 3. **Ease of Use**: The `copy()` method provided by data classes makes it easy to create new instances with modified
 *    values, which is particularly useful when implementing the `copyWithValue` method.
 *
 * ### Example of a Data Class Implementation:
 * ```kotlin
 * data class IntGene(override val value: Int) : Feature<Int, IntGene> {
 *     override fun copyWithValue(value: Int) = copy(value = value)
 *
 *     companion object {
 *         fun pure(value: Int): IntGene = IntGene(value)
 *     }
 * }
 * ```
 *
 * ### Benefits of Using Data Classes:
 * - **Reduced Boilerplate**: Data classes eliminate much of the repetitive code required for implementing common
 *   functions, making your code cleaner and more maintainable.
 * - **Immutability and Safety**: By leveraging immutability, data classes help you avoid common bugs related to
 *   unintended mutations, leading to safer and more predictable code.
 * - **Convenient Copying**: The `copy()` method simplifies the creation of modified instances, which is a frequent
 *   requirement in evolutionary algorithms.
 *
 * @param T The type of the value held by the feature.
 * @param F The type of the feature itself, which must extend [Feature].
 * @property value The value held by the feature, representing its state or characteristic in the evolutionary process.
 */
interface Feature<T, F> : Verifiable where F : Feature<T, F> {

    /**
     * The value held by the feature.
     *
     * This property represents the specific data or characteristic associated with the feature in the evolutionary
     * process. The value is typically used in genetic operations or to determine the fitness of an individual.
     */
    val value: T

    /**
     * Creates a duplicate of the feature with a new specified value.
     *
     * This method returns a new instance of the feature, identical in every way except for the new value. It is
     * useful in genetic algorithms where a new feature needs to be generated based on an existing one, but with
     * a modified value.
     *
     * @param value The new value for the feature.
     * @return A new feature instance with the specified value.
     */
    fun copyWithValue(value: T): F

    /**
     * Applies a function to the feature's value and returns a new feature instance with the transformed value.
     *
     * The `flatMap` method allows for chaining operations that transform the feature's value and return a new feature.
     * This method is a key part of the monadic structure, enabling complex transformations while maintaining the
     * integrity of the feature's structure.
     *
     * @param f The function to apply to the feature's value.
     * @return A new feature instance with the transformed value.
     */
    fun flatMap(f: (T) -> F): F = f(value)

    /**
     * Converts the feature's value into a list representation.
     *
     * The `toList` method returns a list containing the feature's value. This is useful in contexts where features
     * need to be processed or analyzed in bulk, particularly in the context of genetic operations.
     *
     * @return A list containing the feature's value.
     */
    fun toList(): List<T> = listOf(value)
}
