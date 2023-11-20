/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.examples.ga.basics

import cl.ravenhill.keen.builders.chars
import cl.ravenhill.keen.builders.chromosome
import cl.ravenhill.keen.builders.engine
import cl.ravenhill.keen.builders.genotype
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.CharGene
import cl.ravenhill.keen.limits.TargetFitness
import cl.ravenhill.keen.operators.crossover.pointbased.SinglePointCrossover
import cl.ravenhill.keen.operators.mutator.strategies.RandomMutator
import cl.ravenhill.keen.operators.selector.RouletteWheelSelector
import cl.ravenhill.keen.util.listeners.EvolutionPrinter
import cl.ravenhill.keen.util.listeners.EvolutionSummary

object GuessWordProblem {
    private const val TARGET = "Sopaipilla"
    private fun matches(genotype: Genotype<Char, CharGene>) = genotype.flatMap()
        .filterIndexed { index, char -> char == TARGET[index] }
        .size.toDouble()

    fun run() {
        val engine = engine(
            ::matches,
            genotype {
                chromosome { chars { size = TARGET.length } }
            }
        ) {
            populationSize = 500
            survivorSelector = RouletteWheelSelector()
            alterers = listOf(RandomMutator(0.06), SinglePointCrossover(0.2))
            limits = listOf(TargetFitness(TARGET.length.toDouble()))
            listeners += listOf(EvolutionSummary(), EvolutionPrinter(10))
        }
        val evolvedPopulation = engine.evolve()
        println("Solution found in ${evolvedPopulation.generation} generations")
        println("Solution: ${evolvedPopulation.best.genotype}")
        println("With fitness: ${evolvedPopulation.best.fitness}")
        println("${engine.listeners[1]}")
    }
}

fun main() = GuessWordProblem.run()