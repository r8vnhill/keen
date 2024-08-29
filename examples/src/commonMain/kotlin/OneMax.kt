/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

import cl.ravenhill.keen.dsl.booleans
import cl.ravenhill.keen.dsl.chromosomeOf
import cl.ravenhill.keen.dsl.geneticAlgorithm
import cl.ravenhill.keen.dsl.genotypeOf
import cl.ravenhill.keen.evolution.executors.construction.CoroutineConcurrentConstructor
import cl.ravenhill.keen.fitness
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.genes.BooleanGene
import cl.ravenhill.keen.limits.maxGenerations
import cl.ravenhill.keen.limits.targetFitness
import cl.ravenhill.keen.operators.alteration.crossover.UniformCrossover
import cl.ravenhill.keen.operators.alteration.mutation.BitFlipMutator
import cl.ravenhill.keen.operators.selection.RouletteWheelSelector

private fun count(genotype: Genotype<Boolean, BooleanGene>) = genotype.flatten().count { it }.toDouble()

suspend fun oneMax() {
    val engine = geneticAlgorithm(
        ::count,
        genotypeOf {
            chromosomeOf {
                booleans {
                    size = 50
                    trueRate = 0.15
                    executor = CoroutineConcurrentConstructor()
                }
            }
        }
    ) {
        populationSize = 500
        parentSelector = RouletteWheelSelector()
        survivorSelector = RouletteWheelSelector()
        alterers += listOf(BitFlipMutator(), UniformCrossover(chromosomeRate = 0.6))
        limits += listOf(maxGenerations(100), targetFitness(50.0))
    }
    engine.evolve()
        .population
        .fitness
        .maxOrNull()
        .let { println("Best fitness: $it") }
}
