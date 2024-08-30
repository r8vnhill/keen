/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.alteration.crossover

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import cl.ravenhill.jakt.constrained
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.exceptions.CrossoverException
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.chromosomes.Chromosome
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.operators.alteration.Alterer
import cl.ravenhill.keen.utils.Exclusivity
import cl.ravenhill.keen.utils.indices
import cl.ravenhill.keen.utils.subsets
import cl.ravenhill.keen.utils.transpose

/**
 * Type alias for the `Crossover` interface, representing recombination operations in genetic algorithms.
 *
 * The `Recombination` alias is used interchangeably with the [Crossover] interface to emphasize the role of crossover
 * operations as a form of recombination in genetic algorithms. Recombination refers to the process of combining genetic
 * material from multiple parent individuals to create offspring, thereby introducing genetic diversity within the
 * population.
 *
 * @param T The type of the value held by the genes.
 * @param G The type of the gene, which must extend [Gene].
 * @see Crossover
 */
typealias Recombination<T, G> = Crossover<T, G>

/**
 * Error message indicating that the crossover operation could not be completed.
 *
 * This constant is used as the error message in instances where the crossover process fails within the evolutionary
 * algorithm. The message can be utilized in exception handling to provide a clear and consistent notification of the
 * failure.
 */
private const val FAILED_TO_PERFORM_CROSSOVER_OPERATION = "Failed to perform crossover operation"

/**
 * Interface for implementing crossover operations in genetic algorithms.
 *
 * The `Crossover` interface defines the contract for crossover operators in genetic algorithms. Crossover is a
 * fundamental genetic operation where genetic material from parent individuals is recombined to produce offspring.
 * This operation plays a crucial role in exploring the solution space and introducing genetic diversity within the
 * population, which are essential for the success of evolutionary algorithms.
 *
 * ## Theoretical Framework:
 * Crossover, also known as recombination, takes inspiration from the biological process where two parent organisms
 * exchange genetic material to create offspring with characteristics inherited from both parents. In genetic
 * algorithms, crossover operates on the representation of individuals, typically called chromosomes, to produce new
 * individuals that combine the parents' traits.
 *
 * The manner in which a crossover operator recombines genetic material significantly influences its effectiveness.
 * Different crossover strategies include:
 * - **Single-point Crossover**: The algorithm selects a single crossover point and exchanges genetic material between
 *   the two parents at that point.
 * - **Two-point Crossover**: The algorithm selects two crossover points and swaps the genetic material between these
 *   points in the parents.
 * - **Uniform Crossover**: The algorithm independently chooses each gene in the offspring from one of the parents with
 *   a uniform probability.
 *
 * Crossover is crucial for maintaining diversity within the population, preventing premature convergence to suboptimal
 * solutions (local optima). However, it must be balanced with selection pressure to ensure the algorithm converges to
 * an optimal solution in a reasonable amount of time.
 *
 * ## Usage:
 * The `Crossover` interface is designed to be implemented by classes that perform specific types of crossover
 * operations. It provides a flexible and powerful way to experiment with different crossover strategies within
 * evolutionary algorithms.
 *
 * The recommended way to use the crossover operator is through its [invoke] function, which abstracts the process of
 * selecting parents and generating offspring. The [crossover] method is exposed for fine-tuning and experimentation in
 * developing new algorithms and should generally be used only in that context.
 *
 * ### Example: Implementing a Single-Point Crossover Operator
 * ```kotlin
 * class SinglePointCrossover<T, G : Gene<T, G>>(
 *     override val chromosomeRate: Double,
 *     override val numParents: Int = 2,
 *     override val numOffspring: Int = 2,
 *     override val exclusivity: Exclusivity = Exclusivity.NON_EXCLUSIVE
 * ) : Crossover<T, G> {
 *     override fun crossover(parents: List<Genotype<T, G>>): List<Genotype<T, G>> {
 *         // Implement single-point crossover logic here
 *     }
 *     // ... Other methods and properties ...
 * }
 * ```
 *
 * @param T The type of the value held by the genes.
 * @param G The type of the gene, which must extend [Gene].
 * @property chromosomeRate The probability that a chromosome will be subject to crossover.
 * @property numParents The number of parents involved in the crossover operation.
 * @property numOffspring The number of offspring generated by the crossover operation.
 * @property exclusivity The exclusivity policy for selecting parents in the crossover process.
 * @return A result wrapping the updated evolutionary state after the crossover operation, or a [CrossoverException] if
 *   the operation fails.
 */
