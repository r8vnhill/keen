/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.repr

import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map

/**
 * A simple implementation of the `Feature` interface that holds an integer value.
 *
 * @property value The integer value that this feature holds.
 *
 * @constructor Creates a `SimpleFeature` with the specified integer value.
 *
 * @see Feature
 */
data class SimpleFeature(override val value: Int) : Feature<Int, SimpleFeature> {
    /**
     * Flattens the feature into a list containing its value.
     *
     * @return A list containing the integer value of this feature.
     */
    override fun toList() = listOf(value)

    /**
     * Creates a copy of this feature with a new value.
     *
     * @param value The new integer value to assign to the copied feature.
     * @return A new `SimpleFeature` instance with the specified value.
     */
    override fun copyWithValue(value: Int) = SimpleFeature(value)
}

/**
 * Generates an arbitrary instance of `SimpleFeature` using the provided integer generator.
 */
fun arbSimpleFeature(arb: Arb<Int> = Arb.int()) = arb.map(::SimpleFeature)
