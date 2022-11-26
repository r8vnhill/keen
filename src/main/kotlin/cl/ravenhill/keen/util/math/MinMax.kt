//package cl.ravenhill.keen.util.math
//
//import java.util.function.Consumer
//
//class MinMax<C: Any> private constructor(comparator: Comparator<in C>) : Consumer<C> {
//    private val _comparator: Comparator<in C>
//    private lateinit var _min: C
//    private lateinit var _max: C
//    private var _count = 0L
//
//    init {
//        _comparator = comparator
//    }
//
//    /**
//     * Accept the element for min-max calculation.
//     *
//     * @param object the element to use for min-max calculation
//     */
//    override fun accept(`object`: C) {
//        _min = min(_comparator, _min, `object`)
//        _max = max(_comparator, _max, `object`)
//        ++_count
//    }
//
//    /**
//     * Combine two `cl.ravenhill.keen.util.math.MinMax` objects.
//     *
//     * @param other the other `cl.ravenhill.keen.util.math.MinMax` object to combine
//     * @return `this`
//     * @throws java.lang.NullPointerException if the `other` object is
//     * `null`.
//     */
//    fun combine(other: MinMax<C>): MinMax<C> {
//        _min = min(_comparator, _min, other._min)
//        _max = max(_comparator, _max, other._max)
//        _count += other._count
//        return this
//    }
//
//    /**
//     * Returns the count of values recorded.
//     *
//     * @return the count of recorded values
//     */
//    fun count(): Long {
//        return _count
//    }
//
//    /**
//     * Return the current maximal object or `null` if no element has been
//     * accepted yet.
//     *
//     * @return the current maximal object
//     */
//    fun max(): C {
//        return _max
//    }
//
//    override fun toString(): String {
//        return String.format("cl.ravenhill.keen.util.math.MinMax[count=%d, min=%s, max=%s]", _count, _min, _max)
//    }
//
//    companion object {
//        /* *************************************************************************
//	 *  Some static helper methods.
//	 * ************************************************************************/
//        /**
//         * Return the minimum of two values, according the given comparator.
//         * `null` values are allowed.
//         *
//         * @param comp the comparator used for determining the min value
//         * @param a the first value to compare
//         * @param b the second value to compare
//         * @param <T> the type of the compared objects
//         * @return the minimum value, or `null` if both values are `null`.
//         * If only one value is `null`, the non `null` values is
//         * returned.
//        </T> */
//        fun <T> min(comp: Comparator<in T>, a: T, b: T): T {
//            return if (a != null) if (b != null) if (comp.compare(a, b) <= 0) a else b else a else b
//        }
//
//        /**
//         * Return the maximum of two values, according the given comparator.
//         * `null` values are allowed.
//         *
//         * @param comp the comparator used for determining the max value
//         * @param a the first value to compare
//         * @param b the second value to compare
//         * @param <T> the type of the compared objects
//         * @return the maximum value, or `null` if both values are `null`.
//         * If only one value is `null`, the non `null` values is
//         * returned.
//        </T> */
//        fun <T> max(comp: Comparator<in T>, a: T, b: T): T {
//            return if (a != null) if (b != null) if (comp.compare(a, b) >= 0) a else b else a else b
//        }
//        /* *************************************************************************
//	 *  Some static factory methods.
//	 * ************************************************************************/
//
//        /**
//         * Create a new `cl.ravenhill.keen.util.math.MinMax` *consumer* with the given
//         * [java.util.Comparator].
//         *
//         * @param comparator the comparator used for comparing two elements
//         * @param <T> the element type
//         * @return a new `cl.ravenhill.keen.util.math.MinMax` *consumer*
//         * @throws java.lang.NullPointerException if the `comparator` is
//         * `null`.
//        </T> */
//        fun <T: Comparable<T>> of(comparator: Comparator<in T>): MinMax<T> {
//            return MinMax(comparator)
//        }
//
//        /**
//         * Create a new `cl.ravenhill.keen.util.math.MinMax` *consumer*.
//         *
//         * @param <C> the element type
//         * @return a new `cl.ravenhill.keen.util.math.MinMax` *consumer*
//        </C> */
//        fun <C : Comparable<C>> of(): MinMax<C> {
//            return of(Comparator.naturalOrder())
//        }
//        /* *************************************************************************
//	 *  Some "flat" mapper functions.
//	 * ************************************************************************/
//
//
//    }
//}