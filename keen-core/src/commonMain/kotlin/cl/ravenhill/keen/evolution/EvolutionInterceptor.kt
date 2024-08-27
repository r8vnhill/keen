/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution

class EvolutionInterceptor<T, F, R, S>(
    val before: (S) -> S,
    val after: (S) -> S
)
