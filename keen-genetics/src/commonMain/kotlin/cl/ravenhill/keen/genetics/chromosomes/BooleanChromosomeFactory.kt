/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetics.chromosomes

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constrained
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.constraints.BeDefined
import cl.ravenhill.keen.exceptions.InvalidSizeException
import cl.ravenhill.keen.genetics.genes.BooleanGene
import kotlin.random.Random
import cl.ravenhill.keen.evolution.executors.construction.ConstructorExecutor
import cl.ravenhill.keen.exceptions.InitializationException

/**
 * Factory class for creating [BooleanChromosome] instances in an evolutionary algorithm.
 *
 * The `BooleanChromosomeFactory` class is a concrete implementation of the [AbstractChromosomeFactory] designed for
 * generating chromosomes composed of boolean genes ([BooleanGene]). This factory leverages a [ConstructorExecutor] to
 * create the gene sequence and supports asynchronous, non-blocking chromosome generation. Additionally, the factory
 * allows customization of the probability that a gene will be `True` through the [trueRate] property.
 *
 * ## Usage:
 * This class is intended for use within evolutionary algorithms that require boolean chromosomes. The factory generates
 * a chromosome of a specified size, where each gene is randomly set to either `True` or `False` based on the provided
 * `Random` instance and the configured `trueRate`. The size of the chromosome must be a positive integer, and this
 * constraint is strictly enforced by the factory.
 *
 * ### Example: Creating a Boolean Chromosome
 * ```kotlin
 * val factory = BooleanChromosomeFactory().apply {
 *     size = 10
 *     trueRate = 0.7
 * }
 * val result = factory.invoke(Random())
 * result.onSuccess { chromosome ->
 *     println("Generated chromosome: $chromosome")
 * }.onFailure { exception ->
 *     println("Failed to generate chromosome: ${exception.message}")
 * }
 * ```
 *
 * @constructor Initializes a new instance of `BooleanChromosomeFactory` with the default settings from the
 * `AbstractChromosomeFactory`.
 * @property trueRate The probability that a gene in the chromosome will be `True`. Defaults to 0.5.
 */
class BooleanChromosomeFactory : AbstractChromosomeFactory<Boolean, BooleanGene>() {

    /**
     * The probability that a gene in the chromosome will be `True`.
     *
     * This property allows customization of the ratio between `True` and `False` genes within the generated chromosome.
     * By adjusting the `trueRate`, you can control how likely it is for a gene to be set to `True` during the chromosome
     * generation process. The value must be between 0.0 and 1.0, inclusive.
     */
    var trueRate: Double = 0.5

    /**
     * Asynchronously creates a `BooleanChromosome` of the specified size.
     *
     * This `invoke` function is the primary method for generating a `BooleanChromosome`. It uses the provided `Random`
     * instance and the configured `trueRate` to determine the boolean value of each gene in the chromosome. The function
     * enforces that the size of the chromosome must be defined and positive, throwing an [InvalidSizeException] if these
     * constraints are violated.
     *
     * ## Constraints:
     * - **Size Must Be Defined**: The size property must be initialized before invoking this method. If the size is not
     *   defined, an [InvalidSizeException] will be thrown.
     * - **Size Must Be Positive**: The size of the chromosome must be greater than 0. If the size is less than 1, an
     *   [InvalidSizeException] will be thrown.
     *
     * @return A [Result] containing the generated `BooleanChromosome`, or an exception if the generation fails.
     */
    override suspend fun invoke(): Either<InitializationException, Chromosome<Boolean, BooleanGene>>  {
        constrained {
            "Size must be initialized; maybe you forgot to set the size property"(::InvalidSizeException) {
                size must BeDefined
            }
            "Cannot create a chromosome with a size less than 1"(::InvalidSizeException) {
                size must BePositive
            }
        }.getOrElse { it.left() }
        return BooleanChromosome(
            executor(size) { if (Domain.random.nextDouble() < trueRate) BooleanGene.True else BooleanGene.False }
        ).right()
    }
}
