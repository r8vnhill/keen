/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.arbs

import cl.ravenhill.keen.arbs.genetic.population
import cl.ravenhill.keen.operators.AltererResult
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.nonNegativeInt

/**
 * Provides an arbitrary generator for creating random [AltererResult] instances.
 *
 * This extension function facilitates the generation of random [AltererResult] objects,
 * which consist of a population of individuals and a non-negative integer value.
 *
 * @receiver Arb.Companion The companion object of the arbitrary type, enabling this to be an extension function.
 *
 * @return An [Arb] instance that produces random [AltererResult]s made up of populations with individuals and non-negative integers.
 */
fun Arb.Companion.altererResult() = arbitrary {
    AltererResult(population().bind(), nonNegativeInt().bind())
}

