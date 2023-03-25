/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.numerical.NumberGene
import cl.ravenhill.keen.operators.CombineCrossover


/**
 * A [CombineCrossover] that performs mean crossover on [NumberGene]s in the chromosome.
 * Given a list of chromosomes, this crossover will take the average of the genes in each chromosome
 * with a given rate.
 *
 * @param DNA The type of the number values stored in the genes.
 * @param probability The probability of performing crossover on each individual of the population.
 * @param chromosomeRate The rate of chromosomes that will undergo crossover.
 * @param geneRate The rate of genes that will undergo crossover within each chromosome.
 *
 * @constructor Creates a [MeanCrossover] instance with the given crossover rates and probability.
 *
 * @see NumberGene
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
class MeanCrossover<DNA : Number>(
    probability: Double,
    chromosomeRate: Double = 1.0,
    geneRate: Double = 1.0
) : CombineCrossover<DNA>(
    { genes: List<Gene<DNA>> ->
        @Suppress("UNCHECKED_CAST")
        genes as List<NumberGene<DNA>>
        genes[0].average(genes.drop(1))
    },
    probability,
    chromosomeRate,
    geneRate
) {
    override fun toString() = "MeanCrossover { probability: $probability }"
}