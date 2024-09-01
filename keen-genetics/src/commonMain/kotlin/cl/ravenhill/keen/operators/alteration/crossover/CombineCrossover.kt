/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.alteration.crossover

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import cl.ravenhill.jakt.constrained
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.jakt.constraints.ints.BeAtLeast
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.exceptions.CrossoverException
import cl.ravenhill.keen.genetics.chromosomes.Chromosome
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.utils.Exclusivity
import cl.ravenhill.keen.utils.sequence

/**
 * A genetic operator that combines genes from multiple parents to produce offspring in an evolutionary algorithm.
 *
 * The `CombineCrossover` class implements a crossover operation commonly used in evolutionary algorithms. This operator
 * takes genes from a set of parent individuals and combines them to create new offspring. By mixing genetic material
 * from different parents, the crossover operator introduces diversity into the population, which is crucial for
 * exploring the solution space and avoiding premature convergence to local optima.
 *
 * ## Usage:
 * The `CombineCrossover` class is designed for use in evolutionary algorithms where the crossover operation is a key
 * component of the genetic evolution process. The class allows for customization of the crossover behavior through the
 * [combiner] function, [chromosomeRate], [geneRate], and [numParents] parameters, making it adaptable to various
 * evolutionary strategies.
 *
 * ### Example: Using `CombineCrossover`
 * ```kotlin
 * val crossover = CombineCrossover<Int, MyGene>(
 *     combiner = { genes -> genes.random(Domain.random) }, // Example of a uniform crossover strategy
 *     chromosomeRate = 0.8, // 80% chance of chromosome-level crossover
 *     geneRate = 0.5, // 50% chance of gene-level crossover
 *     numParents = 3, // Crossover among 3 parents
 *     exclusivity = Exclusivity.NON_EXCLUSIVE // Allows genes to be selected from the same parent multiple times
 * )
 * ```
 *
 * In this example, `CombineCrossover` is configured to perform a uniform crossover where genes are randomly selected
 * from the available parents. The `chromosomeRate` and `geneRate` determine the likelihood of crossover at both the
 * chromosome and gene levels, allowing for fine-tuned control over the genetic recombination process.
 *
 * @param T The type of the value held by the genes.
 * @param G The type of the gene, which must extend [Gene].
 * @param combiner A function that takes a list of genes from the parent individuals and combines them into a single
 *   gene for the offspring.
 * @param chromosomeRate The probability that an entire chromosome will undergo crossover.
 * @param geneRate The probability that a specific gene within a chromosome will undergo crossover.
 * @param numParents The number of parent individuals involved in the crossover.
 * @param exclusivity The policy determining whether a parent can be selected multiple times during the crossover.
 * @property numOffspring The number of offspring produced by the crossover operation.
 * @throws CompositeException if the gene rate is not between 0 and 1, the chromosome rate is not between 0 and 1, or
 *   the number of parents is less than 2.
 */
