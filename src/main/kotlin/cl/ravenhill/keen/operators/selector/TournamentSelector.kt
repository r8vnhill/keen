/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.keen.core.Genotype
import cl.ravenhill.keen.core.KeenCore
import cl.ravenhill.keen.util.Optimizer
import kotlin.random.asKotlinRandom

class TournamentSelector<DNA>(private val i: Int) : Selector<DNA> {
    override fun invoke(
        population: List<Genotype<DNA>>,
        count: Int,
        optimizer: Optimizer
    ): List<Genotype<DNA>> {
        val selection = mutableListOf<Genotype<DNA>>()
        for (i in 0 until count) {
            var fittest = population.random(KeenCore.generator.asKotlinRandom())
            for (j in 0 until i) {
                val challenger = population.random(KeenCore.generator.asKotlinRandom())
                if (optimizer(challenger.fitness, fittest.fitness)) {
                    fittest = challenger
                }
            }
            selection.add(fittest)
        }
        return selection
    }

    override fun equals(other: Any?) = when {
        other === this -> true
        other !is TournamentSelector<*> -> false
        other::class != this::class -> false
        other.i != this.i -> false
        else -> true
    }

    override fun toString(): String {
        return "TournamentSelector { i: $i }"
    }
}
