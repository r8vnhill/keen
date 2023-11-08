/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.crossover.combination

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.DoubleConstraint.BeInRange
import cl.ravenhill.jakt.constraints.collections.BeEmpty
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.exceptions.DoubleConstraintException
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.crossover.AbstractCrossover

/**
 * A crossover operator that combines genes from multiple parent chromosomes to produce offspring.
 * This crossover operation applies a combiner function to a list of genes taken from the same
 * position across all parent chromosomes.
 *
 * @param DNA The type of the gene's value.
 * @param G The gene type which implements [Gene] interface.
 * @property combiner A function that takes a list of genes and combines them into a single gene.
 * @property chromosomeRate The probability of a chromosome being selected for crossover.
 * @property geneRate The probability of a gene within a selected chromosome being combined.
 *
 * @constructor Initializes the crossover operation with the provided combiner function, chromosome rate,
 *              and gene rate, ensuring the gene rate falls within the valid range of 0.0 to 1.0.
 *              It also ensures that the chromosomes passed to combine have uniform length.
 *
 * @throws DoubleConstraintException if the gene rate is not in the range 0.0 to 1.0.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
open class CombineCrossover<DNA, G : Gene<DNA, G>>(
    val combiner: (List<G>) -> G,
    chromosomeRate: Double = 1.0,
    val geneRate: Double = 1.0
) : AbstractCrossover<DNA, G>(1, chromosomeRate = chromosomeRate) {

    init {
        constraints {
            "The gene rate [$geneRate] must be in 0.0..1.0" {
                geneRate must BeInRange(0.0..1.0)
            }
        }
    }

    /**
     * Combines genes from the same index across a list of chromosomes using the provided combiner function.
     *
     * @param chromosomes A list of parent chromosomes to be combined.
     * @return A list of genes, each created by combining genes from the same position across all parent chromosomes.
     */
    fun combine(chromosomes: List<Chromosome<DNA, G>>): List<G> {
        constraints {
            "Combination needs at least one chromosome" { chromosomes mustNot BeEmpty }
            "All chromosomes must have the same size" { chromosomes.map { it.size }.toSet() must HaveSize(1) }
        }
        return List(chromosomes[0].size) { i ->
            if (Core.random.nextDouble() < geneRate) {
                combiner(chromosomes.map { it[i] })
            } else {
                chromosomes[0][i]
            }
        }
    }

    /**
     * Performs crossover on chromosomes by combining their genes to produce offspring.
     *
     * @param chromosomes The parent chromosomes to undergo crossover.
     * @return A list containing a single offspring chromosome resulting from the combination.
     */
    override fun crossoverChromosomes(chromosomes: List<Chromosome<DNA, G>>) =
        listOf(chromosomes[0].withGenes(combine(chromosomes)))
}
