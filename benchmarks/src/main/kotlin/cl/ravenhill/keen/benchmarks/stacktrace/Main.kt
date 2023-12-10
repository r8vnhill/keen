/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.benchmarks.stacktrace

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.listeners.EvolutionPlotter
import cl.ravenhill.keen.listeners.EvolutionSummary
import cl.ravenhill.keen.operators.alteration.crossover.CombineCrossover
import cl.ravenhill.keen.operators.alteration.crossover.Crossover
import cl.ravenhill.keen.operators.alteration.crossover.SinglePointCrossover
import cl.ravenhill.keen.operators.alteration.mutation.InversionMutator
import cl.ravenhill.keen.operators.alteration.mutation.Mutator
import cl.ravenhill.keen.operators.alteration.mutation.RandomMutator
import cl.ravenhill.keen.operators.alteration.mutation.SwapMutator
import kotlin.reflect.KFunction


fun example1(mutator: Mutator<KFunction<*>, KFunctionGene>, crossover: Crossover<KFunction<*>, KFunctionGene>) {
    val summary = EvolutionSummary<KFunction<*>, KFunctionGene>()
    val plotter = EvolutionPlotter<KFunction<*>, KFunctionGene>()
    val tracer = Tracer.create<IllegalArgumentException>(
        functions0 + functions1 + functions2,
        targetMessage = "The number is greater than 100",
        mutator = mutator,
        crossover = crossover,
        listeners = listOf(summary, plotter)
    )
    val result = tracer.run()
    summary.display()
    println(result)
    plotter.display()
}

fun example2(mutator: Mutator<KFunction<*>, KFunctionGene>, crossover: Crossover<KFunction<*>, KFunctionGene>) {
    val summary = EvolutionSummary<KFunction<*>, KFunctionGene>()
    val plotter = EvolutionPlotter<KFunction<*>, KFunctionGene>()
    val tracer = Tracer.create<IllegalArgumentException>(
        functions0 + functions1 + functions2,
        targetFunction = listOf("throwIAE"),
        mutator = mutator,
        listeners = listOf(summary, plotter),
        crossover = crossover
    )
    val result = tracer.run()
    summary.display()
    println(result)
    plotter.display()
}

fun main() {
//    example1(RandomMutator(individualRate = 0.2), SinglePointCrossover(chromosomeRate = 0.5))
//    example1(RandomMutator(individualRate = 0.2), CombineCrossover({ it.random(Domain.random)}))
//    example1(SwapMutator(individualRate = 0.3), SinglePointCrossover(chromosomeRate = 0.5))
//    example1(SwapMutator(individualRate = 0.3), CombineCrossover({ it.random(Domain.random)}))
//    example1(InversionMutator(individualRate = 0.3), SinglePointCrossover(chromosomeRate = 0.5))
//    example1(InversionMutator(individualRate = 0.3), CombineCrossover({ it.random(Domain.random)}))
//    example2(RandomMutator(individualRate = 0.2), SinglePointCrossover(chromosomeRate = 0.5))
//    example2(RandomMutator(individualRate = 0.2), CombineCrossover({ it.random(Domain.random)}))
//    example2(SwapMutator(individualRate = 0.3), SinglePointCrossover(chromosomeRate = 0.5))
    example2(SwapMutator(individualRate = 0.3), CombineCrossover({ it.random(Domain.random)}))
//    example2(InversionMutator(individualRate = 0.3), SinglePointCrossover(chromosomeRate = 0.5))
//    example2(InversionMutator(individualRate = 0.3), CombineCrossover({ it.random(Domain.random)}))
}