package cl.ravenhill.keen


/**
 * A constraint that can be applied to a value of type [T].
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface Clause<T> {

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
sealed interface IntClause : Clause<Int> {

    /**
     * Constraint that checks if an integer is positive.
     */
    object BePositive : IntClause {

        override fun validate(value: Int): Result<Int> =
            if (value <= 0) {
                Result.failure(IntClauseException {
                    "Expected a positive number, but got $value"
                })
            } else {
                Result.success(value)
            }
    }

    /**
     * Constraint that checks if an integer is in a given range.
     */
    open class BeInRange(private val range: IntRange) : IntClause {

        override fun validate(value: Int): Result<Int> =
            if (value !in range) {
                Result.failure(IntClauseException {
                    "Expected a number in range $range, but got $value"
                })
            } else {
                Result.success(value)
            }
    }

    /**
     * Constraint that checks if an integer is at least a given value.
     */
    class BeAtLeast(min: Int) : BeInRange(min..Int.MAX_VALUE)
}

/**
 * Represents a constraint that can be applied to a double.
 */
sealed interface DoubleClause : Clause<Double> {
    class BeInRange(private val range: ClosedFloatingPointRange<Double>) :
        DoubleClause {
        override fun validate(value: Double) = if (value in range) {
            Result.success(value)
        } else {
            Result.failure(DoubleClauseException {
                "Expected a number in range $range, but got $value"
            })
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
sealed interface PairClause<T, U> : Clause<Pair<T, U>> {

    /**
     * A constraint that checks if a pair is (strictly) ordered.
     *
     * @since 2.0.0
     * @version 2.0.0
     */
    class StrictlyOrdered<A : Comparable<A>> : PairClause<A, A> {
        override fun validate(value: Pair<A, A>) =
            if (value.first >= value.second) {
                Result.failure(PairClauseException {
                    "Expected a strictly ordered pair, but got $value"
                })
            } else {
                Result.success(value)
            }
    }

    object Finite : PairClause<Double, Double> {
        override fun validate(value: Pair<Double, Double>) =
            if (value.first.isFinite() && value.second.isFinite()) {
                Result.success(value)
            } else {
                Result.failure(PairClauseException {
                    "Expected a finite pair, but got $value"
                })
            }
    }
}

/**
 * Represents a constraint that can be applied to a collection.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
sealed interface CollectionClause : Clause<Collection<*>> {

    /**
     * A constraint that checks if a collection is empty.
     */
    object NotBeEmpty : CollectionClause {
        override fun validate(value: Collection<*>): Result<Collection<*>> {
            return if (value.isEmpty()) {
                Result.failure(CollectionClauseException {
                    "Expected a non-empty collection, but got $value"
                })
            } else {
                Result.success(value)
            }
        }
    }
}