interface Crossover<T, G> : Alterer<T, G, Genotype<T, G>> where G : Gene<T, G> {

    val chromosomeRate: Double

    val numParents: Int

    val numOffspring: Int

    val exclusivity: Exclusivity

    /**
     * Performs the crossover operation on the given evolutionary state.
     *
     * This method is the primary entry point for executing a crossover operation within an evolutionary algorithm. It
     * selects parents from the population according to the defined parameters (number of parents, exclusivity) and
     * generates offspring through the crossover process. The resulting population is then used to build the next state
     * of the evolutionary process.
     *
     * ## Error Handling:
     * The method catches and handles any exceptions that occur during the crossover operation, returning
     * a [CrossoverException] if the process fails.
     *
     * @param state The current evolutionary state.
     * @param outputSize The desired size of the output population after the crossover operation.
     * @param buildState A function to build the new evolutionary state from the recombined individuals.
     * @return A result containing the new evolutionary state or a [CrossoverException] if the operation fails.
     */
    override suspend fun <S> invoke(
        state: S,
        outputSize: Int,
        buildState: (List<Individual<T, G, Genotype<T, G>>>) -> S
    ): Either<CrossoverException, S> where S : EvolutionState<T, G, Genotype<T, G>, S> = runCatching {
        val parents = Domain.random.subsets(state.population, numParents, exclusivity)
        val recombined = mutableListOf<Individual<T, G, Genotype<T, G>>>()
        while (recombined.size < outputSize) {
            val selectedParents = parents.random(Domain.random).map { it.representation }
            crossover(selectedParents)
                .getOrElse { return CrossoverException(FAILED_TO_PERFORM_CROSSOVER_OPERATION, it).left() }
                .forEach { recombined.add(Individual(it)) }
        }
        recombined
    }.fold(
        onSuccess = { buildState(it.take(outputSize)).right() },
        onFailure = { CrossoverException(FAILED_TO_PERFORM_CROSSOVER_OPERATION, it).left() }
    )

    /**
     * Performs a crossover operation on the provided parent genotypes to produce offspring genotypes.
     *
     * The `crossover` function takes a list of parent genotypes and applies a crossover operation to generate a new set
     * of offspring genotypes. This process involves validating the parent genotypes, selecting the indices of
     * chromosomes to crossover, and then generating new chromosomes for the offspring. The crossover operation is a key
     * mechanism in evolutionary algorithms, allowing the combination of genetic material from multiple parents to
     * create diversity in the population.
     *
     * ## Usage:
     * This function is typically called within the context of a genetic algorithm or similar evolutionary process. The
     * function is public to allow for fine-tuning of new algorithms or experimentation with crossover strategies.
     * However, the recommended way to use the crossover functionality is through the `Crossover` interface's [invoke]
     * operator, which provides a higher-level abstraction for performing crossover operations.
     *
     * @param parentGenotypes A list of genotypes from the parent individuals.
     * @return Either a list of new genotypes generated through the crossover operation, or a [CrossoverException] if
     *   an error occurs.
     * @throws CrossoverException if the crossover operation fails due to invalid inputs or other errors.
     */
    fun crossover(parentGenotypes: List<Genotype<T, G>>): Either<CrossoverException, List<Genotype<T, G>>> =
        runCatching {
            validateParentGenotypes(parentGenotypes)
            val parentGenotypeSize = parentGenotypes.first().size
            val chromosomeIndices = Domain.random.indices(chromosomeRate, parentGenotypeSize)
            val chromosomes = chromosomeIndices.map { index -> parentGenotypes.map { it[index] } }
            val offspringChromosomes = chromosomes.map(::crossoverChromosomes).transpose()
            generateOffspring(parentGenotypes, chromosomeIndices, offspringChromosomes)
        }.fold(
            onSuccess = { it.right() },
            onFailure = { throwable -> CrossoverException(FAILED_TO_PERFORM_CROSSOVER_OPERATION, throwable).left() }
        )

