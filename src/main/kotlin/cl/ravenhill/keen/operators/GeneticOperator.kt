/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.operators

import cl.ravenhill.keen.Population

/***************************************************************************************************
 * This code defines two interfaces related to genetic algorithms.
 * The GeneticOperator interface represents a genetic operator that can be applied to a population
 * of individuals to create new offspring, and provides a method to apply the operator and return
 * the resulting population.
 * The GeneticOperationResult interface represents the result of applying a genetic operator to a
 * population of individuals.
 * Both interfaces are parameterized by the type of data that represents an individual's genotype.
 **************************************************************************************************/

/**
 * Represents a genetic operator that can be applied to a population of individuals to create new
 * offspring.
 *
 * Examples of genetic operators include mutation, crossover, and selection.
 *
 * @param DNA The type of data that represents an individual's genotype.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface GeneticOperator<DNA> {
    /**
     * Applies the genetic operator to the specified population of individuals and returns a new
     * population of offspring.
     *
     * @param population The population of individuals to which the genetic operator will be
     *      applied.
     * @param generation The current generation of the population.
     * @return A new population of offspring created by applying the genetic operator to the input
     *      population.
     */
    operator fun invoke(population: Population<DNA>, generation: Int): GeneticOperationResult<DNA>
}

/**
 * Represents the result of applying a genetic operator to a population of individuals.
 *
 * @param DNA The type of data that represents an individual's genotype.
 *
 * @since 2.0.0
 * @version 2.0.0
 */
interface GeneticOperationResult<DNA>