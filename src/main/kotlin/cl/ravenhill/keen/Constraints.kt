package cl.ravenhill.keen


sealed interface IntConstraint {
    fun validate(i: Int): Result<Int>

    object Positive : IntConstraint {

        override fun validate(i: Int): Result<Int> =
            if (i <= 0) {
                Result.failure(IntConstraintException {
                    "Expected a positive number, but got $i"
                })
            } else {
                Result.success(i)
            }
    }
}

sealed interface PairConstraint<T, U> {
    fun validate(pair: Pair<T, U>): Result<Pair<T, U>>

    object StrictlyOrdered : PairConstraint<Int, Int> {
        override fun validate(pair: Pair<Int, Int>) =
            if (pair.first >= pair.second) {
                Result.failure(UnfulfilledConstraintException {
                    "Expected a strictly ordered pair, but got $pair"
                })
            } else {
                Result.success(pair)
            }
    }
}


