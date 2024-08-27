/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.dsl

import cl.ravenhill.keen.evolution.engines.GeneticAlgorithmFactory
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.GenotypeFactory
import cl.ravenhill.keen.genetics.genes.Gene

fun <T, G> geneticAlgorithm(
    fitnessFunction: (Genotype<T, G>) -> Double,
    genotype: GenotypeFactory<T, G>,
    init: GeneticAlgorithmFactory<T, G>.() -> Unit,
) where G : Gene<T, G> = GeneticAlgorithmFactory(fitnessFunction, genotype).apply(init).make()
