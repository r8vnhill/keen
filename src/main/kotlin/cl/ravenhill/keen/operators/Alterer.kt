/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators

import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.requirements.DoubleRequirement.BeInRange
import cl.ravenhill.keen.requirements.IntRequirement.BeAtLeast
import java.util.Objects

/***************************************************************************************************
 * The code represents an Alterer, a type of genetic operator that alters a population of
 * individuals by applying some form of alteration such as mutation or crossover.
 * The Alterer interface extends the GeneticOperator interface and adds a probability property
 * representing the likelihood of an alteration being applied to an individual in the population.
 * The interface defines an operator function that applies the Alterer to a population of
 * individuals and returns an AltererResult, containing the resulting population of individuals and
 * the count of how many individuals were altered.
 * The AbstractAlterer class is an abstract base class for implementing genetic algorithm operators
 * that modify the population by altering individuals according to a given probability.
 * It enforces the probability is between 0.0 and 1.0.
 * The AltererResult class represents the result of applying an alteration operation to a
 * population, containing the altered population and the number of alterations performed.
 **************************************************************************************************/

/**
 * Represents an alterer, which is a type of genetic operator that modifies a population of
 * individuals by applying some form of alteration, such as mutation or crossover.
 *
 * @param DNA The type of data that represents an individual's genotype.
 *
 * @property probability the probability that an alteration will be applied to an individual in the
 *      population.
 */
interface Alterer<DNA, G : Gene<DNA, G>> : GeneticOperator<DNA, G> {

    val probability: Double

    /**
     * Applies the alterer to the specified population of individuals and returns an AltererResult,
     * which contains the resulting population of individuals and a count of how many individuals
     * were altered.
     *
     * @param population The population of individuals to which the alterer will be applied.
     * @param generation The current generation of the population.
     * @return An AltererResult, which contains the resulting population of individuals and a count
     *      of how many individuals were altered.
     */
    override operator fun invoke(
        population: Population<DNA, G>,
        generation: Int
    ): AltererResult<DNA, G>
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
abstract class AbstractAlterer<DNA, G : Gene<DNA, G>>(final override val probability: Double) :
        Alterer<DNA, G> {
    init {
        enforce {
            "The alteration probability [$probability] must be between 0.0 and 1.0" {
                probability must BeInRange(0.0..1.0)
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
class AltererResult<DNA, G : Gene<DNA, G>>(
    val population: Population<DNA, G>,
    val alterations: Int = 0
) : GeneticOperationResult<DNA> {
    init {
        enforce {
            "The number of alterations [$alterations] must be greater than or equal to 0" {
                alterations must BeAtLeast(0)
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

    // Documentation inherited from Any
    override fun toString() =
        "AltererResult { population: $population, alterations: $alterations }"

    // Documentation inherited from Any
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is AltererResult<*, *> -> false
        population != other.population -> false
        alterations != other.alterations -> false
        else -> true
    }

    // Documentation inherited from Any
    override fun hashCode() = Objects.hash(AltererResult::class, population, alterations)
}