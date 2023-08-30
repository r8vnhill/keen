/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.prog.functions

class Add : Fun<Double>("+", 2, { it[0] + it[1] })
