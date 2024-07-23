package cl.ravenhill.keen.mixins

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot


fun <T> haveRange(range: ClosedRange<T>) where T : Comparable<T> = Matcher { ranged: Ranged<T> ->
    MatcherResult(
        ranged.range == range,
        { "$ranged should be in the range $range" },
        { "$ranged should not be in the range $range" }
    )
}

infix fun <T> Ranged<T>.shouldHaveRange(range: ClosedRange<T>) where T : Comparable<T> = this should haveRange(range)

infix fun <T> Ranged<T>.shouldNotHaveRange(range: ClosedRange<T>) where T : Comparable<T> =
    this shouldNot haveRange(range)
