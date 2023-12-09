/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ranking

import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * Interface defining a mechanism for ranking individuals in an evolutionary algorithm.
 *
 * `IndividualRanker` is crucial in evolutionary algorithms for determining the relative performance or suitability of
 * individuals within a population. It utilizes a custom comparator to rank individuals based on specific criteria, such
 * as fitness, genetic diversity, or any other metric relevant to the problem being solved.
 *
 * ## Usage:
 * Implement this interface to define a custom ranking strategy for individuals in a population. The ranking
 * mechanism can significantly influence the direction and effectiveness of the evolutionary process, especially
 * during selection phases.
 *
 * ### Example:
 * Implementing a simple fitness-based ranker:
 * ```kotlin
 * class FitnessRanker : IndividualRanker<MyGeneType, MyGene> {
 *     override fun compare(first: Individual<MyGeneType, MyGene>, second: Individual<MyGeneType, MyGene>): Int {
 *         return first.fitness.compareTo(second.fitness)
 *     }
 * }
 *
 * val population = listOf(/* ... Individuals ... */)
 * val ranker = FitnessRanker()
 * val sortedPopulation = ranker.sort(population) // Sorts the population based on fitness
 * ```
 * In this example, `FitnessRanker` ranks individuals based on their fitness scores, with higher fitness
 * being considered superior.
 *
 * @param T The type of data encapsulated by the genes in the individuals.
 * @param G The specific type of gene present in the individuals.
 *
 * @property comparator A [Comparator] used for ranking individuals. It internally uses the [invoke] method
 *   to define the sorting logic.
 *
 * @see Comparator for details on the comparator used for sorting.
 * @see Individual for the entity being ranked.
 *
 * @author <https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface IndividualRanker<T, G> where G : Gene<T, G> {
    val comparator
        get() = Comparator(::invoke)

    /**
     * Compares two individuals and determines their relative order based on a predefined criterion.
     *
     * This method is fundamental to the ranking process in evolutionary algorithms, as it dictates how individuals are
     * compared and ordered within a population. The comparison is based on specific criteria, which could include
     * fitness, genetic diversity, or other custom metrics relevant to the problem domain.
     *
     * The method returns an integer indicating the relative ranking between the two individuals:
     * - A negative integer implies that the first individual (`first`) ranks higher than the second (`second`).
     * - A positive integer indicates that the second individual ranks higher than the first.
     * - Zero signifies that both individuals are considered equal in terms of the ranking criteria.
     *
     * ## Implementation:
     * Implementations of this method should define the logic for comparing two individuals. This logic can vary
     * depending on the objectives of the evolutionary algorithm and the characteristics of the individuals.
     *
     * ## Example:
     * Implementing a comparison based on fitness values:
     * ```kotlin
     * override fun invoke(first: Individual<MyGeneType, MyGene>, second: Individual<MyGeneType, MyGene>): Int {
     *     return first.fitness.compareTo(second.fitness) // Higher fitness implies a higher rank
     * }
     * ```
     * In this example, individuals are compared based on their fitness values, where a higher fitness value
     * indicates a higher rank.
     *
     * @param first The first individual in the comparison. Its relative ranking is determined against the second
     *   individual.
     * @param second The second individual to be compared with the first.
     * @return An integer indicating the relative ranking of the two individuals based on the comparison criteria.
     *   Negative for the first individual ranking higher, positive for the second individual ranking higher, and zero
     *   for equal ranking.
     */
    operator fun invoke(first: Individual<T, G>, second: Individual<T, G>): Int


    /**
     * Sorts a population of individuals in descending order based on their fitness or other criteria.
     *
     * This method is essential in evolutionary algorithms for ranking individuals according to their fitness or a
     * custom comparison criterion. Sorting the population helps in selecting the fittest individuals for reproduction
     * and guiding the evolutionary process towards optimal solutions. The sorting is performed in descending order,
     * meaning that individuals with higher fitness or preferred characteristics (as defined by the comparator)
     * are placed at the beginning of the list.
     *
     * ## Functionality:
     * - Utilizes the `comparator` property to determine the order of individuals.
     * - Sorts the population in descending order, with the "best" individuals (according to the comparator) appearing
     *   first.
     *
     * ## Usage:
     * This method can be invoked to sort a population before selection processes like tournament selection, truncation
     * selection, or when evaluating the overall progress of the evolutionary algorithm.
     *
     * ### Example:
     * Sorting a population based on fitness:
     * ```kotlin
     * val sortedPopulation = ranker.sort(population)
     * // The sortedPopulation now has individuals ordered from highest to lowest fitness
     * ```
     * In this example, `sort` arranges the individuals in the population from the highest to the lowest fitness,
     * allowing for easy identification of the top performers in the current generation.
     *
     * @param population The list of individuals to be sorted. It represents the current population in an evolutionary
     *   algorithm.
     * @return A new list of individuals sorted in descending order based on the criteria defined by the comparator.
     */
    fun sort(population: Population<T, G>) = population.sortedWith(comparator.reversed())

    /**
     * Transforms a list of fitness values of individuals.
     *
     * This method is designed to apply a transformation to the fitness values of individuals within a population. It
     * receives a list of fitness values and returns a corresponding list of transformed values. The purpose of this
     * transformation is to adjust fitness values in accordance with specific criteria or objectives of an evolutionary
     * algorithm. This can include normalization, scaling, or inversion of values, depending on the context and the
     * desired selection dynamics.
     *
     * Implementations of this method should provide a clear rationale for the transformation logic, particularly in
     * scenarios where the selection mechanism or optimization goal necessitates a non-standard treatment of fitness
     * values. For instance, in minimization problems, a typical transformation might invert the fitness values, so
     * that lower original values (indicating better solutions) translate to higher values for selection purposes.
     *
     * @param fitness A list of original fitness values of individuals in the population.
     * @return A list of transformed fitness values, corresponding to each original value in the input list.
     */
    fun fitnessTransform(fitness: List<Double>) = fitness
}
