/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.jakt.constraints


/**
 * A [PairConstraint] is a [Constraint] that specifies constraints on a [Pair] of values of types
 * [T] and [U].
 *
 * @param T The type of the first element of the pair.
 * @param U The type of the second element of the pair.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
sealed interface PairConstraint<T, U> : Constraint<Pair<T, U>> {

    /// Documentation inherited from [Requirement].
    override fun generateException(description: String) =
        cl.ravenhill.jakt.exceptions.PairRequirementException { description }

    /**
     * [BeStrictlyOrdered] is a [PairConstraint] that requires that the first element in a [Pair]
     * is strictly less than the second element, where both elements must be of the same comparable
     * type [A].
     */
    class BeStrictlyOrdered<A : Comparable<A>> : PairConstraint<A, A> {
        /// Documentation inherited from [Requirement].
        override val validator = { value: Pair<A, A> -> value.first < value.second }


        /// Documentation inherited from [Any].
        override fun toString() = "BeStrictlyOrdered<~>"
    }

    /**
     * [BeFinite] is a [PairConstraint] that requires that both elements in a [Pair] are finite
     * doubles.
     */
    data object BeFinite : PairConstraint<Double, Double> {
        override val validator =
            { value: Pair<Double, Double> -> value.first.isFinite() && value.second.isFinite() }
    }
}