open class CombineCrossover<T, G>(
    val combiner: (List<G>) -> G,
    override val chromosomeRate: Double = DEFAULT_CHROMOSOME_RATE,
    val geneRate: Double = DEFAULT_GENE_RATE,
    override val numParents: Int = DEFAULT_NUM_PARENTS,
    override val exclusivity: Exclusivity = defaultExclusivity
) : Crossover<T, G> where G : Gene<T, G> {

    init {
        constrained {
            "The gene rate must be between 0 and 1" { geneRate must BeInRange(0.0..1.0) }
            "The chromosome rate must be between 0 and 1" { chromosomeRate must BeInRange(0.0..1.0) }
            "Number of parents must be greater or equal to 2" { numParents must BeAtLeast(2) }
        }.onLeft { throw it }
    }

    /**
     * The number of offspring produced by the crossover operation.
     */
    override val numOffspring: Int = 1

    /**
     * Performs a crossover operation on a list of chromosomes to generate new chromosomes.
     *
     * The `crossoverChromosomes` function takes a list of parent chromosomes and performs a crossover operation to
     * produce a list of offspring chromosomes. The crossover process involves combining the genes from the parent
     * chromosomes according to the configured crossover strategy.
     *
     * ## Recommended Usage:
     * The recommended way to perform a crossover operation is by using the [invoke] operator provided by the
     * [Crossover] interface. The `crossoverChromosomes` function is exposed primarily for use in the implementation of
     * new algorithmic variants and for cases where fine-grained control over the crossover operation is needed. Direct
     * use of this function allows developers to experiment with and fine-tune specific crossover strategies.
     *
     * @param chromosomes A list of parent chromosomes to be crossed over to produce offspring.
     * @return An `Either<CrossoverException, List<Chromosome<T, G>>>` where `Right` contains the list of offspring
     *         chromosomes, and `Left` contains a `CrossoverException` if the operation fails.
     */
    override fun crossoverChromosomes(
        chromosomes: List<Chromosome<T, G>>
    ): Either<CrossoverException, List<Chromosome<T, G>>> = listOf(
        chromosomes.first()
            .copyWithGenes(
                combine(chromosomes)
                    .getOrElse { return CrossoverException("Failed to combine genes", it).left() }
            )
    ).right()

    /**
     * Combines genes from multiple chromosomes to produce a new list of genes for offspring in a genetic algorithm.
     *
     * The `combine` function is responsible for the gene recombination step during a crossover operation. It takes a
     * list of parent chromosomes, validates them against specific constraints, and then combines their genes to create
     * a new list of genes for the offspring.
     *
     * ## Recommended Usage:
     * The recommended way to perform a crossover operation is by using the [invoke] operator provided by the
     * [Crossover] interface. The `combine` function is primarily intended for use in the implementation of new
     * algorithmic variants and for cases where fine-grained control over the gene combination process is needed. Direct
     * use of this function allows developers to experiment with and fine-tune specific gene recombination strategies.
     *
     * @param chromosomes A list of chromosomes to be combined, with each chromosome represented as a `List` of genes.
     * @return An `Either<CrossoverException, List<G>>` where `Right` contains the list of combined genes, and `Left`
     *         contains a `CrossoverException` if the operation fails.
     */
    fun combine(chromosomes: List<Chromosome<T, G>>): Either<CrossoverException, List<G>> {
        validateChromosomes(chromosomes).onLeft { return it.left() }
        return createGeneList(chromosomes)
    }

    /**
     * Validates the list of chromosomes to ensure they meet the requirements for a crossover operation.
     *
     * @param chromosomes A list of chromosomes to be validated for the crossover operation.
     * @return An `Either<CrossoverException, Unit>` where `Right` indicates successful validation, and `Left` contains
     *   a `CrossoverException` if the validation fails.
     */
    private fun validateChromosomes(chromosomes: List<Chromosome<T, G>>): Either<CrossoverException, Unit> =
        constrained {
            "Number of inputs (${chromosomes.size}) must equal the number of parents ($numParents)" {
                chromosomes must HaveSize(numParents)
            }
            "All chromosomes must have the same length" {
                chromosomes.map { it.size }.toSet() must HaveSize(1)
            }
        }.mapLeft {
            CrossoverException("Invalid input chromosomes", it)
        }

    /**
     * Creates a list of genes by combining or selecting genes from a list of parent chromosomes.
     *
     * @param chromosomes A list of chromosomes from which genes are selected or combined.
     * @return An `Either<CrossoverException, List<G>>` where `Right` contains the list of generated genes, and `Left`
     *   contains a `CrossoverException` if any step in the process fails.
     */
    private fun createGeneList(chromosomes: List<Chromosome<T, G>>): Either<CrossoverException, List<G>> =
        List(chromosomes[0].size) { i ->
            if (Domain.random.nextDouble() < geneRate) {
                retrieveGenes(chromosomes, i).flatMap { genes ->
                    combiner(genes).right()
                }
            } else {
                retrieveGene(chromosomes[0], i)
            }
        }.sequence()

    /**
     * Retrieves genes from a list of chromosomes at the specified index.
     *
     * @param chromosomes A list of chromosomes from which to retrieve the genes.
     * @param index The index of the gene to be retrieved within each chromosome.
     * @return An [Either] containing a list of genes on the right if successful, or a [CrossoverException] on the left
     *   if any retrieval fails.
     * @throws CrossoverException if the gene cannot be retrieved from any chromosome due to an invalid index or other
     *   issues.
     */
    private fun retrieveGenes(chromosomes: List<Chromosome<T, G>>, index: Int): Either<CrossoverException, List<G>> =
        chromosomes.map { geneList ->
            geneList[index].getOrElse {
                return CrossoverException("Failed to retrieve gene at index $index", it).left()
            }
        }.right()

    /**
     * Retrieves a gene from a given chromosome at the specified index.
     *
     * @param chromosome The chromosome from which to retrieve the gene.
     * @param index The index of the gene to be retrieved within the chromosome.
     * @return An `Either` containing the gene on the right if successful, or a `CrossoverException` on the left if the
     *   retrieval fails.
     * @throws CrossoverException if the gene cannot be retrieved due to an invalid index or other issues.
     */
    private fun retrieveGene(chromosome: Chromosome<T, G>, index: Int): Either<CrossoverException, G> =
        chromosome[index]
            .map { it.right() }
            .getOrElse { CrossoverException("Failed to retrieve gene at index $index", it).left() }

    companion object {

        /**
         * The default rate at which chromosomes are combined during the crossover process. Set to 0.5 by default.
         */
        const val DEFAULT_CHROMOSOME_RATE = 0.5


        /**
         * Default rate for gene crossover operations in the `CombineCrossover` class. Set to 0.5 by default.
         */
        const val DEFAULT_GENE_RATE = 0.5

        /**
         * The default number of parent chromosomes to be used in the crossover operation. Set to 2 by default.
         */
        const val DEFAULT_NUM_PARENTS = 2

        /**
         * Default exclusivity setting for crossover operations in the `CombineCrossover` class. Set to
         * [Exclusivity.NON_EXCLUSIVE] by default.
         */
        val defaultExclusivity = Exclusivity.NON_EXCLUSIVE
    }
}
