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

    /**
     * A constraint that checks if a pair is (strictly) ordered.
     *
     * @since 2.0.0
     * @version 2.0.0
     */
    class StrictlyOrdered<A : Comparable<A>> : PairRequirement<A, A> {
        override fun validate(value: Pair<A, A>) =
            if (value.first >= value.second) {
                Result.failure(PairRequirementException {
                    "Expected a strictly ordered pair, but got $value"
                })
            } else {
                Result.success(value)
            }
    }

    object Finite : PairRequirement<Double, Double> {
        override fun validate(value: Pair<Double, Double>) =
            if (value.first.isFinite() && value.second.isFinite()) {
                Result.success(value)
            } else {
                Result.failure(PairRequirementException {
                    "Expected a finite pair, but got $value"
                })
            }
    }
}