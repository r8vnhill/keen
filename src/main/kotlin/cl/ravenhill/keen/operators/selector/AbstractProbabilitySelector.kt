/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.util.optimizer.PhenotypeOptimizer
import cl.ravenhill.keen.util.validateAtLeast

private const val SERIAL_INDEX_THRESHOLD = 35

abstract class AbstractProbabilitySelector<DNA>(protected val sorted: Boolean) : Selector<DNA> {

    abstract fun probabilities(
        population: List<Phenotype<DNA>>,
        count: Int,
        optimizer: PhenotypeOptimizer<DNA>
    ): DoubleArray

    override operator fun invoke(
        population: List<Phenotype<DNA>>,
        count: Int,
        optimizer: PhenotypeOptimizer<DNA>
    ): List<Phenotype<DNA>> {
        count.validateAtLeast(0) { "Selection count [$count] must be at least 0" }
        val pop = if (sorted) {
            optimizer.sort(population)
        } else {
            population
        }
        val probabilities = probabilities(population, count, optimizer)
        checkAnCorrect(probabilities)
        incremental(probabilities)
        return List(count) { pop[indexOf(probabilities)] }
    }

    private fun incremental(probabilities: DoubleArray): DoubleArray {
        for (i in 1 until probabilities.size) {
            probabilities[i] += probabilities[i - 1]
        }
        return probabilities
    }

    /**
     * Checks if the given probability values are finite. If not, all values are
     * set to the same probability.
     */
    private fun checkAnCorrect(probabilities: DoubleArray) {
        var ok = true
        var i = probabilities.size
        while (ok && --i >= 0) {
            ok = probabilities[i].isFinite()
        }
        if (!ok) {
            probabilities.fill(1.0 / probabilities.size)
        }
    }

    private fun indexOf(incr: DoubleArray): Int {
        return if (incr.size <= SERIAL_INDEX_THRESHOLD) {
            serialIndexOf(incr)
        } else {
            binaryIndexOf(incr)
        }
    }

    private fun binaryIndexOf(incr: DoubleArray): Int {
        var imin = 0
        var imax = incr.size
        var index = -1
        val v = Core.rng.nextDouble()
        while (imin < imax && index == -1) {
            val imid = (imin + imax) ushr 1
            if (imid == 0 || (incr[imid] >= v && incr[imid - 1] < v)) {
                index = imid
            } else if (incr[imid] < v) {
                imin = imid + 1
            } else {
                imax = imid
            }
        }
        return index
    }

    private fun serialIndexOf(incr: DoubleArray): Int {
        TODO("Not yet implemented")
    }
}