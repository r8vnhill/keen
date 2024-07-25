/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.evolution.states.State
import cl.ravenhill.keen.exceptions.CrossoverException
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.alteration.Alterer
import cl.ravenhill.keen.utils.indices
import cl.ravenhill.keen.utils.subsets
import cl.ravenhill.keen.utils.transpose

/**
 * Interface defining the crossover genetic operation in evolutionary algorithms.
 *
 * `Crossover` is a key component in genetic algorithms, simulating the biological crossover observed in reproduction.
 * It combines genetic information from parent genotypes to create offspring genotypes, contributing to genetic
 * diversity and exploration of the solution space.
 *
 * @param T The type of data encapsulated by the genes.
 * @param G The type of gene, conforming to the [Gene] interface.
 * @property numOffspring The number of offspring produced by a single crossover operation.
 * @property numParents The number of parent genotypes required for the crossover operation.
 * @property chromosomeRate The probability of selecting a chromosome for the crossover operation.
 * @property exclusivity Flag indicating if the same individual can be a parent more than once in the crossover process.
 */
interface Crossover<T, G> : Alterer<T, G> where G : Gene<T, G> {
    val numOffspring: Int
    val numParents: Int
    val chromosomeRate: Double
    val exclusivity: Boolean

    /**
     * Executes the recombination operation on the current evolutionary state to generate offspring.
     *
     * Recombination, a critical step in evolutionary algorithms, involves selecting parent individuals from the
     * current population and applying crossover operations to produce offspring. The offspring then typically
     * replace or supplement the existing population in the evolutionary process.
     *
     * ## Process:
     * 1. **Parent Selection**: Randomly selects subsets of individuals from the current population, based on the
     *    specified number of parents and exclusivity criteria. These subsets represent potential parent groups for
     *    recombination.
     * 2. **Crossover and Offspring Creation**: Performs crossover operations on the selected parents to produce
     *   offspring. This step is repeated until the desired number of offspring is obtained.
     * 3. **State Update**: Constructs a new evolutionary state with the offspring, replacing the existing population.
     *
     * ## Usage:
     * This method is generally invoked during the evolutionary cycle, specifically in the reproduction phase where new
     * genetic variations are introduced into the population.
     *
     * ### Example:
     * ```
     * val recombinationOperator = RecombinationOperator<MyDataType, MyGene>(/* parameters */)
     * val newState = recombinationOperator(currentState, 100) // Produces 100 offspring
     * ```
     * In this example, `RecombinationOperator` is applied to the `currentState` of the evolutionary process to
     * generate 100 offspring, resulting in a new `EvolutionState` that reflects the updated population.
     *
     * @param state The current [GeneticEvolutionState] encapsulating the population and other relevant evolutionary data.
     * @param outputSize The desired number of offspring to be produced through recombination.
     * @return An updated [GeneticEvolutionState] containing the newly produced offspring, with a population size equal to
     * `outputSize`.
     */
    override fun invoke(state: State<T, G>, outputSize: Int): State<T, G> {
        // Select a subset of individuals to recombine using the provided probability and other parameters
        val parents = Domain.random.subsets(state.population, numParents, exclusivity)
        // Recombine the selected individuals to produce offspring
        val recombined = mutableListOf<Individual<T, G>>()
        while (recombined.size < outputSize) {
            crossover(parents.random(Domain.random).map { it.genotype }).forEach {
                recombined += Individual(it)
            }
        }
        return state.copy(population = recombined.take(outputSize))
    }

    /**
     * Performs crossover on a list of parent genotypes to produce offspring genotypes.
     *
     * This function is a core component of evolutionary algorithms, where it simulates the biological process of
     * crossover seen in reproduction. It combines parts of genotypes (genetic makeup) from two or more parents to
     * produce one or more offspring genotypes. The crossover operation is controlled by the probability of selecting
     * chromosomes from parents for recombination.
     *
     * ## Constraints:
     * - The size of the [parentGenotypes] list must match the predefined number of parents ([numParents]).
     *
     * ## Process:
     * 1. Randomly selects indices of chromosomes from the parent genotypes based on the [chromosomeRate].
     * 2. Creates a collection of chromosomes, each containing chromosomes at the same index from each parent.
     * 3. Recombines these chromosomes to create new chromosome sequences.
     * 4. Constructs new genotypes from these recombined chromosomes.
     *
     * ## Usage:
     * This method is used in the crossover stage of evolutionary algorithms. It's particularly important for
     * maintaining genetic diversity and allowing the algorithm to explore a broader range of solutions.
     *
     * ### Example:
     * ```
     * val parent1 = Genotype(listOf(Chromosome1(), Chromosome2()))
     * val parent2 = Genotype(listOf(Chromosome3(), Chromosome4()))
     * val offspringGenotypes = crossover(listOf(parent1, parent2))
     * // offspringGenotypes will contain genotypes with recombined chromosomes from parent1 and parent2
     * ```
     *
     * ## Remark:
     * This does not ensure that th number of offspring genotypes is equal to [numOffspring]. For that, you should
     * call the [invoke] method.
     *
     * @param parentGenotypes The list of parent genotypes from which to generate offspring.
     * @return A list of offspring genotypes produced by the crossover of the parent genotypes.
     * @throws CompositeException containing all the exceptions thrown by the constraints.
     * @throws CollectionConstraintException if the size of the [parentGenotypes] list doesn't match the number of
     *   parents ([numParents]).
     */
    @Throws(CompositeException::class, CollectionConstraintException::class)
    fun crossover(parentGenotypes: List<Genotype<T, G>>): List<Genotype<T, G>> {
        constraints {
            "The number of inputs (${parentGenotypes.size}) must be equal to the number of parents ($numParents)"(
                ::CrossoverException
            ) {
                parentGenotypes must HaveSize(numParents)
            }
            "Genotypes must have the same number of chromosomes"(::CrossoverException) {
                parentGenotypes.map { it.size }.toSet() must HaveSize(1)
            }
            parentGenotypes.forEachIndexed { index, genotype ->
                "The number of chromosomes in parent $index must be greater than 0"(::CrossoverException) {
                    genotype must HaveSize { it > 0 }
                }
            }
        }
        val parentGenotypeSize = parentGenotypes.first().size // Number of chromosomes in each parent genotype
        // Select random indices of chromosomes to recombine
        val chromosomeIndices = Domain.random.indices(pickProbability = chromosomeRate, end = parentGenotypeSize)
        // Associate the chromosomes of each parent genotype with the selected indices
        val chromosomes = chromosomeIndices.map { index -> parentGenotypes.map { it[index] } }
        // Recombine the selected chromosomes
        val offspringChromosomes = chromosomes.map { crossoverChromosomes(it) }.transpose()
        // Create new genotypes from the recombined chromosomes
        return offspringChromosomes.map {
            var i = 0
            Genotype(parentGenotypes[0].mapIndexed { index, chromosome ->
                if (index in chromosomeIndices) {
                    it[i++]
                } else {
                    chromosome
                }
            })
        }
    }

    /**
     * Performs crossover on a list of chromosomes.
     *
     * This function applies the crossover genetic operation to a list of chromosomes. The crossover operation is a
     * fundamental process in genetic algorithms where two or more chromosomes exchange genetic material to produce new
     * offspring chromosomes. The specifics of how the crossover is performed depend on the implementation details and
     * the nature of the chromosomes.
     *
     * ## Usage:
     * Crossover is typically used in the context of genetic algorithms to generate new individuals from existing ones,
     * thereby exploring new points in the solution space. This function takes a list of parent chromosomes and returns
     * a new list of chromosomes that are the result of applying the crossover operation.
     *
     * @param chromosomes A list of chromosomes to undergo the crossover process. These chromosomes act as parents for
     *   the generation of offspring chromosomes.
     * @return A list of chromosomes resulting from the crossover of the input chromosomes. The size and contents of
     *   this list depend on the specific crossover implementation and the characteristics of the input chromosomes.
     */
    fun crossoverChromosomes(chromosomes: List<Chromosome<T, G>>): List<Chromosome<T, G>>
}
