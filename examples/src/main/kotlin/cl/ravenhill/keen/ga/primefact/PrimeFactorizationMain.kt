/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ga.primefact

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.chromosomes.numeric.IntChromosome
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import cl.ravenhill.keen.listeners.plotter.EvolutionPlotter
import cl.ravenhill.keen.listeners.printer.evolutionPrinter
import cl.ravenhill.keen.listeners.summary.EvolutionSummary
import cl.ravenhill.keen.operators.alteration.mutation.GeneMutator
import kotlin.math.abs

/**
 * Implementation of the Prime Factorization Problem using a genetic algorithm.
 *
 * This program aims to find the prime factors of a given target number (420) by evolving a population of potential
 * solutions using genetic algorithms. The fundamental theorem of arithmetic states that every integer greater than 1
 * is either a prime number itself or can be factorized into prime numbers, which are unique for each number.
 *
 * ## Genetic Algorithm Setup:
 * The genetic algorithm is configured with a custom genotype comprising integers. These integers are restricted to the
 * set of potential factors, ensuring that only viable candidates are considered for factorization. The algorithm
 * evolves the population towards a solution where the product of genes in the fittest individual equals the target
 * number.
 *
 * ## Usage:
 * The `main` function sets up and runs the genetic algorithm. It evolves the population while applying genetic
 * operations like mutation and crossover. The evolution process is subject to constraints such as reaching a fitness
 * target or a steady state. The result is displayed, showing the factorization of the target number.
 */
fun main() {
    val engine = PrimeFactorizationProblem(
        ::EvolutionSummary,
        ::EvolutionPlotter,
        evolutionPrinter(every = 10)
    )
    val summary = engine.listeners.filterIsInstance<EvolutionSummary<Int, IntGene>>().first()
    val plotter = engine.listeners.filterIsInstance<EvolutionPlotter<Int, IntGene>>().first()
    summary.display()
    println(
        buildString {
            append("Solution: ")
            append(summary.fittest.genotype.flatten().filter { it > 1 }.joinToString(" * "))
            append(" = ${PrimeFactorizationProblem.TARGET}")
        }
    )
    plotter.display()
}
