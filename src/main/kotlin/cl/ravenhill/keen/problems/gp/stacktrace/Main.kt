/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.problems.gp.stacktrace

import cl.ravenhill.keen.util.statistics.StatisticCollector
import cl.ravenhill.keen.util.statistics.StatisticPlotter

fun example1(
    statCollector: List<StatisticCollector<Instruction, InstructionGene>> =
        listOf(StatisticPlotter())
) {
    val tracer1 = Tracer.create<NullPointerException>(functions0, statCollectors = statCollector)
    val mcr = tracer1.run()
    println(mcr)
}

fun main() {
    example1()
//    val tracer2 = Tracer.create<ArithmeticException>(functions0)
//    tracer2.run()
//    val tracer3 = Tracer.create<IllegalArgumentException>(functions0, "Input string must not be blank.")
//    tracer3.run()
//    val tracer4 =
//        Tracer.create<IllegalArgumentException>(functions0, functionName = "throwException1")
//    tracer4.run()
//    (tracer4.engine.statistics.last() as StatisticPlotter).displayFitness()
//    val tracer5 = Tracer.create<IllegalArgumentException>(
//        functions1 + functions0,
//        "Input number must be positive."
//    )
//    val eng = tracer5.run()
}