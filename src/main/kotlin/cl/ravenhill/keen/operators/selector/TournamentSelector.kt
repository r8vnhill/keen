/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.requirements.IntRequirement.BePositive
import cl.ravenhill.keen.util.optimizer.PhenotypeOptimizer
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import java.util.Objects

class TournamentSelector<DNA, G : Gene<DNA, G>>(private val sampleSize: Int) :
        AbstractSelector<DNA, G>() {

    init {
        enforce { "The sample size must be positive" { sampleSize should BePositive } }
    }

    override fun select(
        population: Population<DNA, G>,
        count: Int,
        optimizer: PhenotypeOptimizer<DNA, G>
    ): Population<DNA, G> = runBlocking {
        (0 until count).asFlow().map { selectOneFrom(population, optimizer) }.toList()
    }

    internal fun selectOneFrom(
        population: Population<DNA, G>,
        optimizer: PhenotypeOptimizer<DNA, G>
    ): Phenotype<DNA, G> {
        return generateSequence { population[Core.random.nextInt(population.size)] }
            .take(sampleSize)
            .maxWith(optimizer.comparator)
    }

    override fun equals(other: Any?) = when {
        other === this -> true
        other !is TournamentSelector<*, *> -> false
        other::class != this::class -> false
        other.sampleSize != this.sampleSize -> false
        else -> true
    }

    override fun toString() = "TournamentSelector { sampleSize: $sampleSize }"

    override fun hashCode() = Objects.hash(TournamentSelector::class, sampleSize)
}