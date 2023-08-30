/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.examples.gp.stacktrace

import cl.ravenhill.keen.util.listeners.EvolutionListener
import cl.ravenhill.keen.util.listeners.EvolutionPlotter
import cl.ravenhill.keen.util.listeners.Listeners

/**
 * This function demonstrates the use of the [Tracer] class in the Keen library to evolve a program
 * that raises a [NullPointerException].
 *
 * The function takes in an optional parameter `statCollector`, which defaults to a list containing a
 * [EvolutionPlotter] instance.
 * The [Tracer] is then initialized with the input `statCollector` and is used to run the genetic
 * algorithm.
 *
 * The function returns a pair of the [MinimalCrashReproduction] object and the [Tracer] instance.
 *
 * ## Examples
 * ### Example 1: Run the function with a list of collectors
 * ```
 * val (mcr, tracer) = example1(listOf(StatisticPlotter(), StatisticPrinter(10)))
 * println(mcr)
 * (tracer.engine.statistics.first() as StatisticPlotter).displayFitness()
 * ```
 *
 * @param statCollector The list of [EvolutionListener] instances to be used by the `Tracer`.
 * Defaults to a list containing a [EvolutionPlotter] instance.
 * @return A pair of [MCR] and `Tracer<NullPointerException>` instances.
 */
private fun example1(
    statCollector: Listeners<Instruction, InstructionGene> = listOf(EvolutionPlotter())
): Pair<MCR, Tracer<NullPointerException>> {
    val tracer = Tracer.create<NullPointerException>(functions0, statCollectors = statCollector)
    val mcr = tracer.run()
    return mcr to tracer
}

private fun example2(
    statCollector: Listeners<Instruction, InstructionGene> = listOf(EvolutionPlotter())
): Pair<MCR, Tracer<ArithmeticException>> {
    val tracer =
        Tracer.create<ArithmeticException>(functions0 + functions1, statCollectors = statCollector)
    val mcr = tracer.run()
    return mcr to tracer
}

private fun example3(
    statCollectors: Listeners<Instruction, InstructionGene> = listOf(EvolutionPlotter())
): Pair<MCR, Tracer<IllegalArgumentException>> {
    val tracer = Tracer.create<IllegalArgumentException>(
        functions0 + functions1,
        "Input string must not be blank.",
        statCollectors = statCollectors
    )
    val mcr = tracer.run()
    return mcr to tracer
}

private fun example4(
    statCollectors: Listeners<Instruction, InstructionGene> = listOf(EvolutionPlotter())
): Pair<MCR, Tracer<IllegalArgumentException>> {
    val tracer =
        Tracer.create<IllegalArgumentException>(
            functions0 + functions1,
            functionName = "throwException1",
            statCollectors = statCollectors
        )
    val mcr = tracer.run()
    return mcr to tracer
}

private fun example5(
    statCollectors: Listeners<Instruction, InstructionGene> = listOf(EvolutionPlotter())
): Pair<MCR, Tracer<IllegalArgumentException>> {
    val tracer = Tracer.create<IllegalArgumentException>(
        functions1 + functions0,
        "Input number must be positive.",
        populationSize = 8,
        statCollectors = statCollectors
    )
    val mcr = tracer.run()
    return mcr to tracer
}

private fun example6(
    statCollectors: Listeners<Instruction, InstructionGene> = listOf(EvolutionPlotter())
): Pair<MCR, Tracer<ArrayIndexOutOfBoundsException>> {
    val tracer = Tracer.create<ArrayIndexOutOfBoundsException>(
        functions0 + functions1 + functions2,
        "Index must be less than array size.",
        statCollectors = statCollectors
    )
    val mcr = tracer.run()
    return mcr to tracer
}

fun main() {
    val (mcr, tracer) = example6()
    println(mcr)
    (tracer.engine.listeners.first() as EvolutionPlotter).displayFitness()
}
