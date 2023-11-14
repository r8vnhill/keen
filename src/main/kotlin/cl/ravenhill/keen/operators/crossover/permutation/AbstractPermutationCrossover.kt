/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.crossover.permutation

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.crossover.AbstractCrossover
import cl.ravenhill.keen.util.duplicates

/**
 * Abstract class for implementing permutation crossover in genetic algorithms.
 *
 * Permutation crossover is a genetic operation used for problems where the solution is a permutation
 * of elements, such as scheduling or routing problems. This class provides a framework for implementing
 * permutation crossover by defining the [performPermutationCrossover] method from the [PermutationCrossover] interface.
 *
 * The [crossoverChromosomes] method orchestrates the crossover process, ensuring that the resulting
 * offspring are valid permutations and do not contain duplicated genes.
 *
 * ## Constraints:
 * This class imposes constraints to ensure that the chromosomes involved in the crossover do not
 * contain duplicate genes, as this would violate the permutation property.
 *
 * ## Usage:
 * To use this class, extend it and implement the [performPermutationCrossover] method. This method
 * should define how the chromosomes are combined to produce offspring. The actual crossover logic
 * will depend on the specific requirements of the problem being solved.
 *
 * ```kotlin
 * class MyPermutationCrossover : AbstractPermutationCrossover<Int, IntGene>() {
 *     override fun performPermutationCrossover(chromosomes: List<Chromosome<Int, IntGene>>): List<List<IntGene>> {
 *         // Implement specific crossover logic here
 *     }
 * }
 * ```
 *
 * @param DNA The type of data that represents an individual's genotype.
 * @param G The specific type of [Gene] that encapsulates the [DNA] type data.
 * @param numOffspring The number of offspring to produce in each crossover operation.
 * @param numParents The number of parent chromosomes involved in the crossover.
 * @param exclusivity Determines whether the crossover operation is exclusive.
 * @param chromosomeRate The rate at which chromosomes are selected for crossover.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 1.2.0
 * @version 2.0.0
 */
abstract class AbstractPermutationCrossover<DNA, G>(
    numOffspring: Int = 2,
    numParents: Int = 2,
    exclusivity: Boolean = false,
    chromosomeRate: Double = 1.0
) : AbstractCrossover<DNA, G>(numOffspring, numParents, exclusivity, chromosomeRate),
    PermutationCrossover<DNA, G>
      where G : Gene<DNA, G> {

    /**
     * Orchestrates the permutation crossover process on a given set of chromosomes.
     * It ensures the validity of the permutation and applies the crossover rate.
     *
     * @param chromosomes The list of parent chromosomes to undergo crossover.
     * @return A list of offspring chromosomes resulting from the crossover.
     */
    override fun crossoverChromosomes(chromosomes: List<Chromosome<DNA, G>>): List<Chromosome<DNA, G>> {
        constraints {
            for (chromosome in chromosomes) {
                "A permutation crossover can't have duplicated genes: ${chromosome.genes.duplicates}" {
                    chromosome.genes.distinct() must HaveSize(chromosome.genes.size)
                }
            }
        }
        if (Core.random.nextDouble() > chromosomeRate) return chromosomes
        val crossed = performPermutationCrossover(chromosomes)
        return crossed.map { chromosomes[0].withGenes(genes = it) }
    }
}
