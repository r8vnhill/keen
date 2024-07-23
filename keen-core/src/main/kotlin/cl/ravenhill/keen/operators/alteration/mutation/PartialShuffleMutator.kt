package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.exceptions.MutatorConfigurationException
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene

/**
 * Implements a partial shuffle mutation strategy for genetic algorithms. This mutator class selectively shuffles
 * a segment of genes within a chromosome based on specified probabilities. It is designed to enhance genetic diversity
 * and potentially improve convergence behavior by exploring new gene arrangements without completely disrupting the
 * chromosome's structure.
 *
 * ## Characteristics:
 * - **Individual Rate**: Probability of mutating an individual within a population.
 * - **Chromosome Rate**: Probability of mutating a chromosome within an individual.
 * - **Shuffle Boundary Probability**: Controls the likelihood of starting and ending the shuffle operation within
 *   the chromosome. The shuffle is bound within these start and end indices.
 *
 * ## Usage:
 * This mutator can be applied to any chromosome type that supports gene shuffling, making it versatile for various
 * genetic algorithm applications. It is particularly suited for problems where small changes to gene order can lead
 * to significant differences in solution quality, such as the Travelling Salesman Problem.
 *
 * ### Example:
 * ```kotlin
 * val mutator = PartialShuffleMutator<Double, DoubleGene>()
 * val mutatedChromosome = mutator.mutateChromosome(originalChromosome)
 * ```
 *
 * ## Validation:
 * Ensures that all configured rates are within the acceptable range [0, 1], throwing a `MutatorConfigException`
 * if any rates are out of bounds.
 *
 * ## References:
 * - Abdoun, Abouchabaka, and Tajani, “Analyzing the Performance of Mutation Operators to Solve the Travelling
 *   Salesman Problem,” which discusses the effectiveness of different mutation strategies including shuffles.
 *
 * @param T The type parameter of the gene values.
 * @param G The type of [Gene] being used, which must conform to [Gene<T, G>].
 * @param individualRate
 *  The mutation rate applied to individuals. Default is [DEFAULT_INDIVIDUAL_RATE].
 * @param chromosomeRate
 *  The mutation rate applied to chromosomes within individuals. Default is [DEFAULT_CHROMOSOME_RATE].
 * @param shuffleBoundaryProbability
 *  The probability that determines the boundaries of the gene shuffle within a chromosome. Default is
 *  [DEFAULT_SHUFFLE_BOUNDARY_PROBABILITY].
 */
class PartialShuffleMutator<T, G>(
    override val individualRate: Double = DEFAULT_INDIVIDUAL_RATE,
    override val chromosomeRate: Double = DEFAULT_CHROMOSOME_RATE,
    val shuffleBoundaryProbability: Double = DEFAULT_SHUFFLE_BOUNDARY_PROBABILITY
) : Mutator<T, G> where G : Gene<T, G> {

    init {
        constraints {
            "Individual mutation rate must be in the range [0, 1]"(::MutatorConfigurationException) {
                individualRate must BeInRange(0.0..1.0)
            }
            "Chromosome mutation rate must be in the range [0, 1]"(::MutatorConfigurationException) {
                chromosomeRate must BeInRange(0.0..1.0)
            }
            "Shuffle boundary probability must be in the range [0, 1]"(::MutatorConfigurationException) {
                shuffleBoundaryProbability must BeInRange(0.0..1.0)
            }
        }
    }

    /**
     * Performs a partial shuffle mutation on a given chromosome. This mutation process involves randomly shuffling
     * a subset of genes within the chromosome, defined by a dynamically determined start and end index. The range
     * for this shuffle is determined based on the `shuffleBoundaryProbability`.
     *
     * ## Functionality:
     * - **No Mutation Condition**: If `shuffleBoundaryProbability` is set to 0.0, the mutation process is skipped,
     *   and the original chromosome is returned unchanged, optimizing performance by avoiding unnecessary operations.
     * - **Determine Shuffle Start**: Iteratively examines each gene in the chromosome from the beginning. The first
     *  gene index that meets the shuffle probability condition becomes the start index of the segment to be shuffled.
     * - **Determine Shuffle End**: Starting from the determined start index, it searches for the first gene index where
     *  the probability condition to end the shuffle is met. This index marks the end of the segment to be shuffled.
     * - **Shuffle and Reconstruct**: The genes between the determined start and end indices (inclusive) are shuffled.
     *   The chromosome is then reconstructed by combining the unshuffled start segment, the shuffled segment, and the
     *   unshuffled end segment into a new chromosome.
     *
     * ## Usage:
     * This mutation method is invoked by the genetic algorithm framework during the mutation phase and is applicable
     * to any chromosome type compatible with the defined `Gene` type parameter.
     *
     * ### Example:
     * Assuming `shuffleBoundaryProbability` is greater than 0, here is how the mutation might be applied:
     * ```kotlin
     * // Assuming chromosome with genes [1, 2, 3, 4, 5] and shuffleBoundaryProbability
     * // set to trigger between indices 1 and 3
     * val originalChromosome = IntChromosome(1, 2, 3, 4, 5)
     * val mutator = PartialShuffleMutator<Double, IntGene>(shuffleBoundaryProbability = 0.5)
     * val mutatedChromosome = mutator.mutateChromosome(originalChromosome)
     * // Result might be [1, 3, 2, 4, 5] depending on the randomness outcome
     * ```
     *
     * @param chromosome The chromosome to be mutated.
     * @return
     *  A new chromosome instance with the specified genes partially shuffled if conditions are met; otherwise, returns
     *  the original chromosome if no mutation occurs.
     */
    override fun mutateChromosome(chromosome: Chromosome<T, G>): Chromosome<T, G> {
        if (shuffleBoundaryProbability == 0.0) return chromosome  // No mutation if the probability is zero.

        val genes = chromosome.genes
        var start = 0
        var end = chromosome.size - 1

        // Identify the start index for the shuffle.
        chromosome.indices.find { Domain.random.nextDouble() < shuffleBoundaryProbability }?.let {
            start = it
        }

        // Identify the end index for the shuffle, starting from the previously found start index.
        (start..<chromosome.size).find { Domain.random.nextDouble() > shuffleBoundaryProbability }?.let {
            end = it
        }

        // Perform the shuffle on the determined segment and reconstruct the chromosome.
        return chromosome.duplicateWithGenes(
            chromosome.take(start) +
                    genes.subList(start, end + 1).shuffled(Domain.random) +
                    chromosome.drop(end + 1)
        )
    }

    override fun toString() =
        "PartialShuffleMutator(individualRate=$individualRate, chromosomeRate=$chromosomeRate, " +
                "shuffleBoundaryProbability=$shuffleBoundaryProbability)"

    companion object {
        /**
         * Default individual rate for the partial shuffle mutator.
         */
        const val DEFAULT_INDIVIDUAL_RATE = 1.0

        /**
         * Default chromosome rate for the partial shuffle mutator.
         */
        const val DEFAULT_CHROMOSOME_RATE = 1.0

        /**
         * Default shuffle boundary probability for the partial shuffle mutator.
         */
        const val DEFAULT_SHUFFLE_BOUNDARY_PROBABILITY = 0.5
    }
}
