/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill

import io.kotest.property.Arb
import io.kotest.property.Shrinker
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.map
import kotlin.jvm.JvmInline

/**
 * A wrapper class that associates a name with a value.
 *
 * @param T The type of the value being bound to the name.
 * @property binding A `Pair` containing the name as a `String` and the associated value of type `T`.
 */
@JvmInline
value class Named<T>(private val binding: Pair<String, T>) {

    /**
     * Returns a string representation of the `Named` instance in the format `name=value`.
     *
     * @return A string in the format `name=value`, where `name` is the first component of the `Pair` and `value` is the
     *   second component.
     */
    override fun toString() = "${binding.first}=${binding.second}"

    /**
     * Extracts the name component from the `Named` instance.
     *
     * @return The name component (`String`) from the `binding` pair.
     */
    operator fun component1() = binding.first

    /**
     * Extracts the value component from the `Named` instance.
     *
     * @return The value component of type `T` from the `binding` pair.
     */
    operator fun component2() = unwrap()

    /**
     * Unwraps the value from the `Named` instance.
     *
     * @return The value associated with the name.
     */
    fun unwrap() = binding.second
}

/**
 * Binds a string to a value, creating a named pair.
 *
 * @param T The type of the value being bound to the string.
 * @param value The value to be bound to the string.
 * @return A `Named` instance containing the string and the associated value as a pair.
 */
infix fun <T> String.boundTo(value: T) = Named(this to value)

/**
 * A data class that represents a pair of named values.
 *
 * @param T The type of the value held by the first `Named` object.
 * @param U The type of the value held by the second `Named` object.
 * @property first The first `Named` object in the pair.
 * @property second The second `Named` object in the pair.
 */
data class NamedPair<T, U>(val first: Named<T>, val second: Named<U>) {
    override fun toString() = "($first, $second)"
}

/**
 * An infix function that creates a `NamedPair` from two `Named` objects.
 *
 * @param T The type of the value held by the first `Named` object.
 * @param U The type of the value held by the second `Named` object.
 * @param other The second `Named` object to be paired with the current `Named` object.
 * @return A `NamedPair` containing the two `Named` objects.
 */
infix fun <T, U> Named<T>.and(other: Named<U>) = NamedPair(this, other)

/**
 * Wraps an arbitrary value in a `Named` instance, associating it with a specified name.
 *
 * @param T The type of the value being wrapped in the `Named` instance.
 * @param name The name to associate with the generated values.
 * @param arb The original `Arb<T>` that generates values of type `T`.
 * @return An `Arb<Named<T>>` that generates named values.
 */
fun <T> arbNamed(name: String, arb: Arb<T>, valueShrinker: Shrinker<T>) =
    arbitrary(NamedShrinker(valueShrinker)) {
        arb.map { name boundTo it }.bind()
    }

/**
 * Generates pairs of named values using two `Arb<Named<T>>` instances.
 *
 * @param T The type of the first value in the pair.
 * @param U The type of the second value in the pair.
 * @param firstArb The `Arb<Named<T>>` instance that generates the first named value.
 * @param secondArb The `Arb<Named<U>>` instance that generates the second named value.
 * @return An `Arb<Named<Pair<T, U>>>` that generates pairs of named values.
 */
fun <T, U> arbNamedPair(firstArb: Arb<Named<T>>, secondArb: Arb<Named<U>>) =
    Arb.bind(firstArb, secondArb) { first, second -> first and second }

class NamedShrinker<T>(private val valueShrinker: Shrinker<T>) : Shrinker<Named<T>> {

    override fun shrink(value: Named<T>) =
        valueShrinker.shrink(value.unwrap()).map { value.component1() boundTo it }
}
