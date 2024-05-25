package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.jakt.constraints.ints.BeNegative
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.exceptions.MutatorConfigException
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.alteration.mutation.DisplacementMutator.Companion.DEFAULT_CHROMOSOME_RATE
import cl.ravenhill.keen.operators.alteration.mutation.DisplacementMutator.Companion.DEFAULT_INDIVIDUAL_RATE

/**
 * Implements a displacement mutation for evolutionary algorithms, where genes within a chromosome are either shifted to
 * the left or right by a specified displacement value. This type of mutation can help in exploring the solution space
 * differently by rearranging the genes' positions without altering their presence within the chromosome.
 *
 * ## Constraints:
 * - `individualRate` and `chromosomeRate` must be within the range [0.0, 1.0].
 * - `displacement` must be a non-negative integer.
 *
 * ## Mutation Process:
 * 1. **No Mutation Conditions**: If the chromosome is empty, the displacement is zero, or the displacement
 *    is a multiple of the chromosome size (which would result in no effective change), the original
 *    chromosome is returned unchanged.
 * 2. **Direction Decision**: A random decision is made to determine whether the shift will be to the left
 *    or right.
 * 3. **Gene Shifting**: Based on the direction:
 *    - **Right Shift**: The last `displacement % size` genes are moved to the front.
 *    - **Left Shift**: The first `displacement % size` genes are moved to the end.
 *
 * ## Usage Example:
 * ```kotlin
 * val mutator = DisplacementMutator<Double, DoubleGene>()
 * val originalChromosome = DoubleChromosome(1.0, 2.0, 3.0, 4.0, 5.0)
 * val mutatedChromosome = mutator.mutateChromosome(originalChromosome)
 * println("Original: $originalChromosome")
 * println("Mutated: $mutatedChromosome")
 * // Output: Original: [1.0, 2.0, 3.0, 4.0, 5.0]
 * // Output: Mutated: [5.0, 1.0, 2.0, 3.0, 4.0] if the shift is to the right
 * // Output: Mutated: [2.0, 3.0, 4.0, 5.0, 1.0] if the shift is to the left
 * ```
 *
 * @param T The type parameter of the gene values.
 * @param G The type of Gene being used, which must conform to Gene<T, G>.
 * @param individualRate
 *  The mutation rate applied to individuals. Default is [DEFAULT_INDIVIDUAL_RATE].
 * @param chromosomeRate
 *  The mutation rate applied to chromosomes within individuals. Default is [DEFAULT_CHROMOSOME_RATE].
 * @param displacement
 *  The number of positions by which genes in the chromosome will be displaced. Default is [DEFAULT_DISPLACEMENT].
 * @throws CompositeException if any of the constraints are violated.
 * @throws MutatorConfigException if the configuration is invalid.
 */
