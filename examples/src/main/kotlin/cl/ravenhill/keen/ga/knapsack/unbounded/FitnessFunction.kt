/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ga.knapsack.unbounded

import cl.ravenhill.keen.genetic.Genotype
import kotlin.math.abs

/**
 * Calculates the fitness value of a given genotype in the context of the unbounded knapsack problem.
 *
 * This function computes the fitness of a genotype (a possible solution) in the unbounded knapsack problem. The fitness
 * is determined based on the total value of items in the genotype, adjusted by a penalty if the total weight exceeds
 * the maximum allowable weight ([UnboundedKnapsackProblem.MAX_WEIGHT]).
 *
 * ## Functionality:
 * - The genotype, representing a collection of knapsack items, is first flattened to get a list of item pairs.
 * - The total value (`value`) is calculated as the sum of the first elements (values) of these item pairs.
 * - The total weight (`weight`) is calculated as the sum of the second elements (weights) of the item pairs.
 * - The fitness is primarily the total value of items in the genotype.
 * - If the genotype does not satisfy the problem constraints (verified by `genotype.verify()`), a penalty is applied.
 *   The penalty is the absolute difference between `MAX_WEIGHT` and `weight`, multiplied by a predefined penalty
 *   multiplier ([UnboundedKnapsackProblem.PENALTY_MULTIPLIER]).
 * - If the genotype meets the problem constraints, no penalty is applied.
 *
 * ## Usage:
 * This function is used to evaluate how well a particular genotype solves the unbounded knapsack problem. It is a
 * crucial part of the genetic algorithm's selection process, as genotypes with higher fitness values are more likely to
 * be selected for reproduction.
 *
 * @param genotype The genotype to be evaluated, consisting of `KnapsackGene` objects.
 * @return The fitness value of the genotype, calculated based on the total value of items and adjusted by a penalty for
 *         exceeding the maximum weight limit.
 */
fun UnboundedKnapsackProblem.fitnessFunction(genotype: Genotype<Pair<Int, Int>, KnapsackGene>): Double {
    val items = genotype.flatten()
    val value = items.sumOf { it.first }
    val weight = items.sumOf { it.second }
    return value - if (!genotype.verify()) {
        abs(MAX_WEIGHT - weight).toDouble() * PENALTY_MULTIPLIER
    } else {
        0.0
    }
}
