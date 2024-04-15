/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.benchmarks.stacktrace

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.genetic.genes.Gene
import kotlin.reflect.KFunction

/**
 * Represents a gene in a genetic algorithm that encapsulates a Kotlin function (`KFunction`).
 *
 * This class models a gene where the genetic value is a Kotlin function (represented by `KFunction<*>`).
 * The gene also maintains a list of potential functions (`functions`) that can be used in the genetic process.
 * It is designed to work within a genetic algorithm, providing capabilities to duplicate the gene with a new value,
 * generate random gene values, and invoke the encapsulated function.
 *
 * ## Usage:
 * ```
 * val listOfFunctions = listOf(::func1, ::func2, ::func3)
 * val gene = KFunctionGene(::func1, listOfFunctions)
 * val newGene = gene.duplicateWithValue(::func2)
 * val result = gene(listOf(arg1, arg2))
 * println(gene) // Prints the name and parameters of the function
 * ```
 * In this example, `gene` is created with a function `func1` and a list of possible functions.
 * `newGene` is a duplicate of `gene` but with `func2` as its value. `gene` is then invoked with a list of arguments.
 * Finally, the `gene` is printed, showing its function name and parameters.
 *
 * @property value The current `KFunction` value of the gene.
 * @property functions A list of `KFunction` objects that can be used to generate new gene values.
 * @property arity The number of parameters that the `value` function takes.
 */
class KFunctionGene(override val value: KFunction<*>, val functions: List<KFunction<*>>) :
    Gene<KFunction<*>, KFunctionGene> {
    val arity = value.parameters.size

    /**
     * Creates a new `KFunctionGene` with the specified `KFunction` value while retaining the same list of functions.
     */
    override fun duplicateWithValue(value: KFunction<*>) = KFunctionGene(value, functions)

    /**
     * Generates a random `KFunction` from the `functions` list.
     */
    override fun generator() = functions.random(Domain.random)

    /**
     * Invokes the `KFunction` represented by this gene with the provided arguments.
     */
    operator fun invoke(args: List<*>) = value.callBy(value.parameters.zip(args).toMap())

    /**
     * Returns a string representation of the gene, including the function's name and its parameters.
     */
    override fun toString() = value.name + value.parameters.joinToString(prefix = "(", postfix = ")") { it.name!! }
}
