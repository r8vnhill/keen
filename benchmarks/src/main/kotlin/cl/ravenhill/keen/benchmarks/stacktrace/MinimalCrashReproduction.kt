/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.benchmarks.stacktrace

import cl.ravenhill.keen.genetic.Individual
import kotlin.reflect.KFunction


/**
 * Represents a minimal crash reproduction scenario for a given program.
 *
 * This data class encapsulates a specific instance of an `Individual` in the context of crash reproduction.
 * It holds an `Individual` of `Statement` and `StatementGene`, which represents a program that, when executed,
 * reproduces a crash. The primary purpose of this class is to store and provide a readable representation
 * of the program that leads to a crash.
 *
 * ## Usage:
 * ```
 * val crashProgram = Individual<Statement, StatementGene>(...)
 * val crashReproduction = MinimalCrashReproduction(crashProgram)
 * println(crashReproduction) // Prints the program statements, each on a new line
 * ```
 * In this example, `crashReproduction` holds the `crashProgram` and its `toString` method provides a formatted
 * output of the program's genotype.
 *
 * @property program The program that reproduces a crash.
 */
data class MinimalCrashReproduction(val program: Individual<KFunction<*>, KFunctionGene>) {
    override fun toString() = program.genotype.joinToString("\n")
}
