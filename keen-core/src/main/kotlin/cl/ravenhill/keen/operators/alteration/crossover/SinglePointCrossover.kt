/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.jakt.ExperimentalJakt
import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.exceptions.CrossoverConfigException
import cl.ravenhill.keen.exceptions.CrossoverException
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.jakt.constraints.doubles.BeInRange as DoubleBeInRange
import cl.ravenhill.jakt.constraints.ints.BeInRange as IntBeInRange


/**
 * Implements a single-point crossover mechanism for evolutionary algorithms.
 *
 * `SinglePointCrossover` is a crossover strategy where two parent chromosomes are combined to produce offspring. This
 * specific implementation uses a single crossover point to split each parent chromosome into two segments. The
 * segments are then recombined with the corresponding segment from the other parent to create two new offspring
 * chromosomes.
 *
 * ## Characteristics:
 * - **Chromosome Rate**: The probability of crossover occurring, defaulting to 1.0 (100%).
 * - **Exclusivity**: A flag indicating whether the crossover is exclusive, defaulting to `false`.
 * - **Offspring and Parent Counts**: Always generates 2 offspring from 2 parent chromosomes.
 *
 * ## Pseudo-code:
 * ```
 * fun singlePointCrossover(P1, P2) {
 *   i = randomIndex(0, P1.size)
 *   O1 = P1[0..i] + P2[i+1..P2.size]
 *   O2 = P2[0..i] + P1[i+1..P1.size]
 *   return O1, O2
 * }
 * ```
 *
 * ## Theoretical example:
 * Suppose we have `P1 = [1, 2, 3, 4, 5]` and `P2 = [6, 7, 8, 9, 10]`.
 * If the randomly generated index `i = 2`, then the offspring would be:
 * ```
 * O1 = [1, 2, 8, 9, 10]
 * O2 = [6, 7, 3, 4, 5]
 * ```
 * This means that the first offspring inherits the first two genes from the first parent, and the last three genes
 * from the second parent. The second offspring inherits the first two genes from the second parent, and the last
 * three genes from the first parent.
 *
 * ## Usage:
 * Employed in evolutionary algorithms to introduce variation in the population by recombining genetic material from
 * two parent chromosomes. It's particularly useful in problems where maintaining the sequence of genes is important.
 *
 * ### Example:
 * Implementing a single-point crossover within a genetic algorithm:
 * ```kotlin
 * val crossover = SinglePointCrossover<MyType, MyGene>()
 * val parentChromosomes: List<Chromosome<MyType, MyGene>> = ...
 * val offspringChromosomes = crossover.crossoverChromosomes(parentChromosomes)
 * ```
 *
 * @param T The type of the gene's value.
 * @param G The gene type, which must be a subtype of `Gene<T, G>`.
 * @property chromosomeRate The rate at which chromosomes are crossed over. Default is 1.0.
 * @property exclusivity Indicates whether crossover excludes other operations. Default is `false`.
 * @constructor Creates a new instance of `SinglePointCrossover` with the given chromosome rate and exclusivity.
 */
