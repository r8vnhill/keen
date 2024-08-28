/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.dsl.booleans
import cl.ravenhill.keen.dsl.chromosomeOf
import cl.ravenhill.keen.dsl.geneticAlgorithm
import cl.ravenhill.keen.dsl.genotypeOf
import cl.ravenhill.keen.evolution.executors.construction.CoroutineConcurrentConstructor
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.genes.BooleanGene
import cl.ravenhill.keen.operators.selection.RouletteWheelSelector
import kotlin.time.TimeSource

private fun count(genotype: Genotype<Boolean, BooleanGene>) = genotype.flatten().count { it }.toDouble()

fun oneMax() {
    val engine1 = geneticAlgorithm(
        ::count,
        genotypeOf {
            chromosomeOf {
                booleans {
                    size = 5_000_000
                    trueRate = 0.15
                    executor = CoroutineConcurrentConstructor()
                }
            }
        }
    ) {
        populationSize = 500
        parentSelector = RouletteWheelSelector()
        survivorSelector = RouletteWheelSelector()

    }
}
