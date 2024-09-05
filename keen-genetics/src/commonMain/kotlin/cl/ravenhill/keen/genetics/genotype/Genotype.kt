/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetics.genotype

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cl.ravenhill.jakt.constrained
import cl.ravenhill.jakt.constrainedTo
import cl.ravenhill.jakt.constraints.ints.BeInRange
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ToStringMode
import cl.ravenhill.keen.exceptions.InvalidIndexException
import cl.ravenhill.keen.genetics.chromosomes.Chromosome
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.mixins.Foldable
import cl.ravenhill.keen.repr.Representation

/**
 * Represents a genotype in the evolutionary computation framework.
 *
 * The `Genotype` class encapsulates a collection of chromosomes, providing functionalities to access and manipulate
 * the genetic information of an individual. It implements the [Representation] and [Collection] interfaces, allowing
 * it to be used in various evolutionary operations.
 *
 * ## Usage:
 * The `Genotype` class is typically used in evolutionary algorithms to represent the structure and genetic composition
 * of individuals in a population.
 *
 * ### Example 1: Creating a Genotype with a List of Chromosomes
 * ```kotlin
 * val chromosome1 = MyChromosome(MyGene(1), MyGene(2), MyGene(3))
 * val chromosome2 = MyChromosome(MyGene(4), MyGene(5), MyGene(6))
 * val genotype = Genotype(listOf(chromosome1, chromosome2))
 * ```
 *
 * ### Example 2: Creating a Genotype with Vararg Chromosomes
 * ```kotlin
 * val chromosome1 = MyChromosome(listOf(MyGene(1), MyGene(2), MyGene(3)))
 * val chromosome2 = MyChromosome(listOf(MyGene(4), MyGene(5), MyGene(6)))
 * val genotype = Genotype(chromosome1, chromosome2)
 * ```
 *
 * @param T The type of the value held by the genes.
 * @param G The type of the gene, which must extend [Gene].
 * @property chromosomes The list of chromosomes that make up the genotype.
 * @property size The number of chromosomes in the genotype.
 * @constructor Creates a `Genotype` instance with the specified list of chromosomes.
 */
