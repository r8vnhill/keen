package cl.ravenhill.keen


/**
 * A constraint that can be applied to a value of type [T].
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface Constraint<T> {

    /**
     * Checks if the given value fulfills the constraint.
     */
    fun validate(value: T): Result<T>
}

/**
 * Represents a constraint that can be applied to an integer.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
sealed interface IntConstraint : Constraint<Int> {

    /**
     * Constraint that checks if an integer is positive.
     */
    object Positive : IntConstraint {

        override fun validate(value: Int): Result<Int> =
            if (value <= 0) {
                Result.failure(IntConstraintException {
                    "Expected a positive number, but got $value"
                })
            } else {
                Result.success(value)
            }
    }
}

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
sealed interface PairConstraint<T, U> : Constraint<Pair<T, U>> {

    /**
     * A constraint that checks if a pair is (strictly) ordered.
     *
     * @since 2.0.0
     * @version 2.0.0
     */
    class StrictlyOrdered<A: Comparable<A>> : PairConstraint<A, A> {
        override fun validate(value: Pair<A, A>) =
            if (value.first >= value.second) {
                Result.failure(PairConstraintException {
                    "Expected a strictly ordered pair, but got $value"
                })
            } else {
                Result.success(value)
            }
    }

    object Finite : PairConstraint<Double, Double> {
        override fun validate(value: Pair<Double, Double>) =
            if (value.first.isFinite() && value.second.isFinite()) {
                Result.success(value)
            } else {
                Result.failure(PairConstraintException {
                    "Expected a finite pair, but got $value"
                })
            }
    }
}


