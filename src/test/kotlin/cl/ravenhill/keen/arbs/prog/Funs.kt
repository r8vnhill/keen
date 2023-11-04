/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arbs.prog

import io.kotest.property.Arb
import io.kotest.property.arbitrary.element

fun <T> Arb.Companion.function() = element({ _: List<T> -> 0 }, { x: List<T> -> x.size })
