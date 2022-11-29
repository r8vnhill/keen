/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.util.optimizer

import cl.ravenhill.keen.genetic.Phenotype

/**
 * Generic optimization strategy to determine which of two phenotypes is better.
 */
interface PhenotypeOptimizer {
    val comparator
        get() = Comparator { p1: Phenotype<*>, p2: Phenotype<*> -> compare(p1, p2) }

    operator fun invoke(a: Phenotype<*>, b: Phenotype<*>): Int = compare(a, b)

    fun compare(p1: Phenotype<*>, p2: Phenotype<*>): Int

    fun sort(population: List<Phenotype<*>>) =
        population.sortedWith(comparator.reversed())
}
