package cl.ravenhill.keen.examples.basics

import cl.ravenhill.keen.Builders.engine
import cl.ravenhill.keen.Builders.genotype
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.CharChromosome
import cl.ravenhill.keen.limits.TargetFitness
import cl.ravenhill.keen.operators.mutator.Mutator
import cl.ravenhill.keen.operators.crossover.SinglePointCrossover
import cl.ravenhill.keen.operators.selector.RouletteWheelSelector
import cl.ravenhill.keen.util.statistics.StatisticCollector
import cl.ravenhill.keen.util.statistics.StatisticPlotter
import cl.ravenhill.keen.util.statistics.StatisticPrinter

private const val TARGET = "Sopaipilla"

private fun matches(genotype: Genotype<Char>) = genotype.chromosomes.first().genes
    .filterIndexed { index, gene -> gene.dna == TARGET[index] }
    .size.toDouble()

fun main() {
    val engine = engine(::matches, genotype {
        chromosomes = listOf(CharChromosome.Builder(10))
    }) {
        populationSize = 500
        survivorSelector = RouletteWheelSelector()
        alterers = listOf(Mutator(0.03), SinglePointCrossover(0.06))
        limits = listOf(TargetFitness(TARGET.length.toDouble()))
        statistics = listOf(StatisticPlotter())
    }
    val evolvedPopulation = engine.run()
    println("Solution found in ${evolvedPopulation.generation} generations")
    println("Solution: ${evolvedPopulation.best?.genotype}")
    println("With fitness: ${evolvedPopulation.best?.fitness}")
}