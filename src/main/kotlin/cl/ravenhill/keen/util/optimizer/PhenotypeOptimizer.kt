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
 *
 * @property comparator The comparator to use to determine which of two phenotypes is better.
 */
interface PhenotypeOptimizer<DNA> {
    val comparator
        get() = Comparator { p1: Phenotype<*>, p2: Phenotype<*> -> compare(p1, p2) }

    /**
     * Compares two phenotypes and returns a negative integer, zero, or a positive integer as the
     * first phenotype is less than, equal to, or greater than the second.
     */
    operator fun invoke(a: Phenotype<*>, b: Phenotype<*>): Int = compare(a, b)

    /**
     * Compares two phenotypes and returns a negative integer, zero, or a positive integer as the
     * first phenotype is less than, equal to, or greater than the second.
     */
    fun compare(p1: Phenotype<*>, p2: Phenotype<*>): Int

    /**
     * Sorts the given list of phenotypes using this optimizer.
     */
    fun sort(population: List<Phenotype<DNA>>) = population.sortedWith(comparator.reversed())

    private fun quicksort(list: List<Phenotype<DNA>>): List<Phenotype<DNA>> = if (list.size < 2) {
        list
    } else {
        val pivot = list[0]
        val less = list.drop(1).filter { compare(it, pivot) < 0 }
        val greater = list.drop(1).filter { compare(it, pivot) >= 0 }
        quicksort(less) + pivot + quicksort(greater)
    }
}