@OptIn(ExperimentalJakt::class)
class SinglePointCrossover<T, G>(override val chromosomeRate: Double = 1.0, override val exclusivity: Boolean = false) :
    Crossover<T, G> where G : Gene<T, G> {

    override val numOffspring = 2

    override val numParents = 2

    init {
        constraints {
            "The chromosome rate must be in the range [0.0, 1.0]."(::CrossoverConfigException) {
                chromosomeRate must DoubleBeInRange(0.0..1.0)
            }
        }
    }

    /**
     * Executes the crossover process on a pair of parent chromosomes to generate offspring.
     *
     * This method implements the single-point crossover algorithm. It randomly selects a crossover point and then swaps
     * the gene segments after this point between the two parent chromosomes. This operation results in the generation
     * of two new offspring chromosomes, each inheriting a mix of genes from both parents.
     *
     * ## Constraints:
     * - The method requires exactly two parent chromosomes as input.
     * - Both parent chromosomes must have the same number of genes.
     *
     * ## Process:
     * 1. Validates the constraints on the input chromosomes.
     * 2. Checks if crossover should occur based on `chromosomeRate`.
     * 3. Selects a random crossover point.
     * 4. Performs the crossover to create two new sets of genes.
     * 5. Generates two new chromosomes from these gene sets.
     *
     * ## Usage:
     * Use this method within an evolutionary algorithm's reproduction phase where new offspring are generated from
     * existing parent chromosomes.
     *
     * ### Example:
     * ```kotlin
     * val crossoverOperator = SinglePointCrossover<MyType, MyGene>()
     * val parentChromosomes = listOf(chromosome1, chromosome2)
     * val offspringChromosomes = crossoverOperator.crossoverChromosomes(parentChromosomes)
     * ```
     *
     * @param chromosomes A list of two parent chromosomes to be crossed over.
     * @return A list containing two offspring chromosomes.
     * @throws CompositeException containing all the exceptions thrown by the constraints.
     * @throws CollectionConstraintException If the input list does not contain exactly two chromosomes of the same
     *   size.
     */
    @Throws(CompositeException::class, CollectionConstraintException::class)
    override fun crossoverChromosomes(chromosomes: List<Chromosome<T, G>>): List<Chromosome<T, G>> {
        constraints {
            "The number of parent chromosomes must be 2"(::CrossoverException) {
                chromosomes must HaveSize(2)
            }
            if (chromosomes.size == 2) {
                "Both parents must have the same size"(::CrossoverException) {
                    chromosomes[0] must HaveSize(chromosomes[1].size)
                }
            }
        }
        if (Domain.random.nextDouble() > chromosomeRate) {
            return chromosomes
        }
        val (first, second) = chromosomes.map { it.genes }
        val crossoverPoint = Domain.random.nextInt(minOf(first.size, second.size))
        val crossed = crossoverAt(crossoverPoint, first to second)
        return listOf(
            chromosomes[0].duplicateWithGenes(crossed.first),
            chromosomes[0].duplicateWithGenes(crossed.second)
        )
    }

    /**
     * Performs a single-point crossover at a specified point between two parent gene lists.
     *
     * This private method is a core component of the single-point crossover process. It takes a pair of gene lists
     * (representing parent chromosomes) and a crossover point. The genes are then split at this point and recombined
     * to form two new gene lists, each containing a mix of genes from both parents. This operation effectively
     * simulates the genetic crossover seen in biological reproduction.
     *
     * ## Constraints:
     * - The crossover point must be within the valid range of indices for the parent gene lists.
     *
     * ## Process:
     * 1. Validates that the crossover point is within the valid range of indices.
     * 2. Slices the parent gene lists at the crossover point.
     * 3. Recombines the sliced segments to form two new gene lists.
     *
     * ## Return:
     * Returns a pair of new gene lists, each resulting from the crossover of the parent genes.
     *
     * ## Usage:
     * This method is used internally by the `crossoverChromosomes` method in the context of a single-point crossover
     * operation within an evolutionary algorithm.
     *
     * @param crossoverPoint The index at which the crossover is to be performed.
     * @param parents A pair of lists of genes representing the parent chromosomes.
     * @return A pair of new gene lists resulting from the crossover.
     * @throws CompositeException If any composite constraints are violated during crossover.
     * @throws IntConstraintException If the crossover point is outside the valid range of indices.
     */
    @Throws(CompositeException::class, IntConstraintException::class)
    internal fun crossoverAt(crossoverPoint: Int, parents: Pair<List<G>, List<G>>): Pair<List<G>, List<G>> {
        val hi = parents.first.size
        constraints {
            "The crossover point must be in the range [0, $hi]."(::CrossoverException) {
                crossoverPoint must IntBeInRange(0..hi)
            }
            "Parents must have the same size"(::CrossoverException) {
                parents.first must HaveSize(parents.second.size)
            }
        }
        val newFirst = parents.first.slice(0..<crossoverPoint) + parents.second.slice(crossoverPoint..<hi)
        val newSecond = parents.second.slice(0..<crossoverPoint) + parents.first.slice(crossoverPoint..<hi)
        return newFirst to newSecond
    }

    override fun toString() = "SinglePointCrossover(chromosomeRate=$chromosomeRate, exclusivity=$exclusivity)"
}
