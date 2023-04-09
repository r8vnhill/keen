/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.incremental
import cl.ravenhill.keen.util.optimizer.PhenotypeOptimizer
import cl.ravenhill.keen.util.validateSum

private const val SERIAL_INDEX_THRESHOLD = 35

/**
 * Abstract class for probability-based selectors.
 *
 * @param DNA  The type of the DNA of the Genotype
 * @property sorted Whether the population should be sorted before selection.
 * @constructor Creates a new probability-based selector.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 1.3.0
 */
abstract class AbstractProbabilitySelector<DNA, G: Gene<DNA, G>>(protected val sorted: Boolean) :
    AbstractSelector<DNA, G>() {

    abstract fun probabilities(
        population: Population<DNA, G>,
        count: Int,
        optimizer: PhenotypeOptimizer<DNA, G>
    ): DoubleArray

    override fun select(
        population: Population<DNA, G>,
        count: Int,
        optimizer: PhenotypeOptimizer<DNA, G>
    ): Population<DNA, G> {
        val pop = if (sorted) {
            optimizer.sort(population)
        } else {
            population
        }
        val probabilities = probabilities(population, count, optimizer)
        probabilities.validateSum(1.0) { "Probabilities sum must be 1.0" }
        checkAnCorrect(probabilities)
        probabilities.incremental()
        return List(count) { pop[indexOf(probabilities)] }
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
        val v = Core.random.nextDouble()
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
        var index = -1
        var i = 0
        while (i < incr.size && index == -1) {
            if (incr[i] >= Core.random.nextDouble()) {
                index = i
            }
            ++i
        }

        return index
    }
}