class DisplacementMutator<T, G>(
    override val individualRate: Double = DEFAULT_INDIVIDUAL_RATE,
    override val chromosomeRate: Double = DEFAULT_CHROMOSOME_RATE,
    val displacement: Int = DEFAULT_DISPLACEMENT
) : Mutator<T, G> where G : Gene<T, G> {
    init {
        constraints {
            "The individual rate ($individualRate) must be in 0.0..1.0"(::MutatorConfigException) {
                individualRate must BeInRange(0.0..1.0)
            }
            "The chromosome rate ($chromosomeRate) must be in 0.0..1.0"(::MutatorConfigException) {
                chromosomeRate must BeInRange(0.0..1.0)
            }
            "The displacement must be a non-negative integer"(
                ::MutatorConfigException
            ) {
                displacement mustNot BeNegative
            }
        }
    }

    /**
     * Mutates a given chromosome by either shifting its genes to the left or right by a predetermined displacement
     * value. The direction of the shift (left or right) is randomly determined each time the mutation is applied. This
     * mutation can introduce variability into the population by altering the order of genes, potentially helping the
     * evolutionary algorithm to escape local optima.
     *
     * ## Conditions:
     * - If the chromosome is empty, contains a displacement of zero, or the displacement modulo the chromosome's size
     *   equals zero (implying no effective change), the chromosome is returned unchanged. These checks prevent
     *   unnecessary operations.
     *
     * ## Operation:
     * 1. **Random Direction Decision**: A boolean is randomly generated to decide the direction of the shift.
     *    - `true` results in a right shift.
     *    - `false` results in a left shift.
     * 2. **Shift Execution**:
     *    - If `right` is `true`, the `shiftRight` method is called with the displacement.
     *    - If `right` is `false`, the `shiftLeft` method is called with the displacement.
     *
     * ## Examples:
     * Given a chromosome with genes [1, 2, 3, 4, 5] and a displacement of 2, the mutation might result in:
     * - `[4, 5, 1, 2, 3]` if shifted right.
     * - `[3, 4, 5, 1, 2]` if shifted left.
     * The actual outcome depends on the random decision made at runtime.
     *
     * ## Usage:
     * This method is typically called by the genetic algorithm during the mutation phase of each generation,
     * where each chromosome has a chance to undergo mutation based on defined rates.
     *
     * @param chromosome The chromosome to mutate. It must be a type that conforms to `Chromosome<T, G>`.
     * @return
     *  A potentially mutated `Chromosome<T, G>`. If conditions prevent mutation, the original chromosome is returned.
     */
    override fun mutateChromosome(chromosome: Chromosome<T, G>): Chromosome<T, G> {
        // Early exit if mutation is not needed based on method parameters and chromosome state.
        if (chromosome.isEmpty() || displacement == 0 || displacement % chromosome.size == 0) {
            return chromosome
        }

        // Randomly determine the direction of the shift, either left or right.
        val right = Domain.random.nextBoolean()

        // Apply the mutation based on the direction decided.
        return if (right) {
            chromosome shiftRight displacement
        } else {
            chromosome shiftLeft displacement
        }
    }

    /**
     * Shifts the genes of a chromosome to the right by a specified number of positions. This method applies a circular
     * shift to the chromosome, meaning that genes that are shifted off the start of the chromosome reappear at the
     * end. The shift amount is taken modulo the chromosome's length to ensure it is within valid bounds.
     *
     * @param displacement
     *  The number of positions by which the genes should be shifted to the right. If the displacement exceeds the
     *  number of genes, it wraps around due to the modulo operation, effectively rotating the genes.
     * @return A new [Chromosome] instance with genes shifted right by the specified displacement, preserving the
     *         original order but changing their positions.
     */
    private infix fun <T, G> Chromosome<T, G>.shiftRight(displacement: Int): Chromosome<T, G> where G : Gene<T, G> {
        val size = size
        val shift = displacement % size  // Ensure the shift is within the bounds of the chromosome size.
        return duplicateWithGenes(genes.takeLast(shift) + genes.dropLast(shift))  // Rearrange genes according to the shift.
    }

    /**
     * Shifts the genes of a chromosome to the left by a specified number of positions. This method applies a circular
     * shift to the chromosome, meaning that genes that are shifted off the end of the chromosome reappear at the
     * beginning. The shift amount is taken modulo the chromosome's length to ensure it is within valid bounds.
     *
     * @param displacement
     *  The number of positions by which the genes should be shifted to the left. If the displacement exceeds the number
     *  of genes, it wraps around due to the modulo operation.
     * @return A new [Chromosome] instance with genes shifted left by the specified displacement.
     */
    private infix fun <T, G> Chromosome<T, G>.shiftLeft(displacement: Int): Chromosome<T, G> where G : Gene<T, G> {
        val size = size
        val shift = displacement % size  // Ensure the shift is within the bounds of the chromosome size.
        return duplicateWithGenes(genes.drop(shift) + genes.take(shift))  // Rearrange genes according to the shift.
    }

    /**
     * Companion object for the `DisplacementMutator` class, defining default values for the mutation parameters.
     * These defaults are used when specific values are not provided during mutator initialization.
     *
     * ## Usage:
     * These constants are particularly useful for simplifying the initialization of `DisplacementMutator` instances
     * by providing ready-to-use defaults that ensure a high mutation frequency and minimal displacement.
     */
    companion object {

        /**
         * Default mutation rate applied to individuals. This rate defines the probability with which any given
         * individual in a population will undergo mutation during the mutation phase of the evolutionary algorithm. The
         * default value is set to 1.0, meaning every individual is expected to mutate, unless specified otherwise.
         */
        const val DEFAULT_INDIVIDUAL_RATE = 1.0

        /**
         * Default mutation rate applied to chromosomes within an individual. This rate determines the likelihood that a
         * chromosome will undergo mutation when its individual is selected for mutation. The default rate is 1.0,
         * indicating that each chromosome in a mutating individual will always be subject to mutation.
         */
        const val DEFAULT_CHROMOSOME_RATE = 1.0

        /**
         * Default displacement value used in the displacement mutation strategy. Displacement refers to the number of
         * positions genes within the chromosome will be moved left or right during the mutation process. The default
         * displacement is set to 1, which means genes will be shifted by one position unless a different displacement
         * is specified.
         */
        const val DEFAULT_DISPLACEMENT = 1
    }

}
