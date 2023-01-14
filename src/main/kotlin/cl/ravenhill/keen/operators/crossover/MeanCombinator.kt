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
import cl.ravenhill.keen.operators.CombineAlterer


/**
 * Performs a combination between two genotypes using the mean of the genes.
 *
 * @param DNA   The type of the DNA.
 *              Must be a [Number] type.
 * @param probability   The probability of this crossover to be applied.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 */
class MeanCombinator<DNA : Number>(probability: Double) :
        CombineAlterer<DNA>(
            // This cast should be safe
            { g1: Gene<DNA>, g2: Gene<DNA> -> (g1 as NumberGene<DNA>).mean(g2 as NumberGene<DNA>) },
            probability
        ) {
    override fun toString() = "MeanCrossover { probability: $probability }"
}