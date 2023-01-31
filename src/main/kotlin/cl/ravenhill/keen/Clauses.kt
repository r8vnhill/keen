package cl.ravenhill.keen


/**
 * A constraint that can be applied to a value of type [T].
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface Requirement<T> {

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
sealed interface IntRequirement : Requirement<Int> {
    val lazyDescription: (Int) -> String
    val validator: (Int) -> Boolean

    override fun validate(value: Int): Result<Int> =
        if (!validator(value)) {
            Result.failure(IntRequirementException { lazyDescription(value) })
        } else {
            Result.success(value)
        }

    /**
     * Constraint that checks if an integer is positive.
     */
    class BePositive(
        override val lazyDescription: (Int) -> String = { value ->
            "Expected a positive number, but got $value"
        }
    ) : IntRequirement {
        override val validator = { value: Int -> value > 0 }
    }

    /**
     * Constraint that checks if an integer is in a given range.
     */
    open class BeInRange(
        private val range: IntRange,
        override val lazyDescription: (Int) -> String = { value ->
            "Expected a number in range $range, but got $value"
        }
    ) : IntRequirement {

        override val validator = { value: Int -> value in range }
    }

    /**
     * Constraint that checks if an integer is at least a given value.
     */
    class BeAtLeast(
        min: Int, lazyDescription: (Int) -> String = { value ->
            "Expected a number at least $min, but got $value"
        }
    ) : BeInRange(min..Int.MAX_VALUE, { lazyDescription(min) })

    /**
     * Constraint that checks if an integer is at most a given value.
     */
    class BeAtMost(
        max: Int,
        lazyDescription: (Int) -> String = {
            "Expected a number at most $max, but got $it"
        }
    ) : BeInRange(Int.MIN_VALUE..max, { lazyDescription(max) })

    /**
     * Constraint that checks if an integer is equal to a given value.
     */
    class BeEqualTo(
        private val expected: Int,
        override val lazyDescription: (Int) -> String = { value ->
            "Expected $expected, but got $value"
        }
    ) : IntRequirement {

        override val validator = { value: Int -> value == expected }
    }
}

/**
 * Represents a constraint that can be applied to a double.
 */
sealed interface DoubleRequirement : Requirement<Double> {
    val validator: (Double) -> Boolean
    val lazyDescription: (Double) -> String

    override fun validate(value: Double): Result<Double> =
        if (!validator(value)) {
            Result.failure(DoubleRequirementException { lazyDescription(value) })
        } else {
            Result.success(value)
        }
    /**
     * Constraint that checks if a double is in a given range.
     */
    class BeInRange(
        private val range: ClosedFloatingPointRange<Double>,
        override val lazyDescription: (Double) -> String = { value ->
            "Expected a number in range $range, but got $value"
        }
    ) : DoubleRequirement {
        override val validator = { value: Double -> value in range }
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

/**
 * Represents a constraint that can be applied to a collection.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
sealed interface CollectionRequirement : Requirement<Collection<*>> {

    /**
     * A constraint that checks if a collection is empty.
     */
    object NotBeEmpty : CollectionRequirement {
        override fun validate(value: Collection<*>): Result<Collection<*>> {
            return if (value.isEmpty()) {
                Result.failure(CollectionRequirementException {
                    "Expected a non-empty collection, but got $value"
                })
            } else {
                Result.success(value)
            }
        }
    }
}
