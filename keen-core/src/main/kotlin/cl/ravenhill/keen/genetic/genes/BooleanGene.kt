/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.Domain


/**
 * A sealed interface representing a gene with a Boolean value in a gene-centric evolutionary algorithm.
 *
 * This interface defines a gene where the genetic value is a Boolean (either `true` or `false`).
 * It provides basic functionalities to generate random Boolean genes, duplicate genes with specific values, and convert
 * the Boolean value to different data types. The interface is sealed to restrict its implementations to the predefined
 * `True` and `False` objects, representing the respective Boolean values.
 *
 * ## Usage:
 * ```
 * val trueGene: BooleanGene = BooleanGene.True
 * val falseGene: BooleanGene = BooleanGene.False
 * val randomGene: BooleanGene = if (Domain.random.nextBoolean()) trueGene else falseGene
 * ```
 * In this example, `trueGene` and `falseGene` are instances of `BooleanGene` representing `true` and `false`
 * respectively.
 */
sealed interface BooleanGene : Gene<Boolean, BooleanGene> {

    /**
     * Generates a random Boolean gene value.
     *
     * This method overrides the `generator` function in the `Gene` interface specifically for `BooleanGene`.
     * It utilizes the [Domain.random] object to produce a random Boolean value, either `true` or `false`. This
     * function is essential in genetic algorithms for creating new gene instances or for mutation operations,
     * where a random value is required to introduce variability into the gene pool.
     *
     * ## Usage:
     * ```
     * val randomBooleanGeneValue = BooleanGene.generator()
     * // randomBooleanGeneValue will be either `true` or `false`, randomly determined
     * ```
     * In this usage example, `randomBooleanGeneValue` will hold a randomly generated Boolean value,
     * demonstrating how the `generator` function can be used to introduce randomness in genetic algorithms.
     *
     * @return A random Boolean value (`true` or `false`).
     */
    override fun generator() = Domain.random.nextBoolean()


    /**
     * Creates a new `BooleanGene` instance with a specific Boolean value.
     *
     * This method overrides the `duplicateWithValue` function from the `Gene` interface for `BooleanGene`.
     * It is used to create a new instance of `BooleanGene` with the given Boolean value, effectively duplicating
     * the gene while allowing for a change in its value. This is particularly useful in genetic algorithms during
     * crossover or mutation operations, where new gene instances with modified values are required.
     *
     * ## Usage:
     * ```
     * val gene = BooleanGene.True
     * val duplicatedGene = gene.duplicateWithValue(false)
     * // duplicatedGene is now an instance of BooleanGene.False
     * ```
     * In this example, `duplicatedGene` is created as a duplicate of `gene` but with a value of `false`.
     *
     * @param value The Boolean value (`true` or `false`) for the new `BooleanGene` instance.
     * @return A new `BooleanGene` instance with the specified Boolean value.
     */
    override fun duplicateWithValue(value: Boolean) = if (value) True else False

    /**
     * Returns the Boolean value of the gene.
     */
    fun toBoolean() = value

    /**
     * Returns 1 if the gene's value is `true`, otherwise 0.
     */
    fun toInt() = if (value) 1 else 0

    /**
     * Returns 1.0 if the gene's value is `true`, otherwise 0.0.
     */
    fun toDouble() = if (value) 1.0 else 0.0

    /**
     * Returns the logical negation of the current boolean value.
     *
     * @return The logical negation of the current value.
     */
    operator fun not() = if (value) False else True

    /**
     * Represents a `BooleanGene` instance with a fixed value of `true`.
     *
     * This data object is a concrete implementation of the `BooleanGene` interface, specifically representing
     * a gene whose value is permanently set to `true`. It is used in genetic algorithms where Boolean genes are
     * required, particularly in situations where a gene with a `true` value is needed. This object can be used directly
     * in gene pools, crossover operations, and mutation processes within a genetic algorithm.
     *
     * Being a data object, `True` is a singleton, ensuring that all references to `True` within an application are
     * references to the same object. This is beneficial for memory efficiency and performance.
     *
     * ## Example:
     * ```
     * val trueGene: BooleanGene = BooleanGene.True
     * // Use trueGene in a genetic algorithm context
     * ```
     * In this example, `trueGene` is an instance of `BooleanGene` representing a gene with a `true` value.
     *
     * @property value The value of the gene, which is always `true`.
     */
    data object True : BooleanGene {
        override val value = true
    }

    /**
     * Represents a `BooleanGene` instance with a fixed value of `false`.
     *
     * This data object is a specific implementation of the `BooleanGene` interface, representing a gene
     * whose value is constantly set to `false`. It is particularly useful in genetic algorithms that require
     * Boolean genes, especially in contexts where a gene with a `false` value is necessary. This object can
     * be effectively utilized in various genetic algorithm operations such as gene pool creation, crossover,
     * and mutation processes.
     *
     * As a data object, `False` serves as a singleton, ensuring that all uses of `False` within an application
     * point to the same instance. This approach contributes to memory efficiency and improved performance.
     *
     * ## Example:
     * ```
     * val falseGene: BooleanGene = BooleanGene.False
     * // Utilize falseGene in a genetic algorithm setting
     * ```
     * In this usage scenario, `falseGene` is an instance of `BooleanGene` representing a gene with a `false` value.
     *
     * @property value The value of the gene, which is always `false`.
     */
    data object False : BooleanGene {
        override val value = false
    }
}
