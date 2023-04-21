package cl.ravenhill.keen.problems.ga.basics

import cl.ravenhill.keen.builders.chars
import cl.ravenhill.keen.builders.chromosome
import cl.ravenhill.keen.builders.engine
import cl.ravenhill.keen.builders.genotype
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.CharGene
import cl.ravenhill.keen.limits.TargetFitness
import cl.ravenhill.keen.operators.crossover.pointbased.SinglePointCrossover
import cl.ravenhill.keen.operators.mutator.Mutator
import cl.ravenhill.keen.operators.selector.RouletteWheelSelector
import cl.ravenhill.keen.util.statistics.StatisticPlotter
import cl.ravenhill.keen.util.statistics.StatisticPrinter

/***************************************************************************************************
 * A program that evolves a string of characters to match a target string using a genetic algorithm.
 * The fitness function is the number of characters in the genotype that match the target string.
 * The genetic algorithm uses a population size of 500, roulette wheel selection, mutation with a
 * probability of 0.03, and single point crossover with a probability of 0.06.
 * The algorithm stops when a genotype with the same length as the target string is found.
 **************************************************************************************************/

/**
 * The target string that the genetic algorithm is trying to evolve towards.
 */
private const val TARGET = "Sopaipilla"

/**
 * Computes the fitness of a genotype by counting the number of characters that match the target string.
 *
 * @param genotype The genotype to compute the fitness of.
 * @return The fitness of the genotype.
 */
private fun matches(genotype: Genotype<Char, CharGene>) = genotype.flatten()
    .filterIndexed { index, char -> char == TARGET[index] }
    .size.toDouble()

/**
 * Runs the genetic algorithm to evolve a string of characters that matches the target string.
 */
fun main() {
    val engine = engine(::matches, genotype {
        chromosome { chars { size = TARGET.length } }
    }) {
        populationSize = 500
        survivorSelector = RouletteWheelSelector()
        alterers = listOf(Mutator(0.03), SinglePointCrossover(0.06))
        limits = listOf(TargetFitness(TARGET.length.toDouble()))
        statistics = listOf(StatisticPrinter(every = 10), StatisticPlotter())
    }
    val evolvedPopulation = engine.evolve()
    println("Solution found in ${evolvedPopulation.generation} generations")
    println("Solution: ${evolvedPopulation.best.genotype}")
    println("With fitness: ${evolvedPopulation.best.fitness}")
    (engine.statistics.last() as StatisticPlotter).displayFitness()
}