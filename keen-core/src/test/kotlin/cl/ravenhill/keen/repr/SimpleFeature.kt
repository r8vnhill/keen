/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.repr

import io.kotest.property.Arb
import io.kotest.property.arbitrary.map

fun <T> arbSimpleFeature(value: Arb<T>): Arb<SimpleFeature<T>> = value.map {
    SimpleFeature(it)
}

class SimpleFeature<T>(override val value: T) : Feature<T, SimpleFeature<T>> {
    override fun duplicateWithValue(value: T) = SimpleFeature(value)

    override fun toString() = "$value"
}