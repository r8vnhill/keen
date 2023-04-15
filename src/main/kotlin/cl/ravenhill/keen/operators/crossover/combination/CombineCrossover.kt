/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.crossover.combination

import cl.ravenhill.keen.Core.Dice
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.crossover.AbstractCrossover
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
 * @param probability The probability of applying the crossover operator to each individual of the
 *      population.
 * @param chromosomeRate The rate of applying the crossover operator to individual chromosomes.
 * @param geneRate The rate of applying the crossover operator to individual genes.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 0.9
 * @version 2.0.0
 */
open class CombineCrossover<DNA, G : Gene<DNA, G>>(
    private val combiner: (List<G>) -> G,
    probability: Double,
    chromosomeRate: Double = 1.0,
    val geneRate: Double = 1.0
) : AbstractCrossover<DNA, G>(probability, 1, chromosomeRate = chromosomeRate) {

    /**
     * Combines the genes of the given chromosomes using the combiner function.
     * If the [geneRate] probability is less than the value returned by ``Dice.probability()``
     * the gene is taken from the first chromosome, otherwise it is taken from the combination
     * of the genes from the other chromosomes.
     *
     * @param chromosomes The list of chromosomes to be combined.
     * @return A new chromosome that is the result of the combination.
     * @see Dice.probability
     */
    internal fun combine(chromosomes: List<Chromosome<DNA, G>>) = List(chromosomes[0].size) { i ->
        if (Dice.probability() < geneRate) {
            combiner(chromosomes.map { it[i] })
        } else {
            chromosomes[0][i]
        }
    }

    /**
     * Applies the crossover operator to the given list of chromosomes, and returns a new
     * list of chromosomes that contains the result of the crossover operation.
     * This operation always returns a list of size 1.
     *
     * @param chromosomes The list of chromosomes to be crossed over.
     * @return A list of chromosomes that contains the result of the crossover operation.
     */
    override fun crossoverChromosomes(chromosomes: List<Chromosome<DNA, G>>) =
        listOf(chromosomes[0].withGenes(combine(chromosomes)))

    /// Documentation inherited from [Any]
    override fun toString() =
        "CombineCrossover(combiner=$combiner, probability=$probability, chromosomeRate=$chromosomeRate, geneRate=$geneRate)"
}