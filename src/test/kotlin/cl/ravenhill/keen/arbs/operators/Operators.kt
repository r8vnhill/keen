/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arbs.operators

import io.kotest.property.Arb
import io.kotest.property.arbitrary.choice

fun Arb.Companion.intAlterer() = choice(intCrossover())