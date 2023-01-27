/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators

import cl.ravenhill.keen.Core.Contract
import cl.ravenhill.keen.DoubleRequirement
import cl.ravenhill.keen.IntRequirement.BeAtLeast
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.util.validateProbability
import java.util.*

/**
 * Represents an operator that alters the population.
 * It is used to perform mutations, crossovers, etc.
 *
 * @param DNA  The type of the DNA.
 * @property probability The probability of this alterer to be applied.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 0.1.0
 * @version 2.0.0
 */
interface Alterer<DNA> {
    val probability: Double

    /**
     * Applies this alterer to the given population.
     *
     * @param population The population to be altered.
     * @param generation The current generation.
     * @return The altered population and the number of alterations.
     */
    operator fun invoke(population: Population<DNA>, generation: Int): AltererResult<DNA>
}

/**
 * Abstract class that implements the [Alterer] interface.
 *
 * @param DNA The type of the DNA.
 * @property probability The probability of this alterer to be applied.
 * @constructor Creates a new [AbstractAlterer].
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 0.1.0
 * @version 2.0.0
 */
abstract class AbstractAlterer<DNA>(final override val probability: Double) :
    Alterer<DNA> {
    init {
        probability should DoubleRequirement.BeInRange(0.0..1.0) {
            "The probability must be between 0 and 1"
        }
    }
}

class AltererResult<DNA>(
    val population: Population<DNA>,
    val alterations: Int = 0
) {
    init {
        Contract {
            alterations should BeAtLeast(0) {
                "The number of alterations cannot be negative"
            }
        }
    }

    /**
     * Returns the population.
     */
    operator fun component1() = population

    /**
     * Returns the number of alterations.
     */
    operator fun component2() = alterations

    override fun toString() =
        "AltererResult { population: $population, alterations: $alterations }"

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is AltererResult<*> -> false
        population != other.population -> false
        alterations != other.alterations -> false
        else -> true
    }

    override fun hashCode() = Objects.hash(AltererResult::class, population, alterations)
}