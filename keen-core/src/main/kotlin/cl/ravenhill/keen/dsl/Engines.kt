/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.dsl

import cl.ravenhill.keen.evolution.engines.factories.GeneticAlgorithmFactory
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.Gene

@Deprecated("Use geneticAlgorithm instead", ReplaceWith("geneticAlgorithm(fitnessFunction, genotype, init)"))
fun <T, G> evolutionEngine(
    fitnessFunction: (Genotype<T, G>) -> Double,
    genotype: Genotype.Factory<T, G>,
    init: GeneticAlgorithmFactory<T, G>.() -> Unit,
) where G : Gene<T, G> = geneticAlgorithm(fitnessFunction, genotype, init)

fun <T, G> geneticAlgorithm(
    fitnessFunction: (Genotype<T, G>) -> Double,
    genotype: Genotype.Factory<T, G>,
    init: GeneticAlgorithmFactory<T, G>.() -> Unit,
) where G : Gene<T, G> = GeneticAlgorithmFactory(fitnessFunction, genotype).apply(init).make()
