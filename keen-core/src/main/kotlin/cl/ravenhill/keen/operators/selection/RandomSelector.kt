/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.selection

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.ranking.IndividualRanker

/**
 * A selector implementation for random selection in evolutionary algorithms.
 *
 * `RandomSelector` is a class that implements a selection strategy based on random choice. Unlike other selection
 * methods that might take fitness into account, this selector assigns an equal probability of selection to each
 * individual in the population, regardless of their fitness levels. This approach is akin to drawing names from a hat.
 *
 * ## Usage:
 * This selector can be used in scenarios where unbiased random selection is desired, such as in certain stages of
 * genetic algorithms or to benchmark other selection strategies.
 *
 * ### Example:
 * ```kotlin
 * val state: EvolutionState<MyDataType, MyGene> = /* Initialize the evolution state */
 * val randomSelector = RandomSelector<MyDataType, MyGene>()
 * val selectedIndividuals = randomSelector(state, 10)
 * ```
 * In this example, `RandomSelector` is used to randomly select individuals from a population, disregarding their
 * fitness levels.
 *
 * @param T The type of data encapsulated by the genes within the individuals.
 * @param G The type of gene in the individuals, conforming to the [Gene] interface.
 * @property sorted A boolean indicating whether the population should be sorted based on the ranking before
 *   selection. In the case of `RandomSelector`, this is always `false` as sorting is not necessary for random
 *   selection.
 */
class RandomSelector<T, G> : Selector<T, G> where G : Gene<T, G> {

    /**
     * Selects a specified number of individuals randomly from a population.
     *
     * This function overrides the select method in the Selector interface, providing a random selection mechanism.
     * Each individual in the population has an equal chance of being selected, irrespective of their fitness levels.
     * This method is useful in scenarios where a non-biased, random sample from the population is needed.
     *
     * ## Usage:
     * This method is typically used in benchmarking scenarios, where the performance of other selection strategies
     * is compared to random selection.
     *
     * ### Example:
     * ```kotlin
     * val population: Population<MyDataType, MyGene> = /* Initialize population */
     * val randomSelector = RandomSelector<MyDataType, MyGene>()
     * val selectedIndividuals = randomSelector.select(population, 5, /* ranker implementation */)
     * ```
     * In this example, 5 individuals are randomly selected from the population using the `RandomSelector`.
     *
     * @param population The population from which individuals are to be selected.
     * @param count The number of individuals to select.
     * @param ranker An instance of `IndividualRanker` used for ranking the individuals.
     *   In the context of random selection, the ranker's functionality is not utilized.
     * @return A list of randomly selected individuals from the population.
     */
    override fun select(population: Population<T, G>, count: Int, ranker: IndividualRanker<T, G>) =
        List(count) { population.random(Domain.random) }
}
