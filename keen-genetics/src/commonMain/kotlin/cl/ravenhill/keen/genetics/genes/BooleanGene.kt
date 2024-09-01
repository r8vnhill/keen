/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetics.genes

import kotlin.random.Random

/**
 * Represents a boolean gene in an evolutionary algorithm.
 *
 * The `BooleanGene` sealed interface models a gene that holds a boolean value, which can be either [True] or [False].
 * This interface extends the [Gene] interface, inheriting the [flatMap] function, which allows it to act as a monad. In
 * Kotlin, a monad is a construct that follows the monad laws, providing composable and chainable operations on the
 * contained value. Although Kotlin does not have native pattern matching, the `BooleanGene` provides an alternative
 * approach through its sealed structure and methods like [toInt].
 *
 * ## Usage:
 * Implementations of `BooleanGene` are used in genetic algorithms where binary decisions or states are needed. The
 * interface provides essential methods for value generation, duplication, and transformation. The sealed nature of
 * the interface ensures that the possible values are restricted to `True` and `False`.
 *
 * ### Example 1: Creating and Using Boolean Genes
 * ```kotlin
 * val gene = BooleanGene.pure(true)
 * val mutatedGene = gene.mutate()
 * println(mutatedGene.value) // Output: either `True` or `False`
 * println(mutatedGene.toInt()) // Output: 1 if `True`, 0 if `False`
 * ```
 *
 * ### Example 2: Converting Between Boolean and Integer Values
 * ```kotlin
 * val geneFromInt = BooleanGene.fromInt(1)
 * println(geneFromInt.value) // Output: `True`
 *
 * val geneFromBoolean = BooleanGene.from(false)
 * println(geneFromBoolean.value) // Output: `False`
 * ```
 *
 * ## Monadic Properties:
 * Since `BooleanGene` inherits the `bind` method from the `Gene` interface, it follows the monadic structure:
 * - **Pure Function**: The `pure` function allows you to wrap a raw boolean value into a `BooleanGene` instance,
 *   adhering to the monad's `unit` or `pure` concept.
 * - **Monad Laws**: The `BooleanGene` implementation respects the monad laws (left identity, right identity, and
 *   associativity), enabling predictable and composable operations on its value.
 *
 * ## Kotlin's Lack of Pattern Matching:
 * Kotlin does not support traditional pattern matching as seen in languages like Scala or Haskell. However, the
 * `BooleanGene` interface circumvents this limitation by using sealed classes and methods like `toInt()` to provide
 * similar functionality. The `if-else` structure within the [copyWithValue] method and the `True` and `False`
 * data objects act as alternatives to pattern matching.
 *
 * @property generator A function that generates new boolean values for the gene, typically used in mutation operations.
 */
sealed interface BooleanGene : Gene<Boolean, BooleanGene> {

    override val generator: (Random) -> Boolean
        get() = { it.nextBoolean() }

    /**
     * Duplicates the gene with a new specified boolean value.
     *
     * This method returns a new instance of the gene with the provided value. It uses an `if-else` structure to
     * determine whether to return [True] or [False], providing an alternative to pattern matching.
     *
     * @param value The new boolean value for the gene.
     * @return A new `BooleanGene` instance with the specified value.
     */
    override fun copyWithValue(value: Boolean): BooleanGene = if (value) True else False

    /**
     * Converts the boolean gene's value to an integer.
     *
     * This method returns `1` if the gene's value is [True] and `0` if it is [False]. It provides a convenient
     * way to work with the gene's value in contexts where integers are required.
     *
     * @return `1` if the gene's value is `True`, `0` if it is `False`.
     */
    fun toInt(): Int = if (value) 1 else 0

    /**
     * Represents the `True` state of the boolean gene.
     *
     * The `True` object holds the value `true` and is one of the two possible states of a [BooleanGene].
     */
    data object True : BooleanGene {
        override val value: Boolean = true
    }

    /**
     * Represents the `False` state of the boolean gene.
     *
     * The `False` object holds the value `false` and is one of the two possible states of a [BooleanGene].
     */
    data object False : BooleanGene {
        override val value: Boolean = false
    }

    companion object {

        /**
         * Wraps a boolean value into a [BooleanGene] instance.
         *
         * The `pure` function is used to create a `BooleanGene` from a raw boolean value. It ensures that the value
         * is correctly wrapped in the appropriate [True] or [False] object.
         *
         * @param value The boolean value to wrap.
         * @return A `BooleanGene` instance representing the provided value.
         */
        fun pure(value: Boolean): BooleanGene = if (value) True else False

        /**
         * Converts a boolean value into a `BooleanGene`.
         *
         * This function is an alias for `pure` and serves as a more descriptive method name for creating `BooleanGene`
         * instances from boolean values.
         *
         * @param value The boolean value to convert.
         * @return A `BooleanGene` instance representing the provided value.
         */
        fun from(value: Boolean): BooleanGene = pure(value)

        /**
         * Converts an integer value into a `BooleanGene`.
         *
         * This function converts an integer (where `1` represents `True` and `0` represents `False`) into a
         * `BooleanGene` instance. It is useful for working with external data sources that represent booleans as
         * integers.
         *
         * @param value The integer value to convert.
         * @return A `BooleanGene` instance representing the boolean equivalent of the provided integer.
         */
        fun fromInt(value: Int): BooleanGene = pure(value == 1)
    }
}