data class Genotype<T, G>(override val chromosomes: List<Chromosome<T, G>>) :
    Representation<T, G>, ContainOps<T, G>, Collection<Chromosome<T, G>>, Foldable<T> by FoldOps(chromosomes)
        where G : Gene<T, G> {

    override val size = chromosomes.size

    /**
     * Secondary constructor for creating a `Genotype` instance using a vararg of chromosomes.
     *
     * @param chromosomes The chromosomes that make up the genotype.
     */
    constructor(vararg chromosomes: Chromosome<T, G>) : this(chromosomes.toList())

    /**
     * Flattens the genotype by collecting the value of all genes in all chromosomes.
     *
     * @return A list of all the values in the genotype.
     */
    override fun flatten(): List<T> = chromosomes.flatMap { it.flatten() }

    /**
     * Applies a transformation function to each gene in the genotype, producing a new genotype with the transformed
     * genes.
     *
     * The `map` function transforms the values of all genes across all chromosomes in the genotype using the specified
     * transformation function [transform]. A new `Genotype` instance is returned, with each gene in each chromosome
     * replaced by the result of applying the transformation function to the original gene's value. This is a
     * non-mutating operation that produces a new genotype, leaving the original genotype unchanged.
     *
     * @param transform The transformation function to apply to each gene's value.
     * @return A new `Genotype` instance where each gene's value has been transformed by the [transform] function.
     */
    override fun map(transform: (T) -> T) = Genotype(chromosomes.map { it.map(transform) })

    /**
     * Removes the first [n] chromosomes from the genotype, returning a new genotype with the remaining chromosomes.
     *
     * The `drop` method removes a specified number of chromosomes from the beginning of the genotype. It validates that
     * the number of chromosomes to drop is within the valid range (0 to the size of the genotype). If valid, a new
     * genotype is returned; otherwise, an error is returned.
     *
     * @param n The number of chromosomes to remove from the beginning.
     * @return An [Either] containing a new `Genotype` with the first [n] chromosomes removed, or a [CompositeException]
     * if the number of chromosomes to drop is out of range.
     */
    override fun drop(n: Int): Either<CompositeException, Genotype<T, G>> =
        validateAndApply(
            n,
            List<Chromosome<T, G>>::drop,
            "The number of chromosomes to drop ($n) must be in the range [0, $size]"
        )

    /**
     * Retains the first [n] chromosomes from the genotype, returning a new genotype with only the first [n]
     * chromosomes.
     *
     * The `take` method retains a specified number of chromosomes from the beginning of the genotype. It validates that
     * the number of chromosomes to take is within the valid range (0 to the size of the genotype). If valid, a new
     * genotype is returned; otherwise, an error is returned.
     *
     * @param n The number of chromosomes to retain from the beginning.
     * @return An [Either] containing a new `Genotype` with the first [n] chromosomes, or a [CompositeException]
     * if the number of chromosomes to take is out of range.
     */
    override fun take(n: Int): Either<CompositeException, Genotype<T, G>> =
        validateAndApply(
            n,
            List<Chromosome<T, G>>::take,
            "The number of chromosomes to take ($n) must be in the range [0, $size]"
        )

    /**
     * Validates the input value [n] and applies the specified [operation] to the list of chromosomes.
     *
     * @param n The number of elements to drop or take.
     * @param operation The operation to apply to the list of chromosomes (e.g., `drop` or `take`).
     * @param errorMessage The error message to display if the validation fails.
     * @return An [Either] containing a new `Genotype` after applying the operation, or a [CompositeException] if the
     *   input is out of range.
     */
    private fun validateAndApply(
        n: Int,
        operation: (List<Chromosome<T, G>>, Int) -> List<Chromosome<T, G>>,
        errorMessage: String
    ): Either<CompositeException, Genotype<T, G>> {
        constrainedTo {
            errorMessage(::InvalidIndexException) {
                n must BeInRange(0..size)
            }
        }.onLeft { return it.left() }
        return Genotype(operation(chromosomes, n)).right()
    }

    /**
     * Checks if the genotype is empty (i.e., contains no chromosomes).
     *
     * @return `true` if the genotype is empty, `false` otherwise.
     */
    override fun isEmpty() = chromosomes.isEmpty()

    /**
     * Provides an iterator over the chromosomes in the genotype.
     *
     * The `iterator` method allows for iterating over each chromosome within the genotype. This is useful for
     * traversing the genetic structure of an individual, enabling operations that require sequential access to each
     * chromosome.
     *
     * ## Usage:
     * This method can be used in loops and other iterative constructs to process each chromosome individually.
     *
     * ### Example:
     * ```kotlin
     * val chromosome1 = MyChromosome(MyGene(1), MyGene(2))
     * val chromosome2 = MyChromosome(MyGene(3), MyGene(4))
     * val genotype = Genotype(chromosome1, chromosome2)
     * for (chromosome in genotype) {
     *     println(chromosome)
     * }
     * // Output will be:
     * // Chromosome(genes=[Gene(1), Gene(2)])
     * // Chromosome(genes=[Gene(3), Gene(4)])
     * ```
     *
     * @return An iterator over the chromosomes in the genotype.
     */
    override fun iterator() = chromosomes.iterator()

    /**
     * Verifies the validity of the genotype by checking all its chromosomes.
     *
     * @return `true` if all chromosomes in the genotype are valid, `false` otherwise.
     */
    override fun verify() = chromosomes.all { it.verify() }

    /**
     * Retrieves the chromosome at the specified index.
     *
     * @param index The index of the chromosome to retrieve.
     * @return The chromosome at the specified index.
     * @throws InvalidIndexException If the index is out of range.
     */
    operator fun get(index: Int): Either<CompositeException, Chromosome<T, G>> {
        constrained {
            "The index ($index) must be in the range [0, $size)"(::InvalidIndexException) {
                index must BeInRange(this@Genotype.indices)
            }
        }.onLeft { return it.left() }
        return chromosomes[index].right()
    }

    /**
     * Returns a string representation of the genotype.
     *
     * @return A string representation of the genotype.
     */
    override fun toString() = when (Domain.toStringMode) {
        ToStringMode.SIMPLE -> chromosomes.joinToString(separator = ", ", prefix = "[", postfix = "]")
        else -> "Genotype(chromosomes=$chromosomes)"
    }
}
