package cl.ravenhill.keen.genetics.genes

import kotlin.random.Random

/**
 * A [Gene] implementation for holding a [Double] value, representing a continuous gene in genetic algorithms.
 *
 * This class implements the **monad pattern** by providing a `pure` function, which allows lifting a value into the
 * monadic context of a `DoubleGene`. It supports transformations of values while retaining the gene's context.
 *
 * ## Usage:
 * This class can be used to represent genes with continuous values, often useful in optimization and search problems
 * within evolutionary computation. The `pure` function creates a new `DoubleGene` with the specified value, enabling
 * functional composition and transformation within the monadic context.
 *
 * ### Example 1: Creating a `DoubleGene` with a specific value
 * ```kotlin
 * val gene = DoubleGene.pure(0.5) // Creates a DoubleGene with a value of 0.5
 * ```
 *
 * ### Example 2: Using `flatMap` to chain transformations
 *
 * ```kotlin
 * val initialGene = DoubleGene.pure(0.5)
 * val transformedGene = initialGene.flatMap { value ->
 *     DoubleGene.pure(value * 2) // Doubles the gene's value
 * }
 * println(transformedGene.value) // Output: 1.0
 * ```
 *
 * @param value The [Double] value held by this gene.
 *
 * @property value The numeric value that this gene represents.
 * @property generator A function that generates random [Double] values, useful for initialization and mutation.
 */
data class DoubleGene(override val value: Double) : Gene<Double, DoubleGene> {

    /**
     * Creates a copy of this gene with a new specified [value].
     *
     * @param value The new [Double] value for the copied gene.
     * @return A new instance of `DoubleGene` with the updated value.
     */
    override fun copyWithValue(value: Double) = pure(value)

    /**
     * A generator function that produces random [Double] values.
     *
     * This generator is used to initialize genes with random values, supporting evolutionary operations that require
     * stochastic variation.
     */
    override val generator: (Random) -> Double = { r -> r.nextDouble() }

    companion object {
        /**
         * Lifts a [Double] value into the `DoubleGene` monadic context.
         *
         * @param value The initial value for the new `DoubleGene`.
         * @return A new `DoubleGene` instance with the specified value.
         */
        fun pure(value: Double): DoubleGene = DoubleGene(value)
    }
}
