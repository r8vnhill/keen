/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators

import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.requirements.DoubleRequirement
import cl.ravenhill.keen.requirements.IntRequirement.BeAtLeast
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.requirements.DoubleRequirement.*
import java.util.*

/**
 * An interface for genetic algorithm operators that alter the population by recombining and/or
 * mutating individuals.
 * Implementations of this interface should specify the probability of applying the operator to each
 * individual.
 *
 * @param DNA  The type of the DNA.
 * @property probability The probability of applying this operator to each individual in the
 *      population.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 0.1.0
 * @version 2.0.0
 */
interface Alterer<DNA> {
    val probability: Double

    /**
     * Invokes the alterer on the given ``population`` for the current ``generation`` returning an
     * [AltererResult] object containing the new population and the number of individuals that were
     * recombined.
     */
    operator fun invoke(population: Population<DNA>, generation: Int): AltererResult<DNA>
}

/**
 * This is an abstract base class for implementing genetic algorithm operators that modify the
 * population by altering individuals according to a given probability.
 * It implements the [Alterer] interface and enforces that the probability is within the range of
 * 0.0 to 1.0.
 *
 * @param DNA The type of the DNA.
 * @param probability The probability of performing the alteration on each individual in the
 *      population.
 * @constructor Creates a new [AbstractAlterer].
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 0.1.0
 * @version 2.0.0
 */
abstract class AbstractAlterer<DNA>(final override val probability: Double) :
    Alterer<DNA> {
    init {
        enforce {
            probability should BeInRange(0.0..1.0) {
                "The alteration probability must be between 0 and 1"
            }
        }
    }
}

/**
 * Represents the result of applying an alteration operation to a population.
 *
 * @param population the altered population
 * @param alterations the number of alterations performed
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 0.1.0
 * @version 2.0.0
 */
class AltererResult<DNA>(
    val population: Population<DNA>,
    val alterations: Int = 0
) {
    init {
        enforce {
            alterations should BeAtLeast(0) {
                "The number of alterations cannot be negative"
            }
        }
    }

    /**
     * Returns the population.
     */
    operator fun component1()    = population

    /**
     * Returns the number of alterations.
     */
    operator fun component2() = alterations

    // Documentation inherited from Any
    override fun toString() =
        "AltererResult { population: $population, alterations: $alterations }"

    // Documentation inherited from Any
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is AltererResult<*> -> false
        population != other.population -> false
        alterations != other.alterations -> false
        else -> true
    }

    // Documentation inherited from Any
    override fun hashCode() = Objects.hash(AltererResult::class, population, alterations)
}