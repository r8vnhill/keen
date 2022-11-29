package cl.ravenhill.keen.util.math

import org.jetbrains.annotations.Contract
import java.util.function.Consumer


class MinMax<C> private constructor(private val comparator: Comparator<in C>) : Consumer<C> {
    private var _min: C? = null
    private var _max: C? = null
    private var _count = 0L

    override fun accept(`object`: C) {
        _min = min(comparator, _min, `object`)
        _max = max(comparator, _max, `object`)
        ++_count
    }

    @Contract("_ -> this")
    fun combine(other: MinMax<C>): MinMax<C> {
        _min = min<C>(comparator, _min!!, other._min!!)
        _max = max<C>(comparator, _max!!, other._max!!)
        _count += other._count
        return this
    }

    fun count(): Long {
        return _count
    }

    fun max(): C? {
        return _max
    }

    override fun toString(): String {
        return String.format(
            "cl.ravenhill.keen.util.math.MinMax[count=%d, min=%s, max=%s]",
            _count,
            _min,
            _max
        )
    }

    companion object {
        fun <T> min(comp: Comparator<in T>, a: T?, b: T?): T? {
            return if (a != null) if (b != null) if (comp.compare(a, b) <= 0) a else b else a else b
        }

        fun <T> max(comp: Comparator<in T>, a: T?, b: T?): T? {
            return if (a != null) if (b != null) if (comp.compare(a, b) >= 0) a else b else a else b
        }

        @Contract(value = "_ -> new", pure = true)
        fun <T> of(comparator: Comparator<in T>): MinMax<T> {
            return MinMax(comparator)
        }

        @JvmStatic
        @Contract(" -> new")
        fun <C : Comparable<C>?> of(): MinMax<C> {
            return of(Comparator.naturalOrder())
        }
    }
}