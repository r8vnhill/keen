/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */
package cl.ravenhill.keen.examples.ga.knapsack

import cl.ravenhill.keen.builders.booleans
import cl.ravenhill.keen.builders.chromosome
import cl.ravenhill.keen.builders.engine
import cl.ravenhill.keen.builders.genotype
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.BoolGene
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.operators.crossover.pointbased.SinglePointCrossover
import cl.ravenhill.keen.operators.mutator.strategies.BitFlipMutator
import cl.ravenhill.keen.operators.mutator.strategies.RandomMutator
import cl.ravenhill.keen.util.listeners.EvolutionPlotter
import cl.ravenhill.keen.util.listeners.EvolutionPrinter
import cl.ravenhill.keen.util.listeners.EvolutionSummary

/**
 * The maximum weight that the knapsack can hold.
 */
private const val MAX_WEIGHT = 1

/**
 * The possible items that can be put in the knapsack.
 */
private val items =
    listOf(11 to 1)

/**
 * The fitness function for the 0-1 knapsack problem.
 * It calculates the fitness of a given genotype by summing the values of the items in the knapsack.
 * If the weight of the knapsack is greater than the maximum weight, the fitness is reduced by the
 * difference between the weight and the maximum weight.
 *
 * @param genotype The genotype to calculate the fitness for.
 * @return The fitness of the genotype.
 */
private fun fitnessFn(genotype: Genotype<Boolean, BoolGene>): Double {
    val profit = (genotype.flatMap() zip items).sumOf { (isInBag, item) ->
        if (isInBag) item.first else 0
    }
    val weight = (genotype.flatMap() zip items).sumOf { (isInBag, item) ->
        if (isInBag) item.second else 0
    }
    val penalty = if (weight > MAX_WEIGHT) weight - MAX_WEIGHT else 0
    return (profit - penalty).toDouble()
}

/**
 * Implements a genetic algorithm to solve the 0-1 knapsack problem.
 *
 * The 0-1 knapsack problem is a classic optimization problem in computer science where a set of
 * items with certain weights and values are given, and a knapsack with a maximum weight capacity is
 * provided.
 * The goal is to determine the subset of items that maximize the value without exceeding the
 * maximum weight capacity.
 *
 * The genetic algorithm implemented in the main function involves creating a population of
 * candidate solutions, each represented by a genotype.
 * The genotype is evaluated using the fitness function, which calculates the value of the items in
 * the knapsack and reduces the fitness if the weight of the knapsack exceeds the maximum weight
 * capacity.
 * The genetic algorithm iteratively improves the solutions in the population by using selection,
 * crossover, and mutation operators.
 * The process is repeated until the stopping criteria are met, such as a maximum number of
 * generations or the fitness of the best solution reaches a certain threshold.
 *
 * The complexity of the 0-1 knapsack problem is exponential, which means that the brute-force
 * approach to solving the problem becomes infeasible for larger sets of items.
 * The genetic algorithm approach implemented in this function provides a heuristic solution that
 * may not always find the optimal solution but can converge to a good solution in a reasonable
 * amount of time.
 *
 * The main function uses the engine function provided by the Keen library to set up and run the
 * genetic algorithm.
 * It initializes the genetic algorithm with a population size of 10 and applies two alterers:
 * [RandomMutator] and [SinglePointCrossover].
 * It also sets the limit to 20 steady generations, meaning that the algorithm stops after 20
 * generations with no improvement in the fitness of the best solution.
 * The statistics of the genetic algorithm are collected using [EvolutionSummary] and plotted
 * using [EvolutionPlotter].
 *
 * Finally, the main function prints the statistics of the genetic algorithm, displays the items
 * selected by the best solution, and displays a plot of the fitness of the population over the
 * generations.
 */
fun main() {
    val engine = engine(
        ::fitnessFn,
        genotype {
            chromosome { booleans { size = items.size; truesProbability = 0.5 } }
        }
    ) {
        populationSize = 50
        alterers = listOf(BitFlipMutator(0.03), SinglePointCrossover(0.06))
        limits = listOf(GenerationCount(100))
        listeners += listOf(EvolutionSummary(), EvolutionPrinter(5), EvolutionPlotter())
    }
    val result = engine.evolve()
    println(engine.listeners.first())
    println(
        "Selected: ${
            result.best.flatMap()
                .mapIndexedNotNull { index, b -> if (b) items[index] else null }
                .joinToString { (value, weight) -> "($value, $weight)" }
        }"
    )
    (engine.listeners.last() as EvolutionPlotter).displayFitness()
}
