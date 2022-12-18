/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.SelectorException
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.util.optimizer.PhenotypeOptimizer
import java.util.Objects
import java.util.stream.Stream

class TournamentSelector<DNA>(private val sampleSize: Int) : AbstractSelector<DNA>() {

    override fun select(
        population: List<Phenotype<DNA>>,
        count: Int,
        optimizer: PhenotypeOptimizer<DNA>
    ) = List(count) {
        selectOneFrom(population, optimizer)
    }

    private fun selectOneFrom(
        population: List<Phenotype<DNA>>,
        optimizer: PhenotypeOptimizer<DNA>
    ): Phenotype<DNA> {
        return Stream.generate { population[Core.random.nextInt(population.size)] }
            .limit(sampleSize.toLong())
            .max(optimizer.comparator)
            .orElseThrow {
                SelectorException {
                    "An error occurred while trying to select an individual by tournament selection"
                }
            }
}

    override fun equals(other: Any?) = when {
        other === this -> true
        other !is TournamentSelector<*> -> false
        other::class != this::class -> false
        other.sampleSize != this.sampleSize -> false
        else -> true
    }

    override fun toString() = "TournamentSelector { sampleSize: $sampleSize }"

    override fun hashCode() = Objects.hash(TournamentSelector::class, sampleSize)
}
