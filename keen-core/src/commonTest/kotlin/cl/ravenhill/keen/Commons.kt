/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.list

/**
 * Generates an arbitrary `Double` value that is guaranteed to be non-NaN.
 *
 * @return An `Arb<Double>` that generates double values, excluding `NaN`.
 */
fun arbNonNanDouble(): Arb<Double> = Arb.double().filterNot(Double::isNaN)

/**
 * Creates an `Arb` (arbitrary generator) for generating lists of `Double` values with a fixed size.
 *
 * @param n The fixed size of the lists to be generated.
 * @param arb The `Arb<Double>` generator used to produce the elements in the list.
 * @return An `Arb<List<Double>>` that generates lists with exactly `n` `Double` values.
 */
fun <T> arbListOfN(n: Int, arb: Arb<T>): Arb<List<T>> = Arb.list(arb, n..n)
