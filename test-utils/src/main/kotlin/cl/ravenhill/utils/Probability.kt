/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.utils

import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter

fun arbProbability() = Arb.double(0.0..1.0, includeNonFiniteEdgeCases = false)

fun arbInvalidProbability() = Arb.double().filter { it < 0.0 || it > 1.0 }
