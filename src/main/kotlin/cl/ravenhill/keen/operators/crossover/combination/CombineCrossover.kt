/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.crossover.combination

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.DoubleConstraint.BeInRange
import cl.ravenhill.jakt.constraints.collections.BeEmpty
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.crossover.AbstractUniformLengthCrossover
import cl.ravenhill.keen.probability

/**
 * A crossover operator that combines genes from the given chromosomes by applying a function to the
 * corresponding genes from each chromosome.
 * The function that combines genes is specified as a lambda that takes a list of genes and returns
 * a new gene that represents the combination of those genes.
 * The probability of applying the crossover operator is specified by the [probability] parameter,
 * and the rate of applying the crossover operator to individual genes is specified by the
 * [geneRate] parameter.
 *
 * @param combiner A lambda that combines genes from the input chromosomes to produce a new gene.
 * @param chromosomeRate The rate of applying the crossover operator to individual chromosomes.
 * @param geneRate The rate of applying the crossover operator to individual genes.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 0.9
 * @version 2.0.0
 */
open class CombineCrossover<DNA, G : Gene<DNA, G>>(
    val combiner: (List<G>) -> G,
    chromosomeRate: Double = 1.0,
    val geneRate: Double = 1.0
) : AbstractUniformLengthCrossover<DNA, G>(1, chromosomeRate = chromosomeRate) {

    init {
        constraints {
            "The gene rate [$geneRate] must be in 0.0..1.0" {
                geneRate must BeInRange(0.0..1.0)
            }
        }
    }

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

    override fun crossoverChromosomes(chromosomes: List<Chromosome<DNA, G>>) =
        listOf(chromosomes[0].withGenes(combine(chromosomes)))
}