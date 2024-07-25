/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ranking

import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * A ranking strategy in evolutionary algorithms that prioritizes individuals with lower fitness values.
 *
 * `FitnessMinRanker` implements the [FitnessRanker] interface, providing a mechanism to rank individuals
 * based on their fitness scores. Unlike traditional rankers that favor higher fitness values, this ranker
 * gives preference to individuals with lower fitness values. It's useful in optimization problems where a
 * lower fitness score indicates a better solution.
 *
 * ## Usage:
 * Use this ranker in scenarios where the goal is to minimize a certain metric, such as cost, distance, or error.
 * It can be integrated into selection mechanisms, guiding the evolutionary process towards solutions with lower
 * fitness values.
 *
 * ### Example:
 * ```kotlin
 * val ranker: IndividualRanker<MyDataType, MyGene> = FitnessMinRanker()
 *
 * // Assuming a population of individuals
 * val sortedPopulation = ranker.sort(population
 * // The sortedPopulation will have individuals with lower fitness values at the beginning
 * ```
 * In this example, `FitnessMinRanker` is used to sort a population of individuals. Those with lower fitness
 * scores are ranked higher (i.e., appear earlier in the sorted list), aligning with a minimization objective.
 *
 * @param T The type of data encapsulated by the genes within the individuals.
 * @param G The type of gene in the individuals, conforming to the [Gene] interface.
 */
class FitnessMinRanker<T, G> : FitnessRanker<T, G> where G : Gene<T, G> {

    /**
     * Compares two individuals based on their fitness values, giving priority to lower fitness scores.
     *
     * In the context of evolutionary algorithms, this ranker is especially useful for scenarios where the objective is
     * to minimize a certain metric (e.g., cost, error, distance). The `FitnessMinRanker` ranks individuals such that
     * those with lower fitness values are considered superior.
     *
     * ## Operational Mechanism:
     * - The `invoke` method implements the comparison logic by taking two individuals as input.
     * - The fitness values of these individuals are compared, and the method returns a comparison result:
     *    - A positive value indicates that the second individual has a lower (and thus preferable) fitness score.
     *    - A negative value indicates that the first individual has a lower fitness score.
     *    - A zero value indicates that both individuals have equal fitness scores.
     *
     * This ranking approach is instrumental in guiding the selection process towards individuals that represent
     * potentially more optimal solutions in minimization problems.
     *
     * @param first The first individual in the comparison.
     * @param second The second individual in the comparison.
     * @return An integer value representing the comparison result. Positive if the second individual has a lower
     *   fitness score, negative if the first does, and zero if both scores are equal.
     */
    override fun invoke(first: Individual<T, G>, second: Individual<T, G>) = second.fitness.compareTo(first.fitness)

    /**
     * Transforms the fitness value to adapt it for selection mechanisms in minimization contexts.
     *
     * In selection processes like roulette wheel selection, higher fitness values typically have higher selection
     * probabilities. However, in minimization problems, where lower fitness values indicate better solutions, this
     * standard approach needs adjustment. The `fitnessTransform` method addresses this by inversely transforming the
     * fitness value, ensuring that individuals with lower original fitness scores (better solutions) receive higher
     * transformed fitness values.
     *
     * This transformation is crucial for adapting minimization problems to selection processes designed for
     * maximization, aligning the selection probability with the optimization goal of the problem.
     *
     * ## Example Usage:
     * Consider a minimization problem where fitness values represent error rates. Lower error rates are better, but
     * a standard selection process would naturally favor higher fitness values. Using `fitnessTransform`, the lower
     * error rates (lower fitness values) are transformed to higher values, making them more likely to be selected.
     *
     * This method is typically used internally by selection mechanisms to transform the fitness values of individuals
     * before calculating selection probabilities. It is not intended to be used directly in most cases.
     *
     * @param fitness The original fitness value of an individual in a minimization problem.
     * @return The transformed fitness value, higher for individuals with lower original fitness, thus adapting them
     *   for selection mechanisms that favor higher values.
     */
    override fun fitnessTransform(fitness: List<Double>): List<Double> {
        val sum = fitness.sum()
        return fitness.map { sum - it }
    }
}

