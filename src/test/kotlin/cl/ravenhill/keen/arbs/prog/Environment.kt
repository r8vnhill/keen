/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arbs.prog

import cl.ravenhill.keen.prog.Environment
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.string

/**
 * Provides an arbitrary factory method for generating random `Environment` instances for property-based testing.
 * This utility function is part of the `Arb` companion object, integrating seamlessly with property testing frameworks
 * that use the `Arb` type for generating random test data.
 *
 * When invoked, it creates an `Environment` instance with a randomly generated string identifier. This can be
 * particularly useful when testing functions or classes that interact with different environments, and you need a
 * variety of `Environment` instances for comprehensive testing.
 *
 * @param U the upper bound of the type of values stored in the environment.
 *
 * @return An arbitrary of `Environment` with a randomly generated string as its ID.
 */
fun <U> Arb.Companion.environment() = arbitrary {
    Environment<U>(string().bind())
}

fun <U> Arb.Companion.environment(size: Arb<Int>, value: Arb<U>) = arbitrary {
    val env = Environment<U>(string().bind())
    repeat(size.bind()) {
        env += it to value.bind()
    }
    env
}