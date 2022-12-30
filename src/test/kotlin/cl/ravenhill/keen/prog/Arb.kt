package cl.ravenhill.keen.prog

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.prog.functions.Add
import cl.ravenhill.keen.prog.functions.GreaterThan
import cl.ravenhill.keen.prog.functions.If
import cl.ravenhill.keen.prog.terminals.ephemeralConstant
import cl.ravenhill.keen.prog.terminals.variable
import cl.ravenhill.keen.util.program
import cl.ravenhill.keen.util.subset
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.positiveInt


/**
 * Generates a reduceable expression.
 */
fun Arb.Companion.program() = arbitrary { rs ->
    val terminals = Arb.terminals().bind()
    val functions = Arb.functions().bind()
    rs.random.program(Core.maxProgramDepth - 1, functions, terminals)
}

/**
 * Generates a list of functions
 */
fun Arb.Companion.functions() = arbitrary {
    val operations = listOf(Add(), GreaterThan(), If())
    Arb.subset(operations).bind()
}

/**
 * Generates a list of terminals randomly picked.
 */
fun Arb.Companion.terminals() =
    arbitrary {
        // We generate a set of constants
        val constants = List(Arb.positiveInt(20).bind()) {
            Arb.ephemeralConstant().bind()
        }
        // We generate a set of variables
        val variables = List(Arb.positiveInt(20).bind()) {
            Arb.variable().bind()
        }
        // We select a subset of elements from the constants and variables
        Arb.subset(constants + variables).bind()
    }

/**
 * Generates an arbitrary subset of elements from the given list.
 */
fun <T> Arb.Companion.subset(collection: List<T>) = arbitrary { rs ->
    // We generate a random number of elements to pick
    Arb.positiveInt(collection.size).bind().let {
        Core.random = rs.random
        // We pick a random subset of `it` elements
        collection.subset(it)
    }
}
