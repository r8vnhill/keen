package cl.ravenhill.keen.requirements

import cl.ravenhill.keen.PairRequirementException

/**
 * Represents a constraint that can be applied to a pair.
 *
 * @param T The type of the first element of the pair.
 * @param U The type of the second element of the pair.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
sealed interface PairRequirement<T, U> : Requirement<Pair<T, U>> {

    // Inherit documentation from Requirement
    override fun generateException(description: String) = PairRequirementException { description }

    /**
     * A constraint that checks if a pair is (strictly) ordered.
     *
     * @since 2.0.0
     * @version 2.0.0
     */
    class StrictlyOrdered<A : Comparable<A>>(
        override val lazyDescription: (Pair<A, A>) -> String = { value ->
            "Expected a strictly ordered pair, but got $value"
        }
    ) : PairRequirement<A, A> {
        override val validator = { value: Pair<A, A> -> value.first < value.second }
    }

    class Finite(
        override val lazyDescription: (Pair<Double, Double>) -> String = { value ->
            "Expected a finite pair, but got $value"
        }
    ) : PairRequirement<Double, Double> {
        override val validator =
            { value: Pair<Double, Double> -> value.first.isFinite() && value.second.isFinite() }
    }
}