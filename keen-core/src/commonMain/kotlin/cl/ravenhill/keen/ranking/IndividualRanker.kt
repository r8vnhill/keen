/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.ranking

import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.utils.SortingStrategy

/**
 * Interface for ranking and sorting individuals in an evolutionary algorithm based on their fitness.
 *
 * The `IndividualRanker` interface defines a contract for comparing and sorting individuals in a population according
 * to their fitness values. This interface is essential in evolutionary algorithms where the selection of parents and
 * survivors is often based on the relative fitness of individuals. The `IndividualRanker` provides a mechanism to
 * compare individuals and sort populations, ensuring that the most fit individuals can be prioritized in the evolution
 * process.
 *
 * ### Example Implementation:
 * ```kotlin
 * class FitnessMaxRanker<T, F, R> : IndividualRanker<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {
 *     override fun invoke(first: Individual<T, F, R>, second: Individual<T, F, R>): Int =
 *         second.fitness.compareTo(first.fitness)
 *     // ... other methods and properties ...
 * }
 * ```
 *
 * In this example, `FitnessMaxRanker` ranks individuals by their fitness, with higher fitness values being preferred
 * (i.e., a larger fitness value indicates a better individual).
 *
 * ## Usage:
 * The `IndividualRanker` interface is used within evolutionary algorithms to manage and manipulate populations based
 * on fitness. Implementations of this interface can be customized to rank individuals according to specific criteria,
 * depending on the goals of the algorithm.
 *
 * ### Example Usage:
 * ```kotlin
 * val ranker: IndividualRanker<MyType, MyFeature, MyRepresentation> = FitnessMaxRanker()
 * val sortedPopulation = ranker.sort(population)
 * ```
 *
 * @param T The type of the value held by the features of the individuals.
 * @param F The type of the feature used in the representation, which must extend [Feature].
 * @param R The type of the representation of individuals, which must extend [Representation].
 */
interface IndividualRanker<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {

    /**
     * A comparator that uses the ranker's comparison function to order individuals.
     *
     * This property provides a standard `Comparator` instance based on the `invoke` method, which can be used to sort
     * collections of individuals in a way that aligns with the ranker's logic.
     */
    val comparator
        get() = Comparator(::invoke)

    /**
     * Compares two individuals based on their fitness.
     *
     * @param first The first individual to compare.
     * @param second The second individual to compare.
     * @return A negative integer, zero, or a positive integer if the first individual's fitness is less than,
     *   equal to, or greater than the second individual's fitness, respectively.
     */
    operator fun invoke(first: Individual<T, F, R>, second: Individual<T, F, R>): Int

    /**
     * Transforms a list of fitness values.
     *
     * This method provides an optional transformation step that can be applied to a list of fitness values before
     * they are used for comparison or sorting. Implementations can override this method to apply custom transformations
     * such as scaling, normalization, or other adjustments to the fitness values.
     *
     * @param fitness The list of fitness values to transform.
     * @return The transformed list of fitness values.
     */
    fun fitnessTransform(fitness: List<Double>) = fitness

    /**
     * Sorts a population of individuals based on their fitness values.
     *
     * This method sorts the provided population according to their fitness values, using the ranker's comparison logic.
     * The sorting can be performed in ascending or descending order, depending on the specified [sortOrder].
     *
     * @param population The list of individuals to be sorted.
     * @param sortOrder The order in which to sort the population, either ascending or descending.
     * @return A sorted list of individuals based on their fitness values.
     */
    suspend fun sort(
        population: List<Individual<T, F, R>>,
        sortOrder: SortingStrategy = SortingStrategy.ASCENDING
    ): List<Individual<T, F, R>>

    /**
     * Returns a comparator based on the specified sorting strategy.
     *
     * The `getComparator` function provides a mechanism to retrieve a comparator that sorts elements according to a
     * given [SortingStrategy]. This is particularly useful in scenarios where the sorting order needs to be dynamically
     * chosen based on configuration or runtime conditions.
     *
     * @param sortOrder The sorting strategy to apply. It determines how the comparator should order the elements.
     * @return A `Comparator` instance that orders elements according to the specified [SortingStrategy].
     */
    fun getComparator(sortOrder: SortingStrategy) = when (sortOrder) {
        SortingStrategy.ASCENDING -> comparator
        SortingStrategy.DESCENDING -> comparator.reversed()
        SortingStrategy.UNSORTED -> Comparator { _, _ -> 0 } // Neutral comparator for UNSORTED
    }
}