    /**
     * Performs a crossover operation on a list of chromosomes to produce a new set of chromosomes.
     *
     * The `crossoverChromosomes` function takes a list of chromosomes from multiple parents and performs a genetic
     * crossover operation to produce a new set of chromosomes for the offspring. The specific crossover mechanism used
     * (e.g., one-point, two-point, uniform) depends on the implementation of this function in the subclass or specific
     * context where it is applied.
     *
     * ## Usage:
     * The `crossoverChromosomes` function is public to allow for experimentation and fine-tuned control in specific
     * scenarios. However, the recommended way of using the `Crossover` interface is through its [invoke] operator,
     * which provides a higher-level abstraction for performing crossover operations in a more controlled and
     * standardized manner.
     *
     * @param chromosomes A list of chromosomes from the parent individuals.
     * @return A list of new chromosomes generated through the crossover operation.
     */
    fun crossoverChromosomes(chromosomes: List<Chromosome<T, G>>): List<Chromosome<T, G>>


    /**
     * Validates the parent genotypes before performing a crossover operation.
     *
     * The `validateParentGenotypes` function ensures that the list of parent genotypes provided for a crossover
     * operation meets the necessary requirements. The function checks that the number of parent genotypes matches the
     * expected number of parents, that all genotypes have the same number of chromosomes, and that each genotype
     * contains a positive number of chromosomes. If any of these constraints are violated, the function returns a
     * [CrossoverException] wrapped in an [Either.Left], indicating that the crossover operation cannot proceed.
     *
     * @param parentGenotypes A list of parent genotypes to be validated before the crossover operation.
     * @return An [Either] that contains `Unit` if the validation succeeds, or a [CrossoverException] if the validation
     *   fails.
     * @throws CrossoverException if any validation constraint is violated.
     */
    private fun validateParentGenotypes(parentGenotypes: List<Genotype<T, G>>): Either<CrossoverException, Unit> =
        constrained {
            "The number of inputs (${parentGenotypes.size}) must be equal to the number of parents ($numParents)" {
                parentGenotypes must HaveSize(numParents)
            }
            "Genotypes must have the same number of chromosomes" {
                parentGenotypes.map { it.size }.toSet() must HaveSize(1)
            }
            parentGenotypes.forEachIndexed { index, genotype ->
                "The number of chromosomes in parent $index must be greater than 0" {
                    genotype.size must BePositive
                }
            }
        }
            .getOrElse { return CrossoverException(FAILED_TO_PERFORM_CROSSOVER_OPERATION, it).left() }
            .right()

    /**
     * Generates offspring genotypes by combining chromosomes from parent genotypes and new chromosome sets.
     *
     * The `generateOffspring` function produces a list of offspring genotypes by selectively replacing chromosomes in
     * the parent genotypes with new chromosomes provided in the [offspringChromosomes] list. The chromosomes to be
     * replaced are specified by their indices in the `chromosomeIndices` list.
     *
     * @return A list of newly generated offspring genotypes, each containing a mix of chromosomes from the parent
     *   genotypes and the provided new chromosome sets.
     */
    private fun generateOffspring(
        parentGenotypes: List<Genotype<T, G>>,
        chromosomeIndices: List<Int>,
        offspringChromosomes: List<List<Chromosome<T, G>>>
    ): List<Genotype<T, G>> = offspringChromosomes.map { newChromosomes ->
        Genotype(parentGenotypes.first().mapIndexed { index, chromosome ->
            if (index in chromosomeIndices) {
                newChromosomes[index]
            } else {
                chromosome
            }
        })
    }
}
