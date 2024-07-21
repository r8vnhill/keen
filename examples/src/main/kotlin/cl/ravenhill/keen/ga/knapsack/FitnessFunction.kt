/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ga.knapsack

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.BooleanGene
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

/**
 * Calculates the fitness value of a given genotype for the Zero-One Knapsack Problem.
 *
 * This function evaluates the fitness of a genotype, representing a possible solution to the Zero-One Knapsack Problem.
 * In this problem, each item can either be included in the knapsack or left out, indicated by a Boolean value in the
 * genotype. The fitness is calculated based on the total profit of items included in the knapsack, adjusted by a
 * penalty if the total weight of these items exceeds the maximum allowable weight
 * ([ZeroOneKnapsackProblem.MAX_WEIGHT]).
 *
 * ## Functionality:
 * - The genotype, representing a binary selection (inclusion or exclusion) of items, is paired with the actual items.
 * - The total profit (`profit`) is calculated as the sum of the values (profits) of items that are included in the
 *   knapsack.
 * - The total weight (`weight`) is the sum of the weights of items that are included.
 * - A penalty is applied if the total weight exceeds `MAX_WEIGHT`. The penalty is the excess weight over `MAX_WEIGHT`.
 * - The fitness is calculated as the total profit minus any weight penalty.
 *
 * @param genotype The genotype to be evaluated, consisting of `BooleanGene` objects where `true` indicates inclusion
 *   of an item in the knapsack.
 * @return The fitness value of the genotype, calculated based on the total profit of included items, adjusted for
 *   penalties due to excess weight.
 */
fun ZeroOneKnapsackProblem.fitnessFunction(genotype: Genotype<Boolean, BooleanGene>): Double {
    val profit = genotype.flatten().zip(items).sumOf { (isInBag, item) ->
        if (isInBag) item.first else 0
    }
    val weight = genotype.flatten().zip(items).sumOf { (isInBag, item) ->
        if (isInBag) item.second else 0
    }
    val penalty = if (weight > MAX_WEIGHT) weight - MAX_WEIGHT else 0
    return (profit - penalty).toDouble()
}
